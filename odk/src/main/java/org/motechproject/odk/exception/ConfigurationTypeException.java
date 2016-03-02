package org.motechproject.odk.exception;

/**
 * This exception is thrown when configuration type that is not listed in {@link org.motechproject.odk.domain.ConfigurationType}
 * is encountered.
 */
public class ConfigurationTypeException extends Exception {

    public ConfigurationTypeException(String message) {
        super(message);
    }
}
