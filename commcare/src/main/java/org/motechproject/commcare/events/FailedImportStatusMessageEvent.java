package org.motechproject.commcare.events;

import org.motechproject.event.MotechEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * A wrapper for the event that will trigger a status message signaling
 * a failure to receive a form or a case. The status messages will be displayed by the Admin
 * module in its UI (if the module is present).
 */
public class FailedImportStatusMessageEvent {

    /**
     * The message to display.
     */
    private final String message;

    /**
     * @param message the message to display
     */
    public FailedImportStatusMessageEvent(String message) {
        this.message = message;
    }

    /**
     * @return message the message to display
     */
    public String getMessage() {
        return message;
    }

    /**
     * Builds a {@link MotechEvent} that will trigger the status message
     * in the admin module.
     * @return the Motech event to send
     */
    public MotechEvent toMotechEvent() {
        Map<String, Object> params = new HashMap<>();

        params.put("message", message);
        params.put("level", "CRITICAL");
        params.put("moduleName", "commcare");

        return new MotechEvent("org.motechproject.message", params);
    }
}
