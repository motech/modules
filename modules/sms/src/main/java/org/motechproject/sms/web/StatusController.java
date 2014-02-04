package org.motechproject.sms.web;

import org.motechproject.commons.couchdb.query.QueryParam;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.server.config.SettingsFacade;
import org.motechproject.sms.SmsEventSubjects;
import org.motechproject.sms.alert.MotechStatusMessage;
import org.motechproject.sms.audit.DeliveryStatus;
import org.motechproject.sms.audit.SmsAuditService;
import org.motechproject.sms.audit.SmsRecord;
import org.motechproject.sms.audit.SmsRecordSearchCriteria;
import org.motechproject.sms.audit.SmsRecords;
import org.motechproject.sms.configs.Config;
import org.motechproject.sms.configs.ConfigReader;
import org.motechproject.sms.configs.Configs;
import org.motechproject.sms.templates.Status;
import org.motechproject.sms.templates.Template;
import org.motechproject.sms.templates.TemplateReader;
import org.motechproject.sms.templates.Templates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.motechproject.commons.date.util.DateUtil.now;
import static org.motechproject.sms.SmsEvents.outboundEvent;
import static org.motechproject.sms.audit.SmsDirection.OUTBOUND;

/**
 * Handles message delivery status updates sent by sms providers to
 * {motechserver}/motech-platform-server/module/sms/status{Config}
 */
@Controller
@RequestMapping(value = "/status")
public class StatusController {

    @Autowired
    private MotechStatusMessage motechStatusMessage;
    private Logger logger = LoggerFactory.getLogger(StatusController.class);
    private ConfigReader configReader;
    private Configs configs;
    private Templates templates;
    private EventRelay eventRelay;
    private SmsAuditService smsAuditService;
    private static final int RECORD_FIND_RETRY_COUNT = 3;
    private static final int RECORD_FIND_TIMEOUT = 500;

    @Autowired
    public StatusController(@Qualifier("smsSettings") SettingsFacade settingsFacade, EventRelay eventRelay,
                            TemplateReader templateReader, SmsAuditService smsAuditService) {
        this.eventRelay = eventRelay;
        configReader = new ConfigReader(settingsFacade);
        //todo: this means we'd crash/error out when a new config is created and we get a status update callback before
        //todo: restarting the module  -  so for now we'll read configs each time handle() gets called
        //todo: but ultimately we'll want something like: configs = configReader.getConfigs()
        templates = templateReader.getTemplates();
        this.smsAuditService = smsAuditService;
    }

    private SmsRecord findFirstByProviderMessageId(SmsRecords smsRecords, String providerMessageId) {
        for (SmsRecord smsRecord : smsRecords.getRecords()) {
            if (smsRecord.getProviderId().equals(providerMessageId)) {
                return smsRecord;
            }
        }
        return null;
    }

