package org.motechproject.ivr.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.motechproject.config.core.constants.ConfigurationConstants;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.ivr.domain.Config;
import org.motechproject.ivr.service.ConfigService;
import org.motechproject.server.config.SettingsFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * See {@link org.motechproject.ivr.service.ConfigService}
 */
@Service("configService")
public class ConfigServiceImpl implements ConfigService {
    private static final String CONFIG_FILE_NAME = "ivr-configs.json";
    private static final String CONFIG_FILE_PATH = "/org.motechproject.motech-ivr-bundle/raw/" + CONFIG_FILE_NAME;
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigServiceImpl.class);
    private SettingsFacade settingsFacade;
    private Map<String, Config> configs = new HashMap<>();

    private synchronized void loadConfigs() {
        List<Config> configList = null;
        try (InputStream is = settingsFacade.getRawConfig(CONFIG_FILE_NAME)) {
            String jsonText = IOUtils.toString(is);
            LOGGER.debug("Loading {}", CONFIG_FILE_NAME);
            Gson gson = new Gson();
            configList = gson.fromJson(jsonText, new TypeToken<List<Config>>() { } .getType());
        } catch (Exception e) {
            String message = String.format("There seems to be a problem with the json text in %s: %s", CONFIG_FILE_NAME,
                    e.getMessage());
            LOGGER.debug(message);
            throw new JsonIOException(message, e);
        }

        configs = new HashMap<>();
        for (Config config : configList) {
            configs.put(config.getName(), config);
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

    public Config getConfig(String name) {
        if (configs.containsKey(name)) {
            return configs.get(name);
        }
        String message = String.format("Unknown config: '%s'.", name);
        LOGGER.error(message);
        throw new IllegalArgumentException(message);
    }

    public List<Config> allConfigs() {
        return new ArrayList<Config>(configs.values());
    }

    public boolean hasConfig(String name) {
        return configs.containsKey(name);
    }

    public void updateConfigs(List<Config> configs) {
        Gson gson = new Gson();
        String jsonText = gson.toJson(configs);
        ByteArrayResource resource = new ByteArrayResource(jsonText.getBytes());
        settingsFacade.saveRawConfig(CONFIG_FILE_NAME, resource);
        loadConfigs();
    }
}
