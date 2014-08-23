package org.motechproject.ivr.web;

import org.motechproject.admin.service.StatusMessageService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.ivr.event.EventParams;
import org.motechproject.ivr.event.EventSubjects;
import org.motechproject.ivr.domain.CallDetailRecord;
import org.motechproject.ivr.domain.Config;
import org.motechproject.ivr.repository.CallDetailRecordDataService;
import org.motechproject.ivr.repository.ConfigDataService;
import org.motechproject.ivr.service.CallInitiationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

/**
 * Responds to HTTP queries to {motech-server}/module/ivr/status/{configName} by creating a CallDetailRecord entry in
 * the database and posting a corresponding Motech event on the queue.
 */
@Controller
@RequestMapping(value = "/status")
public class StatusController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusController.class);
    private CallDetailRecordDataService callDetailRecordDataService;
    private ConfigDataService configDataService;
    private StatusMessageService statusMessageService;
    private EventRelay eventRelay;
    private static final String MODULE_NAME = "ivr";

    @Autowired
    public StatusController(CallDetailRecordDataService callDetailRecordDataService, EventRelay eventRelay,
                            ConfigDataService configDataService, StatusMessageService statusMessageService) {
        this.callDetailRecordDataService = callDetailRecordDataService;
        this.eventRelay = eventRelay;
        this.configDataService = configDataService;
        this.statusMessageService = statusMessageService;
    }

    /**
     * Listens to HTTP calls to http://{server}:{port}/module/ivr/status/{config}?param1=val1&param2=val2&... from IVR
     * providers. Creates a corresponding CDR entity in the database. Sends a MOTECH message with the CDR data in the
     * payload and the call status as the subject.
     *
     * @param configName
     * @param params
     */
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(value = "/{configName}", produces = "text/xml")
    public void handle(@PathVariable String configName, @RequestParam Map<String, String> params,
                       @RequestHeader Map<String, String> headers) {
        LOGGER.debug(String.format("handle(configName = %s, params = %s, headers = %s)", configName, params, headers));

        Config config = null;

        config = configDataService.findByName(configName);
        LOGGER.debug("handle(): read the following config from the database: {}", config);
        if (null == config) {
            String message = String.format("Invalid config: {}", configName);
            LOGGER.warn(message);
            statusMessageService.warn(message, MODULE_NAME);
            throw new CallInitiationException(message);
        }

        // Construct a CDR from the URL query parameters passed in the callback
        CallDetailRecord callDetailRecord = new CallDetailRecord();

        callDetailRecord.setConfigName(configName);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (config.shouldIgnoreField(entry.getKey())) {
                LOGGER.debug("Ignoring provider field '{}' with value '{}'", entry.getKey(), entry.getValue());
            } else {
                callDetailRecord.setField(config.mapStatusField(entry.getKey()), entry.getValue());
            }
        }

        // Generate a MOTECH event
        Map<String, Object> eventParams = EventParams.eventParamsFromCallDetailRecord(callDetailRecord);
        MotechEvent event = new MotechEvent(EventSubjects.CALL_STATUS, eventParams);
        LOGGER.debug("Sending MotechEvent {}", event.toString());
        eventRelay.sendEventMessage(event);

        // Save the CDR
        LOGGER.debug("Saving CallDetailRecord {}", callDetailRecord);
        callDetailRecordDataService.create(callDetailRecord);
    }
}
