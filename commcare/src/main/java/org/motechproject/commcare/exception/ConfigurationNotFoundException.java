package org.motechproject.commcare.exception;

/**
 * Thrown when there were problems with configuration e.g. unsupported configuration name, no default configuration
 * selected.
 */
public class ConfigurationNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 5997289153709926961L;

    public ConfigurationNotFoundException(String message) {
        super(message);
    }

}
