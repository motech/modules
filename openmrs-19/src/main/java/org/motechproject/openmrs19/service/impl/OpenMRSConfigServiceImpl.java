package org.motechproject.openmrs19.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.motechproject.config.SettingsFacade;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.openmrs19.config.Config;
import org.motechproject.openmrs19.config.Configs;
import org.motechproject.openmrs19.exception.config.ConfigurationNotFoundException;
import org.motechproject.openmrs19.service.OpenMRSConfigService;
import org.motechproject.openmrs19.tasks.constants.EventSubjects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

import static org.motechproject.openmrs19.validation.ConfigValidator.validateConfig;

@Service("configService")
public class OpenMRSConfigServiceImpl implements OpenMRSConfigService {

    private static final String OPEN_MRS_CONFIGS_FILE_NAME = "openmrs-configs.json";
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenMRSConfigServiceImpl.class);

    @Autowired
    private SettingsFacade settingsFacade;

    @Autowired
    private EventRelay eventRelay;

    private Configs configs;

    @PostConstruct
    public void postConstruct() {
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

    @Override
    @Transactional
    public void deleteAllConfigs() {
        configs.getConfigs().clear();
        configs.setDefaultConfigName(null);
    }

    @Override
    @Transactional
    public void saveAllConfigs(Configs newConfigs) {
        configs.setDefaultConfigName(newConfigs.getDefaultConfigName());
        for (Config config : newConfigs.getConfigs()) {
            validateConfig(config);
            configs.add(config);
        }
        updateConfigs();
    }

    @Override
    @Transactional
    public void addConfig(Config config) {
        validateConfig(config);
        configs.add(config);
        updateConfigs();
    }

    @Override
    @Transactional
    public void updateConfig(Config config) {
        validateConfig(config);
        configs.update(config);
        updateConfigs();
    }

    @Override
    @Transactional
    public void deleteConfig(String name) {
        configs.delete(name);
        updateConfigs();
    }

    @Override
    @Transactional
    public void markConfigAsDefault(String name) {
        configs.markAsDefault(name);
        updateConfigs();
    }

    @Override
    @Transactional
    public Configs getConfigs() {
        return configs;
    }

    @Override
    @Transactional
    public Config getConfigByName(String name) {
        Config config = StringUtils.isEmpty(name) ?
                configs.getByName(configs.getDefaultConfigName()) : configs.getByName(name);

        if (config == null) {
            throw new ConfigurationNotFoundException(name);
        }

        return config;
    }

    @Override
    @Transactional
    public Config getDefaultConfig() {
        return configs.getByName(configs.getDefaultConfigName());
    }

    @Override
    public boolean verifyConfig(Config config) {
        HttpMethod method = new GetMethod(config.toInstancePath("/concept").toString());
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(config.getUsername(), config.getPassword());
        HttpClient client = new HttpClient();
        client.getState().setCredentials(AuthScope.ANY, credentials);
        int status = 0;
        try {
            status = client.executeMethod(method);
        } catch (HttpException e) {
            LOGGER.warn("HttpException while sending request to OpenMRS: " + e.getMessage());
        } catch (IOException e) {
            LOGGER.warn("IOException while sending request to OpenMRS: " + e.getMessage());
        } finally {
            method.releaseConnection();
        }

        return status == HttpStatus.SC_OK;
    }

    private void updateConfigs() {
        Gson gson = new Gson();
        String jsonText = gson.toJson(configs, Configs.class);
        ByteArrayResource resource = new ByteArrayResource(jsonText.getBytes());
        settingsFacade.saveRawConfig(OPEN_MRS_CONFIGS_FILE_NAME, resource);
        eventRelay.sendEventMessage(new MotechEvent(EventSubjects.CONFIG_CHANGE_EVENT));
    }
}
