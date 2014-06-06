package org.motechproject.commcare.web;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.motechproject.commcare.events.constants.EventSubjects.SCHEMA_CHANGE_EVENT;

@Controller
public class AppSchemaChangeController {
    private EventRelay eventRelay;

    @Autowired
    public AppSchemaChangeController(EventRelay eventRelay) {
        this.eventRelay = eventRelay;
    }

    @RequestMapping(value = "/appSchemaChange")
    @ResponseStatus(HttpStatus.OK)
    public void receiveSchemaChange() {
        eventRelay.sendEventMessage(new MotechEvent(SCHEMA_CHANGE_EVENT));
    }
}
