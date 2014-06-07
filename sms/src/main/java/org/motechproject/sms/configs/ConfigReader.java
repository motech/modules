package org.motechproject.sms.configs;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import org.apache.commons.io.IOUtils;
import org.motechproject.server.config.SettingsFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;
import java.io.InputStream;

//todo: switch to the new config services.

/**
 * Reading & writing configurations from the sms-configs.json file
 */
public class ConfigReader {
    private static final String SMS_CONFIGS_FILE_NAME = "sms-configs.json";
    private SettingsFacade settingsFacade;
    private Logger logger = LoggerFactory.getLogger(ConfigReader.class);

    public ConfigReader(SettingsFacade settingsFacade) {
        this.settingsFacade = settingsFacade;
    }

    public Configs getConfigs() {
        Configs configs = null;
        InputStream is = settingsFacade.getRawConfig(SMS_CONFIGS_FILE_NAME);
        String errorMessage = null;
        if (is == null) {
            throw new JsonIOException(SMS_CONFIGS_FILE_NAME + " missing");
        }
        try {
            String jsonText = IOUtils.toString(is);
            Gson gson = new Gson();
            configs = gson.fromJson(jsonText, Configs.class);
        } catch (Exception e) {
            errorMessage = "Might you have a malformed " + SMS_CONFIGS_FILE_NAME + " file? " + e.toString();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                // Ignore IO exception, what are we going to do anyway?
                logger.error("IOException when closing config file {}: {}", SMS_CONFIGS_FILE_NAME, e);
            }
        }
        if (errorMessage != null) {
            throw new JsonIOException(errorMessage);
        }
        return configs;
    }

    public void setConfigs(Configs configs) {
        Gson gson = new Gson();
        String jsonText = gson.toJson(configs, Configs.class);
        ByteArrayResource resource = new ByteArrayResource(jsonText.getBytes());
        settingsFacade.saveRawConfig(SMS_CONFIGS_FILE_NAME, resource);
    }
}
