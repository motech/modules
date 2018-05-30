package org.motechproject.openlmis.service.impl;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.motechproject.config.SettingsFacade;
import org.motechproject.openlmis.service.Settings;
import org.motechproject.openlmis.service.SettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;


/**
 * Implementation of {@link org.motechproject.openlmis.service.SettingsService}
 */
@Service("openlmisSettingsService")
public class SettingsServiceImpl implements SettingsService {
    private static final String SETTINGS_FILE_NAME = "openlmis-settings.json";
    private static final Logger LOGGER = LoggerFactory.getLogger(SettingsServiceImpl.class);

    private SettingsFacade settingsFacade;
    private Settings settings;

    private synchronized void loadSettings() {
        try (InputStream is = settingsFacade.getRawConfig(SETTINGS_FILE_NAME)) {
            String jsonText = IOUtils.toString(is);
            LOGGER.debug("Loading OpenLMIS settings...");
            Gson gson = new Gson();
            settings = gson.fromJson(jsonText, Settings.class);
        } catch (Exception e) {
            String message = "There was an error loading json from the OpenLMIS settings.";
            LOGGER.debug(message);
            throw new JsonIOException(message, e);
        }
    }

    @Autowired
    public SettingsServiceImpl(@Qualifier("openlmisSettings") SettingsFacade settingsFacade) {
        this.settingsFacade = settingsFacade;
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
    }
}
