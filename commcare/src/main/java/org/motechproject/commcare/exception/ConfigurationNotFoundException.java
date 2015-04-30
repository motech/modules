package org.motechproject.commcare.exception;

/**
 * Thrown when there were problems with configuration e.g. unsupported configuration name, no default configuration
 * selected.
 */
public class ConfigurationNotFoundException extends RuntimeException {

    public ConfigurationNotFoundException(String message) {
        super(message);
    }

}
