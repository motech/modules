package org.motechproject.http.agent.components;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Asynchronous implementation of {@link org.motechproject.http.agent.components.CommunicationType}.
 * Uses the event system to trigger an asynchronous request.
 */
@Component
public class AsynchronousCall implements CommunicationType {

    private EventRelay eventRelay;

    @Autowired
    public AsynchronousCall(EventRelay eventRelay) {
        this.eventRelay = eventRelay;
    }

    @Override
    public void send(MotechEvent motechEvent) {
        eventRelay.sendEventMessage(motechEvent);
    }

    /**
     * @see org.motechproject.http.agent.components.CommunicationType
     * @return null
     */
    @Override
    public ResponseEntity<?> sendWithReturnType(MotechEvent motechEvent) {
        eventRelay.sendEventMessage(motechEvent);
        return null;
    }
}
