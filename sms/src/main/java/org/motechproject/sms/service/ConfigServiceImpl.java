package org.motechproject.sms.service;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.motechproject.config.core.constants.ConfigurationConstants;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.server.config.SettingsFacade;
import org.motechproject.sms.configs.Config;
import org.motechproject.sms.configs.Configs;
import org.motechproject.sms.event.constants.EventSubjects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

/**
 * See {@link org.motechproject.sms.service.ConfigService}
 */
@Service("configService")
public class ConfigServiceImpl implements ConfigService {

    private static final String SMS_CONFIGS_FILE_NAME = "sms-configs.json";
    private static final String SMS_CONFIGS_FILE_PATH = "/" + ConfigurationConstants.RAW_DIR + "/" +
        SMS_CONFIGS_FILE_NAME;
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigServiceImpl.class);
    private SettingsFacade settingsFacade;
    private Configs configs;
    private EventRelay eventRelay;

    private synchronized void loadConfigs() {
        try (InputStream is = settingsFacade.getRawConfig(SMS_CONFIGS_FILE_NAME)) {
            String jsonText = IOUtils.toString(is);
            Gson gson = new Gson();
            configs = gson.fromJson(jsonText, Configs.class);
        }
        catch (Exception e) {
            throw new JsonIOException("Malformed " + SMS_CONFIGS_FILE_NAME + " file? " + e.toString(), e);
        }
    }

    @Autowired
    public ConfigServiceImpl(@Qualifier("smsSettings") SettingsFacade settingsFacade, EventRelay eventRelay) {
        this.settingsFacade = settingsFacade;
        this.eventRelay = eventRelay;
        loadConfigs();
    }

    @MotechListener(subjects = { ConfigurationConstants.FILE_CHANGED_EVENT_SUBJECT })
    public void handleFileChanged(MotechEvent event) {
        String filePath = (String) event.getParameters().get(ConfigurationConstants.FILE_PATH);
        if (!StringUtils.isBlank(filePath) && filePath.endsWith(SMS_CONFIGS_FILE_PATH)) {
            LOGGER.info("{} has changed, reloading configs.", SMS_CONFIGS_FILE_NAME);
            loadConfigs();
            eventRelay.sendEventMessage(new MotechEvent(EventSubjects.CONFIGS_CHANGED));
        }
    }


    public Config getDefaultConfig() {
        return configs.getDefaultConfig();
    }

    public List<Config> getConfigList() {
        return configs.getConfigs();
    }

    public Configs getConfigs() {
        return configs;
    }

    public boolean hasConfig(String name) {
        return configs.hasConfig(name);
    }

    public Config getConfig(String name) {
        return configs.getConfig(name);
    }

    public Config getConfigOrDefault(String name) {
        return configs.getConfigOrDefault(name);
    }

    public void updateConfigs(Configs configs) {
        Gson gson = new Gson();
        String jsonText = gson.toJson(configs, Configs.class);
        ByteArrayResource resource = new ByteArrayResource(jsonText.getBytes());
        settingsFacade.saveRawConfig(SMS_CONFIGS_FILE_NAME, resource);
        loadConfigs();
        eventRelay.sendEventMessage(new MotechEvent(EventSubjects.CONFIGS_CHANGED));
    }

    public boolean hasConfigs() {
        return !configs.isEmpty();
    }

    @Override
    public String getServerUrl() {
        return settingsFacade.getPlatformSettings().getServerUrl();
    }
}
