package org.motechproject.openmrs.config;

import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.config.FeedConfig;

import java.util.ArrayList;

/**
 * Utility class for providing dummy data for configuration service tests.
 */
public class ConfigDummyData {

    /**
     * Prepares dummy config using the given suffix.
     *
     * @param suffix  the suffix to be used during config creation
     * @return the created config
     */
    public static Config prepareConfig(String suffix) {
        Config config = new Config();
        config.setName(getName(suffix));
        config.setOpenMrsVersion("openMrsVersion-" + suffix);
        config.setOpenMrsUrl("openMrsUrl-" + suffix);
        config.setUsername("username-" + suffix);
        config.setPassword("password-" + suffix);
        config.setMotechPatientIdentifierTypeName("motech_" + suffix + "_type");
        config.setPatientIdentifierTypeNames(new ArrayList<>());
        config.setFeedConfig(new FeedConfig());
        return config;
    }

    /**
     * Returns configuration name generated using the given suffix.
     *
     * @param suffix  the configuration suffix
     * @return the generated name
     */
    public static String getName(String suffix) {
        return "name-" + suffix;
    }

    /**
     * Utility class, should not be initiated.
     */
    private ConfigDummyData() {
    }
}
