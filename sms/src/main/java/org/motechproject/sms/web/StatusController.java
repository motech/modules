package org.motechproject.sms.web;

import org.motechproject.admin.service.StatusMessageService;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.mds.query.QueryParams;
import org.motechproject.mds.util.Order;
import org.motechproject.sms.audit.DeliveryStatus;
import org.motechproject.sms.audit.SmsAuditService;
import org.motechproject.sms.audit.SmsRecord;
import org.motechproject.sms.audit.SmsRecordSearchCriteria;
import org.motechproject.sms.audit.SmsRecords;
import org.motechproject.sms.audit.SmsRecordsDataService;
import org.motechproject.sms.configs.Config;
import org.motechproject.sms.service.ConfigService;
import org.motechproject.sms.service.TemplateService;
import org.motechproject.sms.templates.Status;
import org.motechproject.sms.templates.Template;
import org.motechproject.sms.util.SmsEventSubjects;
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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.motechproject.commons.date.util.DateUtil.now;
import static org.motechproject.sms.audit.SmsDirection.OUTBOUND;
import static org.motechproject.sms.util.SmsEvents.outboundEvent;

/**
 * Handles message delivery status updates sent by sms providers to
 * {motechserver}/motech-platform-server/module/sms/status{Config}
 */
@Controller
@RequestMapping(value = "/status")
public class StatusController {

    private static final int RECORD_FIND_RETRY_COUNT = 3;
    private static final int RECORD_FIND_TIMEOUT = 500;
    private static final String SMS_MODULE = "motech-sms";

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusController.class);

    private StatusMessageService statusMessageService;
    private EventRelay eventRelay;
    private SmsAuditService smsAuditService;
    private TemplateService templateService;
    private ConfigService configService;
    private SmsRecordsDataService smsRecordsDataService;

    @Autowired
    public StatusController(@Qualifier("templateService") TemplateService templateService,
                            @Qualifier("configService") ConfigService configService,
                            EventRelay eventRelay, StatusMessageService statusMessageService,
                            SmsAuditService smsAuditService, SmsRecordsDataService smsRecordsDataService
                            ) {
        this.templateService = templateService;
        this.configService = configService;
        this.eventRelay = eventRelay;
        this.statusMessageService = statusMessageService;
        this.smsAuditService = smsAuditService;
        this.smsRecordsDataService = smsRecordsDataService;
    }

    /**
     * Handles a status update from a provider. This method will result in publishing a MOTECH Event and creating
     * a record in the database.
     * @param configName the name of the configuration for the provider that is sending the update
     * @param params params of the request sent by the provider
     */
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(value = "/{configName}")
    public void handle(@PathVariable String configName, @RequestParam Map<String, String> params) {
        LOGGER.info("SMS Status - configName = {}, params = {}", configName, params);

        if (!configService.hasConfig(configName)) {
            String msg = String.format("Received SMS Status for '%s' config but no matching config: %s, " +
                            "will try the default config", configName, params);
            LOGGER.error(msg);
            statusMessageService.warn(msg, SMS_MODULE);
        }
        Config config = configService.getConfigOrDefault(configName);
        Template template = templateService.getTemplate(config.getTemplateName());
        Status status = template.getStatus();

        if (status.hasMessageIdKey() && params != null && params.containsKey(status.getMessageIdKey())) {
            if (status.hasStatusKey() && status.hasStatusSuccess()) {
                analyzeStatus(status, configName, params);
            } else {
                String msg = String.format("We have a message id, but don't know how to extract message status, this is most likely a template error. Config: %s, Parameters: %s",
                        configName, params);
                LOGGER.error(msg);
                statusMessageService.warn(msg, SMS_MODULE);
            }
        } else {
            String msg = String.format("Status message received from provider, but no template support! Config: %s, Parameters: %s",
                    configName, params);
            LOGGER.error(msg);
            statusMessageService.warn(msg, SMS_MODULE);
        }
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
        QueryParams queryParams = new QueryParams(new Order("timestamp", Order.Direction.DESC));

        // Try to find an existing SMS record using the provider message ID
        // NOTE: Only works if the provider guarantees the message id is unique. So far, all do.
        do {
            //seems that lucene takes a while to index, so try a couple of times and delay in between
            if (retry > 0) {
                LOGGER.debug("Trying again to find log record with motechId {}, try {}", providerMessageId, retry + 1);
                try {
                    Thread.sleep(RECORD_FIND_TIMEOUT);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            smsRecords = smsAuditService.findAllSmsRecords(new SmsRecordSearchCriteria()
                    .withConfig(configName)
                    .withProviderId(providerMessageId)
                    .withQueryParams(queryParams));
            retry++;
        } while (retry < RECORD_FIND_RETRY_COUNT && CollectionUtils.isEmpty(smsRecords.getRecords()));

        if (CollectionUtils.isEmpty(smsRecords.getRecords())) {
            // If we couldn't find a record by provider message ID try using the MOTECH ID
            smsRecords = smsAuditService.findAllSmsRecords(new SmsRecordSearchCriteria()
                    .withConfig(configName)
                    .withMotechId(providerMessageId)
                    .withQueryParams(queryParams));
            if (!CollectionUtils.isEmpty(smsRecords.getRecords())) {
                LOGGER.debug("Found log record with matching motechId {}", providerMessageId);
                existingSmsRecord = smsRecords.getRecords().get(0);
            }
        } else {
            //todo: temporary kludge: lucene can't find exact strings, so we're looping on results until we find
            //todo: an exact match. Remove when we switch to SEUSS.
            existingSmsRecord = findFirstByProviderMessageId(smsRecords, providerMessageId);
            if (existingSmsRecord != null) {
                LOGGER.debug("Found log record with matching providerId {}", providerMessageId);
            }
        }

        if (existingSmsRecord == null) {
            String msg = String.format("Received status update but couldn't find a log record with matching " +
                    "ProviderMessageId or motechId: %s", providerMessageId);
            LOGGER.error(msg);
            statusMessageService.warn(msg, SMS_MODULE);
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
        List<String> recipients = Collections.singletonList(smsRecord.getPhoneNumber());

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
                    now(), null));
        } else {
            String msg = String.format("Likely template error, unable to extract status string. Config: %s, Parameters: %s",
                    configName, params);
            LOGGER.error(msg);
            statusMessageService.warn(msg, SMS_MODULE);
            smsRecord.setDeliveryStatus(DeliveryStatus.FAILURE_CONFIRMED);
            eventRelay.sendEventMessage(outboundEvent(SmsEventSubjects.FAILURE_CONFIRMED, configName, recipients,
                    smsRecord.getMessageContent(), smsRecord.getMotechId(), providerMessageId, null, null,
                    now(), null));
        }
        smsRecordsDataService.create(smsRecord);
    }
}
