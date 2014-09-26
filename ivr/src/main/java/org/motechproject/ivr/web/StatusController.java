package org.motechproject.ivr.web;

import org.motechproject.admin.service.StatusMessageService;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.ivr.repository.CallDetailRecordDataService;
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

import static org.motechproject.ivr.web.LogAndEventHelper.sendAndLogEvent;

/**
 * Responds to HTTP queries to {motech-server}/module/ivr/status/{configName} by creating a CallDetailRecord entry in
 * the database and posting a corresponding Motech event on the queue.
 */
@Controller
@RequestMapping(value = "/status")
public class StatusController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusController.class);
    public static final String XML_OK_RESPONSE = "<?xml version=\"1.0\"?><response>OK</response>";
    private CallDetailRecordDataService callDetailRecordDataService;
    private ConfigService configService;
    private StatusMessageService statusMessageService;
    private EventRelay eventRelay;

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

        sendAndLogEvent(configService, callDetailRecordDataService, statusMessageService, eventRelay, configName,
                null, params);

        return XML_OK_RESPONSE;
    }
}
