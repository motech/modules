package org.motechproject.ipf.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.ipf.event.EventSubjects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class IPFActionEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(IPFActionEventHandler.class);

    @MotechListener(subjects =  {EventSubjects.TEMPLATE_ACTION})
    public void handleIpfTaskAction(MotechEvent event) {
        LOGGER.info("Event handled");
    }
}
