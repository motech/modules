package org.motechproject.rapidpro.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import org.apache.commons.io.IOUtils;
import org.motechproject.config.SettingsFacade;
import org.motechproject.rapidpro.domain.Settings;
import org.motechproject.rapidpro.service.SettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * Implementation of {@link SettingsService}.
 */

@Component("rapidproSettingsService")
public class SettingsServiceImpl implements SettingsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SettingsService.class);
    private static final String SETTINGS = "settings.json";

    private Settings settings;
    private SettingsFacade settingsFacade;

    @Autowired
    public SettingsServiceImpl(SettingsFacade settingsFacade) {
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
        settingsFacade.saveRawConfig(SETTINGS, resource);
        loadSettings();
    }

    @Override
    public void deleteSettings() {
        updateSettings(new Settings());
    }

    private synchronized void loadSettings() {
        try (InputStream is = settingsFacade.getRawConfig(SETTINGS)) {
            String jsonText = IOUtils.toString(is);
            Gson gson = new Gson();
            settings = gson.fromJson(jsonText, Settings.class);
        } catch (Exception e) {
            LOGGER.debug(e.toString());
            throw new JsonIOException(e);
        }
    }
}
