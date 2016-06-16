package org.motechproject.openmrs.validation;

import org.apache.commons.lang.Validate;
import org.motechproject.openmrs.config.Config;

/**
 * Utility class for validating configs.
 */
public final class ConfigValidator {

    private static final String FIELD_CANNOT_BE_EMPTY = "%s cannot be null or empty";

    /**
     * Validates that the given configuration has all the required fields set.
     *
     * @param config  the config to be validated
     */
    public static void validateConfig(Config config) {
        Validate.notEmpty(config.getName(), String.format(FIELD_CANNOT_BE_EMPTY, "Name"));
        Validate.notEmpty(config.getOpenMrsVersion(), String.format(FIELD_CANNOT_BE_EMPTY, "OpenMRS Version"));
        Validate.notEmpty(config.getOpenMrsUrl(), String.format(FIELD_CANNOT_BE_EMPTY, "OpenMRS URL"));
        Validate.notEmpty(config.getUsername(), String.format(FIELD_CANNOT_BE_EMPTY, "Username"));
        Validate.notEmpty(config.getPassword(), String.format(FIELD_CANNOT_BE_EMPTY, "Password"));
        Validate.notEmpty(config.getMotechPatientIdentifierTypeName(), String.format(FIELD_CANNOT_BE_EMPTY, "Motech patient identifier type name"));
    }

    /**
     * Utility class, should not be initiated.
     */
    private ConfigValidator() {
    }
}
