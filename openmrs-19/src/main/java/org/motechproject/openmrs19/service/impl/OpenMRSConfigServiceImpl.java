package org.motechproject.openmrs19.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import org.apache.commons.io.IOUtils;
import org.motechproject.config.SettingsFacade;
import org.motechproject.openmrs19.config.Config;
import org.motechproject.openmrs19.config.Configs;
import org.motechproject.openmrs19.service.OpenMRSConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.motechproject.openmrs19.validation.ConfigValidator.validateConfig;

@Service
public class OpenMRSConfigServiceImpl implements OpenMRSConfigService {

    private static final String OPEN_MRS_CONFIGS_FILE_NAME = "openmrs-configs.json";

    @Autowired
    private SettingsFacade settingsFacade;

    private Configs configs;

    @PostConstruct
    public void postConstruct() {
        loadConfigs();
    }

    @Override
    public void addConfig(Config config) {
        validateConfig(config);
        configs.add(config);
        updateConfigs();
    }

    @Override
    public void updateConfig(Config config) {
        validateConfig(config);
        configs.update(config);
        updateConfigs();
    }

    @Override
    public void deleteConfig(String name) {
        configs.delete(name);
        updateConfigs();
    }

    @Override
    public void markConfigAsDefault(String name) {
        configs.markAsDefault(name);
        updateConfigs();
    }

    @Override
    public List<Config> getConfigs() {
        return configs.getConfigs();
    }

    @Override
    public Config getConfigByName(String name) {
        return configs.getByName(name);
    }

    @Override
    public Config getDefaultConfig() {
        return configs.getDefault();
    }

    private synchronized void loadConfigs() {
        try (InputStream is = settingsFacade.getRawConfig(OPEN_MRS_CONFIGS_FILE_NAME)) {
            String jsonText = IOUtils.toString(is);
            Gson gson = new Gson();
            configs = gson.fromJson(jsonText, Configs.class);
        } catch (IOException e) {
            throw new JsonIOException("Unable to read " + OPEN_MRS_CONFIGS_FILE_NAME, e);
        } catch (RuntimeException e) {
            throw new JsonIOException("Malformed " + OPEN_MRS_CONFIGS_FILE_NAME + " file", e);
        }
    }

    private void updateConfigs() {
        Gson gson = new Gson();
        String jsonText = gson.toJson(configs, Configs.class);
        ByteArrayResource resource = new ByteArrayResource(jsonText.getBytes());
        settingsFacade.saveRawConfig(OPEN_MRS_CONFIGS_FILE_NAME, resource);
    }
}
