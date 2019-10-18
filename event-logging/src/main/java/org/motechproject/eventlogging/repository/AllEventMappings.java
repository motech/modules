package org.motechproject.eventlogging.repository;

import com.google.gson.reflect.TypeToken;
import org.motechproject.commons.api.json.MotechJsonReader;
import org.motechproject.eventlogging.matchers.KeyValue;
import org.motechproject.eventlogging.matchers.LogMappings;
import org.motechproject.eventlogging.matchers.LoggableEvent;
import org.motechproject.eventlogging.matchers.MappedLoggableEvent;
import org.motechproject.eventlogging.matchers.MappingsJson;
import org.motechproject.config.SettingsFacade;
import org.motechproject.eventlogging.matchers.ParametersPresentEventFlag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    /**
     * Returns loggable events from event mappings stored in configuration file.
     * @return list of loggable events
     */
    public List<LoggableEvent> converToLoggableEvents() {
        List<MappingsJson> allMappings = getAllMappings();
        List<LoggableEvent> loggableEvents = new ArrayList<>();
        for (MappingsJson mapping : allMappings) {
            if (mapping.getMappings() == null && mapping.getIncludes() == null && mapping.getExcludes() == null) {
                LoggableEvent event = new LoggableEvent(mapping.getSubjects(), mapping.getFlags());
                loggableEvents.add(event);
            } else {
                MappedLoggableEvent mappedEvent = new MappedLoggableEvent(mapping.getSubjects(), null, null);

                List<KeyValue> mappings = null;

                if (mapping.getMappings() != null) {
                    List<Map<String, String>> mappingList = mapping.getMappings();
                    mappings = new ArrayList<>();

                    List<String> subjects = mapping.getSubjects();

                    if (subjects != null) {
                        for (Map<String, String> map : mappingList) {
                            KeyValue keyValue = KeyValue.buildFromMap(map);
                            mappings.add(keyValue);
                        }
                    }
                }

                List<String> inclusions = mapping.getIncludes();
                List<String> exclusions = mapping.getExcludes();

                LogMappings logMappings = new LogMappings(mappings, exclusions, inclusions);

                mappedEvent.setMappings(logMappings);

                List<ParametersPresentEventFlag> eventFlags = mapping.getFlags();

                if (eventFlags != null) {
                    mappedEvent.setFlags(eventFlags);
                }


                loggableEvents.add(mappedEvent);
            }
        }

        return loggableEvents;
    }

}
