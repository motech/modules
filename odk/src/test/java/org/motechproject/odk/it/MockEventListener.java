package org.motechproject.odk.it;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventListener;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class MockEventListener implements EventListener {

    private String id;
    private List<MotechEvent> events;
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MockEventListener.class);

    public MockEventListener(String id) {
        this.id = id;
        events = new ArrayList<>();
    }

    @Override
    public void handle(MotechEvent motechEvent) {
        LOGGER.debug("Received Event with subject: " + motechEvent.getSubject());
        events.add(motechEvent);

    }

    public void clearEvents() {
        this.events = new ArrayList<>();
    }

    @Override
    public String getIdentifier() {
        return id;
    }

    public List<MotechEvent> getEvents() {
        return events;
    }
}
