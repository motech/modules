package org.motechproject.atomclient.service;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.motechproject.atomclient.exception.AtomClientConfigurationException;
import org.motechproject.server.config.SettingsFacade;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;


@Service("atomClientConfigService")
public class AtomClientConfigServiceImpl implements AtomClientConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AtomClientConfigServiceImpl.class);
    private SettingsFacade settingsFacade;
    private String fetchCron;
    private FeedConfigs feedConfigs = new FeedConfigs();


    @Autowired
    public void setSettingsFacade(@Qualifier("atomClientSettings") SettingsFacade settingsFacade) {
        this.settingsFacade = settingsFacade;

    }


    @PostConstruct
    public void loadFeedConfigsAndProperties() {
        loadFeedConfigs();
        loadDefaultProperties();
    }


    @Override
    public void loadFeedConfigs() {
        String json;
        try (InputStream is = settingsFacade.getRawConfig(Constants.RAW_CONFIG_FILE)) {
            json = IOUtils.toString(is);
        }
        catch (IOException e) {
            LOGGER.error("Error reading {}: {}", Constants.RAW_CONFIG_FILE, e.getMessage());
            return;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            setFeedConfigs(mapper.readValue(json, FeedConfigs.class));
        } catch (IOException e) {
            LOGGER.error("{} in {} with:\n\n{}\n\n", e.getMessage(), Constants.RAW_CONFIG_FILE, json);
        }
    }


    @Override
    public void loadDefaultProperties() {
        changeCron(settingsFacade.getProperty(Constants.FETCH_CRON_PROPERTY));
    }


    private void changeCron(String cronString) {
        if (StringUtils.isBlank(cronString)) {
            fetchCron = "";
            return;
        }

        if (StringUtils.equals(fetchCron, cronString)) {
            return;
        }

        try {
            CronExpression.validateExpression(cronString);
        } catch (ParseException ex) {
            throw new AtomClientConfigurationException(String.format("Cron expression %s is invalid: %s",
                    cronString, ex.getMessage()), ex);
        }

        fetchCron = cronString;
    }


    @Override
    public FeedConfigs getFeedConfigs() {
        return feedConfigs;
    }


    @Override
    public void setFeedConfigs(FeedConfigs feedConfigs) {
        this.feedConfigs = feedConfigs;
        for (FeedConfig config : feedConfigs.getFeeds()) {
            LOGGER.info("Will fetch from {}", config.getUrl());
        }
    }


    @Override
    public String getFetchCron() {
        return fetchCron;
    }


    @Override
    public void setFetchCron(String cronString) {
        changeCron(cronString);
    }
}
