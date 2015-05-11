package org.motechproject.ivr.web;

import org.motechproject.admin.service.StatusMessageService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.ivr.domain.CallDetailRecord;
import org.motechproject.ivr.domain.Config;
import org.motechproject.ivr.event.EventParams;
import org.motechproject.ivr.exception.ConfigNotFoundException;
import org.motechproject.ivr.repository.CallDetailRecordDataService;
import org.motechproject.ivr.service.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Helper class used to log CDR and send Motech events shared by the status & template controllers
 */
public final class LogAndEventHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatusController.class);
    private static final String MODULE_NAME = "ivr";

    // Private default constructor to prevent instantiation of this helper class
    private LogAndEventHelper() { }

    /**
     * The StatusController and TemplateController classes both have the same way of generating a CDR, sending a Motech
     * event and logging it, so this function is called by both to avoid code duplication.
     *
     * @param configService the service used for config retrieval
     * @param cdrService the service used for logging call detail records
     * @param messageService the admin messages service, used for posting admin messages
     * @param eventRelay the event relay used for publishing MOTECH events
     * @param configName name of the configuration used
     * @param params the parameters of the call to log
     */
    public static void sendAndLogEvent(String eventSubject, ConfigService configService, //NO CHECKSTYLE ArgumentCount
                                       CallDetailRecordDataService cdrService, StatusMessageService messageService,
                                       EventRelay eventRelay, String configName, String templateName,
                                       Map<String, String> params) {
        if (!configService.hasConfig(configName)) {
            String msg = String.format("Invalid config: '%s'", configName);
            messageService.warn(msg, MODULE_NAME);
            throw new ConfigNotFoundException(msg);
        }

        Config config = configService.getConfig(configName);

        // Construct a CDR from the URL query parameters passed in the callback
        CallDetailRecord callDetailRecord = new CallDetailRecord();

        callDetailRecord.setConfigName(configName);
        callDetailRecord.setTemplateName(templateName);

        //todo: some providers send session information (including caller id) through the headers, so we should add
        //todo: a config setting that scans the headers for CDR info in addition to the query parameters

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (config.shouldIgnoreField(entry.getKey())) {
                LOGGER.debug("Ignoring provider field '{}' with value '{}'", entry.getKey(), entry.getValue());
            } else {
                callDetailRecord.setField(config.mapStatusField(entry.getKey()), entry.getValue());
            }
        }

        // Generate a MOTECH event
        Map<String, Object> eventParams = EventParams.eventParamsFromCallDetailRecord(callDetailRecord);
        MotechEvent event = new MotechEvent(eventSubject, eventParams);
        LOGGER.debug("Sending MotechEvent {}", event.toString());
        eventRelay.sendEventMessage(event);

        // Save the CDR
        LOGGER.debug("Saving CallDetailRecord {}", callDetailRecord);
        cdrService.create(callDetailRecord);

    }
}
