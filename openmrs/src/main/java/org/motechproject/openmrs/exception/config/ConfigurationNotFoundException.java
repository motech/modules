package org.motechproject.openmrs.exception.config;

/**
 * Thrown when configuration with the given name does not exist.
 */
public class ConfigurationNotFoundException extends ConfigurationException {

    private static final String MESSAGE = "Configuration with the name \"%s\" does not exist";

    public ConfigurationNotFoundException(String name) {
        super(MESSAGE, name);
    }

}
