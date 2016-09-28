package org.motechproject.dhis2.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import org.apache.commons.io.IOUtils;
import org.motechproject.dhis2.event.EventSubjects;
import org.motechproject.dhis2.service.Settings;
import org.motechproject.dhis2.service.SettingsService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.config.SettingsFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * Implementation of {@link org.motechproject.dhis2.service.SettingsService}
 */
@Service("dhisSettingsService")
public class SettingsServiceImpl implements SettingsService {
    private static final String SETTINGS_FILE_NAME = "dhis2-settings.json";
    private static final Logger LOGGER = LoggerFactory.getLogger(SettingsServiceImpl.class);

    private SettingsFacade settingsFacade;
    private EventRelay eventRelay;
    private Settings settings;

    @Autowired
    public SettingsServiceImpl(@Qualifier("dhisSettings") SettingsFacade settingsFacade, EventRelay eventRelay) {
        this.settingsFacade = settingsFacade;
        this.eventRelay = eventRelay;
        loadSettings();
    }

    @Override
    public Settings getSettings() {
        return settings;
    }

    @Override
    public void updateSettings(Settings settings) {
        Gson gson = new Gson();
        String jsonText = gson.toJson(settings);
        ByteArrayResource resource = new ByteArrayResource(jsonText.getBytes());
        settingsFacade.saveRawConfig(SETTINGS_FILE_NAME, resource);
        loadSettings();

        eventRelay.broadcastEventMessage(new MotechEvent(EventSubjects.DHIS_SETTINGS_UPDATED));
    }

    private synchronized void loadSettings() {
        try (InputStream is = settingsFacade.getRawConfig(SETTINGS_FILE_NAME)) {
            String jsonText = IOUtils.toString(is);
            LOGGER.debug("Loading DHIS2 settings...");
            Gson gson = new Gson();
            settings = gson.fromJson(jsonText, Settings.class);
            if (settings.getServerURI() != null) {
                settings.setServerURI(removeTrailingSlash());
            }
        } catch (Exception e) {
            String message = "There was an error loading json from the DHIS2 settings.";
            LOGGER.debug(message);
            throw new JsonIOException(message, e);
        }
    }

    private String removeTrailingSlash() throws URISyntaxException {
            URI uri = new URI(settings.getServerURI());
            uri = uri.normalize();
            String uriString = uri.toString();
            if (uriString.endsWith("/")) {
                uriString = uriString.substring(0, uriString.length() - 1);
            }
            return uriString;
    }
}