    private SmsRecord findOrCreateSmsRecord(String configName, String providerMessageId, String statusString) {
        int retry = 0;
        SmsRecord smsRecord;
        SmsRecords smsRecords;
        SmsRecord existingSmsRecord = null;
        QueryParam queryParam = new QueryParam();

        queryParam.setSortBy("timestamp");
        queryParam.setReverse(true);

        // Try to find an existing SMS record using the provider message ID
        // NOTE: Only works if the provider guarantees the message id is unique. So far, all do.
        do {
            //seems that lucene takes a while to index, so try a couple of times and delay in between
            if (retry > 0) {
                logger.debug("Trying again to find log record with motechId {}, try {}", providerMessageId, retry + 1);
                try {
                    Thread.sleep(RECORD_FIND_TIMEOUT);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            smsRecords = smsAuditService.findAllSmsRecords(new SmsRecordSearchCriteria()
                    .withConfig(configName)
                    .withProviderId(providerMessageId)
                    .withQueryParam(queryParam));
            retry++;
        } while (retry < RECORD_FIND_RETRY_COUNT && CollectionUtils.isEmpty(smsRecords.getRecords()));
        if (CollectionUtils.isEmpty(smsRecords.getRecords())) {
            // If we couldn't find a record by provider message ID try using the MOTECH ID
            smsRecords = smsAuditService.findAllSmsRecords(new SmsRecordSearchCriteria()
                    .withConfig(configName)
                    .withMotechId(providerMessageId)
                    .withQueryParam(queryParam));
            if (!CollectionUtils.isEmpty(smsRecords.getRecords())) {
                logger.debug("Found log record with matching motechId {}", providerMessageId);
                existingSmsRecord = smsRecords.getRecords().get(0);
            }
        } else {
            //todo: temporary kludge: lucene can't find exact strings, so we're looping on results until we find
            //todo: an exact match. Remove when we switch to SEUSS.
            existingSmsRecord = findFirstByProviderMessageId(smsRecords, providerMessageId);
            if (existingSmsRecord != null) {
                logger.debug("Found log record with matching providerId {}", providerMessageId);
            }
        }

        if (existingSmsRecord == null) {
            String msg = String.format("Received status update but couldn't find a log record with matching " +
                    "ProviderMessageId or motechId: %s", providerMessageId);
            logger.error(msg);
            motechStatusMessage.alert(msg);
        }

        if (existingSmsRecord != null) {
            smsRecord = new SmsRecord(configName, OUTBOUND, existingSmsRecord.getPhoneNumber(),
                    existingSmsRecord.getMessageContent(), now(), null, statusString,
                    existingSmsRecord.getMotechId(), providerMessageId, null);
        } else {
            //start with an empty SMS record
            smsRecord = new SmsRecord(configName, OUTBOUND, null, null, now(), null, statusString, null,
                    providerMessageId, null);
        }

        return smsRecord;
    }

    private void analyzeStatus(Status status, String configName, Map<String, String> params) {
        String statusString = params.get(status.getStatusKey());
        String providerMessageId = params.get(status.getMessageIdKey());
        SmsRecord smsRecord = findOrCreateSmsRecord(configName, providerMessageId, statusString);
        List<String> recipients = Arrays.asList(new String[]{smsRecord.getPhoneNumber()});

        if (statusString != null) {
            String eventSubject;
            if (statusString.matches(status.getStatusSuccess())) {
                smsRecord.setDeliveryStatus(DeliveryStatus.DELIVERY_CONFIRMED);
                eventSubject = SmsEventSubjects.DELIVERY_CONFIRMED;
            } else if (status.hasStatusFailure() && statusString.matches(status.getStatusFailure())) {
                smsRecord.setDeliveryStatus(DeliveryStatus.FAILURE_CONFIRMED);
                eventSubject = SmsEventSubjects.FAILURE_CONFIRMED;
            } else {
                // If we're not certain the message was delivered or failed, then it's in the DISPATCHED gray area
                smsRecord.setDeliveryStatus(DeliveryStatus.DISPATCHED);
                eventSubject = SmsEventSubjects.DISPATCHED;
            }
            eventRelay.sendEventMessage(outboundEvent(eventSubject, configName, recipients,
                    smsRecord.getMessageContent(), smsRecord.getMotechId(), providerMessageId, null, statusString,
                    now()));
        } else {
            String msg = String.format("Likely template error, unable to extract status string. Config: %s, Parameters: %s",
                    configName, params);
            logger.error(msg);
            motechStatusMessage.alert(msg);
            smsRecord.setDeliveryStatus(DeliveryStatus.FAILURE_CONFIRMED);
            eventRelay.sendEventMessage(outboundEvent(SmsEventSubjects.FAILURE_CONFIRMED, configName, recipients,
                    smsRecord.getMessageContent(), smsRecord.getMotechId(), providerMessageId, null, null,
                    now()));
        }
        smsAuditService.log(smsRecord);
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(value = "/{configName}")
    public void handle(@PathVariable String configName, @RequestParam Map<String, String> params) {
        logger.info("SMS Status - configName = {}, params = {}", configName, params);

        //todo: for now re-read configs every call, we'll change to the new config notifications when it's ready
        configs = configReader.getConfigs();

        Config config;
        if (configs.hasConfig(configName)) {
            config = configs.getConfig(configName);
        } else {
            String msg = String.format("Received SMS Status for '%s' config but no matching config: %s", configName,
                    params);
            logger.error(msg);
            motechStatusMessage.alert(msg);
            config = configs.getDefaultConfig();
        }
        Template template = templates.getTemplate(config.getTemplateName());
        Status status = template.getStatus();

        if (status.hasMessageIdKey() && params != null && params.containsKey(status.getMessageIdKey())) {
            if (status.hasStatusKey() && status.hasStatusSuccess()) {
                analyzeStatus(status, configName, params);
            } else {
                String msg = String.format("We have a message id, but don't know how to extract message status, this is most likely a template error. Config: %s, Parameters: %s",
                        configName, params);
                logger.error(msg);
                motechStatusMessage.alert(msg);
            }
        } else {
            String msg = String.format("Status message received from provider, but no template support! Config: %s, Parameters: %s",
                    configName, params);
            logger.error(msg);
            motechStatusMessage.alert(msg);
        }
    }
}
