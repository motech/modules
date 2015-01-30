package org.motechproject.commcare.web;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.motechproject.commcare.events.constants.EventSubjects.SCHEMA_CHANGE_EVENT;

@Controller
public class AppSchemaChangeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppSchemaChangeController.class);
    private EventRelay eventRelay;

    @Autowired
    public AppSchemaChangeController(EventRelay eventRelay) {
        this.eventRelay = eventRelay;
    }

    @RequestMapping(value = "/appSchemaChange")
    @ResponseStatus(HttpStatus.OK)
    public void receiveSchemaChange() {
        LOGGER.trace("Received schema change request.");
        eventRelay.sendEventMessage(new MotechEvent(SCHEMA_CHANGE_EVENT));
    }
}
