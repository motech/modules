package org.motechproject.odk.service.impl;


import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import org.apache.commons.io.IOUtils;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.Settings;
import org.motechproject.odk.service.ConfigurationService;
import org.motechproject.server.config.SettingsFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;


@Service("odkConfigurationService")
public class ConfigurationServiceImpl implements ConfigurationService {

    private static final String CONFIG_FILE_NAME = "settings.json";
    private SettingsFacade settingsFacade;
    private Settings settings;

    @Autowired
    public ConfigurationServiceImpl(SettingsFacade settingsFacade) {
        this.settingsFacade = settingsFacade;
        loadSettings();
    }

    @Override
    public Configuration getConfigByName(String name) {

        for (Configuration configuration : settings.getConfigurations()) {
            if (configuration.getName().equals(name)) {
                return configuration;
            }
        }
        return null;
    }

    @Override
    public void addOrUpdateConfiguration(Configuration configuration) {
        Configuration exists = getConfigByName(configuration.getName());
        if (exists == null) {
            settings.getConfigurations().add(configuration);
        } else {
            settings.getConfigurations().remove(exists);
            settings.getConfigurations().add(configuration);
        }
        updateSettings();
    }

    @Override
    public void removeConfiguration(String configName) {
        Configuration configuration = getConfigByName(configName);
        if (configuration != null) {
            settings.getConfigurations().remove(configuration);
            updateSettings();
        }
    }

    @Override
    public List<Configuration> getAllConfigs() {
        return settings.getConfigurations();
    }

    private synchronized void loadSettings() {
        try (InputStream is = settingsFacade.getRawConfig(CONFIG_FILE_NAME)) {
            String jsonText = IOUtils.toString(is);
            Gson gson = new Gson();
            settings = gson.fromJson(jsonText, Settings.class);
            if (settings == null) {
                settings = new Settings();
            }
        } catch (Exception e) {
            throw new JsonIOException("Malformed " + CONFIG_FILE_NAME + " file.\n " + e.toString(), e);
        }
    }

    private void updateSettings() {
        Gson gson = new Gson();
        String jsonText = gson.toJson(settings, Settings.class);
        ByteArrayResource resource = new ByteArrayResource(jsonText.getBytes());
        settingsFacade.saveRawConfig(CONFIG_FILE_NAME, resource);
        loadSettings();
    }

}
