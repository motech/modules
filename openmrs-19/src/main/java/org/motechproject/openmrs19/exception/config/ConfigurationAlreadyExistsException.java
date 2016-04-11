package org.motechproject.openmrs19.exception.config;

/**
 * Thrown when configuration with the same name already exists.
 */
public class ConfigurationAlreadyExistsException extends ConfigurationException {

    private static final String MESSAGE = "Configuration with the name \"%s\" already exists";

    public ConfigurationAlreadyExistsException(String name) {
        super(MESSAGE, name);
    }

}
