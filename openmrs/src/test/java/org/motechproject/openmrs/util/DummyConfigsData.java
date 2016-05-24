package org.motechproject.openmrs.util;

import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.config.Configs;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.capitalize;

public class DummyConfigsData {


    public static Configs prepareConfigs() {
        Config defaultConfig = prepareConfig("default");

        List<Config> configList = new ArrayList<>();
        configList.add(defaultConfig);
        configList.add(prepareConfig("one"));
        configList.add(prepareConfig("two"));

        return new Configs(configList, defaultConfig.getName());
    }

    public static String getConfigNameForSuffix(String suffix) {
        return "Concept" + capitalize(suffix);
    }

    private static Config prepareConfig(String suffix) {
        Config config = new Config();

        config.setName(getConfigNameForSuffix(suffix));
        config.setUsername(suffix);
        config.setPassword(String.format("%s123", suffix));
        config.setMotechPatientIdentifierTypeName(String.format("motech_%s_id", suffix));
        config.setOpenMrsUrl(String.format("http://www.%s.url", suffix));
        config.setMotechPatientIdentifierTypeName(String.format("motech_%s_type", suffix));
        config.setPatientIdentifierTypeNames(getPatientIdentifierTypeNames(suffix));

        return config;
    }

    private static List<String> getPatientIdentifierTypeNames(String suffix) {
        List<String> names = new ArrayList<>();

        String parsedSuffix = capitalize(suffix);

        names.add(String.format("firstType%s", parsedSuffix));
        names.add(String.format("secondType%s", parsedSuffix));
        names.add(String.format("thirdType%s", parsedSuffix));

        return names;
    }
}
