package org.motechproject.eventlogging.repository;

import com.google.gson.reflect.TypeToken;
import org.motechproject.commons.api.json.MotechJsonReader;
import org.motechproject.eventlogging.matchers.MappingsJson;
import org.motechproject.config.SettingsFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Class used for retrieving event mappings from the json configuration file.
 */
@Component
public class AllEventMappings {

    public static final String MAPPING_FILE_NAME = "event-mappings.json";

    private MotechJsonReader motechJsonReader;

    private SettingsFacade settings;

    /**
     * Creates an instance of AllEventMappings using autowired settings facade
     * and new MotechJsonReader instance.
     *
     * @param settings autowired SettingsFacade settings
     */
    @Autowired
    public AllEventMappings(@Qualifier("eventLoggingSettings") SettingsFacade settings) {
        this.settings = settings;
        this.motechJsonReader = new MotechJsonReader();
    }

    /**
     * Returns all event mappings stored in configuration file specified in MAPPING_FILE_NAME filed
     * in form of {@link org.motechproject.eventlogging.matchers.MappingsJson} list.
     *
     * @return list of MappingJson objects containing event mapping configurations
     */
    public List<MappingsJson> getAllMappings() {
        Type type = new TypeToken<List<MappingsJson>>() {
        } .getType();

        InputStream is = settings.getRawConfig(MAPPING_FILE_NAME);

        List<MappingsJson> mappings = (List<MappingsJson>) motechJsonReader.readFromStream(is, type);

        return mappings;
    }
}
