package org.motechproject.odk.event;

public class EventBuilderException extends Exception {

    public EventBuilderException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventBuilderException(Throwable cause) {
        super(cause);
    }

    public EventBuilderException(String message) {
        super(message);
    }
}
