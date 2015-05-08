package org.motechproject.csd.exception;

/**
 * The <code>ConfigNotFoundException</code> exception signals a situation when an CSD configuration
 * with a given name cannot be found.
 */
public class ConfigNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 2178022657807925281L;

    public ConfigNotFoundException(String message) {
        super(message);
    }

    public ConfigNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
