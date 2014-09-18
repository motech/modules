package org.motechproject.ivr.web;

import org.motechproject.admin.service.StatusMessageService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.ivr.domain.CallDetailRecord;
import org.motechproject.ivr.domain.Config;
import org.motechproject.ivr.event.EventParams;
import org.motechproject.ivr.event.EventSubjects;
import org.motechproject.ivr.repository.CallDetailRecordDataService;
import org.motechproject.ivr.service.CallInitiationException;
import org.motechproject.ivr.service.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private ConfigService configService;
    private StatusMessageService statusMessageService;
    private EventRelay eventRelay;
    private static final String MODULE_NAME = "ivr";

    @Autowired
    public StatusController(CallDetailRecordDataService callDetailRecordDataService, EventRelay eventRelay,
                            @Qualifier("configService") ConfigService configService,
                            StatusMessageService statusMessageService) {
        this.callDetailRecordDataService = callDetailRecordDataService;
        this.eventRelay = eventRelay;
        this.configService = configService;
        this.statusMessageService = statusMessageService;
    }

    /**
     * Listens to HTTP calls to http://{server}:{port}/module/ivr/status/{config}?key1=val1&key2=val2&... from IVR
     * providers. Creates a corresponding CDR entity in the database. Sends a MOTECH message with the CDR data in the
     * payload and the call status as the subject.
     *
     * @param configName
     * @param params
     * @return static XML content with an OK response element.
     */
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(value = "/{configName}", produces = "text/xml")
    public String handle(@PathVariable String configName, @RequestParam Map<String, String> params,
                         @RequestHeader Map<String, String> headers) {
        LOGGER.debug(String.format("handle(configName = %s, parameters = %s, headers = %s)", configName, params,
                headers));

        if (!configService.hasConfig(configName)) {
            String msg = String.format("Invalid config: '%s'", configName);
            LOGGER.error(msg);
            statusMessageService.warn(msg, MODULE_NAME);
            throw new CallInitiationException(msg);
        }

        Config config = configService.getConfig(configName);

        // Construct a CDR from the URL query parameters passed in the callback
        CallDetailRecord callDetailRecord = new CallDetailRecord();

        callDetailRecord.setConfigName(configName);

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
        MotechEvent event = new MotechEvent(EventSubjects.CALL_STATUS, eventParams);
        LOGGER.debug("Sending MotechEvent {}", event.toString());
        eventRelay.sendEventMessage(event);

        // Save the CDR
        LOGGER.debug("Saving CallDetailRecord {}", callDetailRecord);
        callDetailRecordDataService.create(callDetailRecord);

        return "<?xml version=\"1.0\"?><response>OK</response>";
    }
}
