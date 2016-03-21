package org.motechproject.metrics.exception;

/**
 * A custom exception thrown when a healthcheck fails.
 */
public class HealthCheckException extends RuntimeException {
    private static final long serialVersionUID = 7004836286809507316L;

    public HealthCheckException(String message) {
        super(message);
    }

    public HealthCheckException(String message, Throwable cause) {
        super(message, cause);
    }
}
