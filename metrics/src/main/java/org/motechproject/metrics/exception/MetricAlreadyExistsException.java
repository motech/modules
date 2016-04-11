package org.motechproject.metrics.exception;

/**
 * A custom exception thrown when a metric of a different type with the same name is already registered.
 */
public class MetricAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = 8840982173312719630L;

    public MetricAlreadyExistsException(String message) {
        super(message);
    }

    public MetricAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
