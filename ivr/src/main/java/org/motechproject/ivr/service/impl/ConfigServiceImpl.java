package org.motechproject.ivr.service.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.motechproject.config.core.constants.ConfigurationConstants;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.ivr.domain.Config;
import org.motechproject.ivr.exception.ConfigNotFoundException;
import org.motechproject.ivr.domain.Configs;
import org.motechproject.ivr.service.ConfigService;
import org.motechproject.config.SettingsFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

/**
 * See {@link org.motechproject.ivr.service.ConfigService}.
 * Uses the {@link org.motechproject.config.SettingsFacade} for storing IVR configurations
 * inside a json file, since we can have multiple configurations.
 */
@Service("configService")
public class ConfigServiceImpl implements ConfigService {
    private static final String CONFIG_FILE_NAME = "ivr-configs.json";
    private static final String CONFIG_FILE_PATH = "/org.motechproject.ivr/raw/" + CONFIG_FILE_NAME;
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigServiceImpl.class);
    private SettingsFacade settingsFacade;
    private Configs configs;
    private Map<String, Config> configMap = new HashMap<>();

    private synchronized void loadConfigs() {
        try (InputStream is = settingsFacade.getRawConfig(CONFIG_FILE_NAME)) {
            String jsonText = IOUtils.toString(is);
            LOGGER.debug("Loading {}", CONFIG_FILE_NAME);
            Gson gson = new Gson();
            configs = gson.fromJson(jsonText, Configs.class);
        } catch (Exception e) {
            String message = String.format("There seems to be a problem with the json text in %s: %s", CONFIG_FILE_NAME,
                    e.getMessage());
            LOGGER.debug(message);
            throw new JsonIOException(message, e);
        }
        configMap = new HashMap<>();
        for (Config config : configs.getConfigList()) {
            configMap.put(config.getName(), config);
        }
    }

    @Autowired
    public ConfigServiceImpl(@Qualifier("ivrSettings") SettingsFacade settingsFacade) {
        this.settingsFacade = settingsFacade;
        loadConfigs();
    }

    @MotechListener(subjects = { ConfigurationConstants.FILE_CHANGED_EVENT_SUBJECT })
    public void handleFileChanged(MotechEvent event) {
        String filePath = (String) event.getParameters().get(ConfigurationConstants.FILE_PATH);
        if (!StringUtils.isBlank(filePath) && filePath.endsWith(CONFIG_FILE_PATH)) {
            LOGGER.info("{} has changed, reloading configs.", CONFIG_FILE_NAME);
            loadConfigs();
        }
    }

    @Override
    public Config getConfig(String name) {
        if (configMap.containsKey(name)) {
            return configMap.get(name);
        }
        throw new ConfigNotFoundException(String.format("Unknown config: '%s'.", name));
    }

    @Override
    public Configs allConfigs() {
        return configs;
    }

    @Override
    public boolean hasConfig(String name) {
        return configMap.containsKey(name);
    }

    @Override
    public void updateConfigs(Configs configs) {
        Gson gson = new Gson();
        String jsonText = gson.toJson(configs, Configs.class);
        ByteArrayResource resource = new ByteArrayResource(jsonText.getBytes());
        settingsFacade.saveRawConfig(CONFIG_FILE_NAME, resource);
        loadConfigs();
    }

    @Override
    public String getDefaultConfig() {
        return configs.getDefaultConfig();
    }

}
