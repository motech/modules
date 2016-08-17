package org.motechproject.openmrs.tasks.builder;

import org.apache.commons.lang.StringUtils;

/**
 * Helper class for building display names for the OpenMRS task channel and data provider.
 */
public final class DisplayNameHelper {

    /**
     * Builds a display name in the form of "subject: name [config-name]". Example - Received Form: Birth [myConfig].
     * If the name is blank, this will be formatted as "name [config-name]".
     * @param subject the subject with which the display name should start
     * @param name the name of the element (form, case, etc.), if blank will be omitted
     * @param configName the name of the configuration
     * @return the display name
     */
    public static String buildDisplayName(String subject, String name, String configName) {
        String displayName;
        if (StringUtils.isBlank(name)) {
            displayName = String.format("%s [%s]", subject, configName);
        } else {
            displayName = String.format("%s: %s [%s: %s]", subject, name, configName);
        }
        return displayName;
    }

    /**
     * Builds a display name in the form of "subject [config-name]". Example - Location [myConfig].
     * @param subject the subject with which the display name should start
     * @param configName the name of the configuration
     * @return the display name
     */
    public static String buildDisplayName(String subject, String configName) {
        return buildDisplayName(subject, null, configName);
    }

    /**
     * Utility class, should not be initiated.
     */
    private DisplayNameHelper() {
    }
}
