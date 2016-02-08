package org.motechproject.odk.exception;

/**
 * This exception is thrown by a {@link org.motechproject.odk.event.builder.EventBuilder} when an error
 * is encountered while building a {@link org.motechproject.event.MotechEvent}.
 */
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
