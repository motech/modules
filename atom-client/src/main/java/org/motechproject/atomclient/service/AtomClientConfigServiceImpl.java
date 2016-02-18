package org.motechproject.atomclient.service;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.motechproject.atomclient.exception.AtomClientConfigurationException;
import org.motechproject.config.core.constants.ConfigurationConstants;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.server.config.SettingsFacade;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;


@Service("atomClientConfigService")
public class AtomClientConfigServiceImpl implements AtomClientConfigService {

    private static final String RAW_CONFIG_FILE = "atom-client-feeds.json";
    private static final String PROPERTIES_FILE = "atom-client-defaults.properties";
    private static final Logger LOGGER = LoggerFactory.getLogger(AtomClientConfigServiceImpl.class);
    private SettingsFacade settingsFacade;
    private EventRelay eventRelay;
    private String fetchCron;
    private FeedConfigs feedConfigs = new FeedConfigs();


    @Autowired
    public AtomClientConfigServiceImpl(@Qualifier("atomClientSettings") SettingsFacade settingsFacade, EventRelay eventRelay) {
        this.settingsFacade = settingsFacade;
        this.eventRelay = eventRelay;

        loadFeedConfigs();
        loadDefaultProperties();
    }


    private void loadFeedConfigs() {
        String json;
        try (InputStream is = settingsFacade.getRawConfig(RAW_CONFIG_FILE)) {
            json = IOUtils.toString(is);
        }
        catch (IOException e) {
            LOGGER.error("Error reading {}: {}", RAW_CONFIG_FILE, e.getMessage());
            return;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            feedConfigs = mapper.readValue(json, FeedConfigs.class);
            for (FeedConfig config : feedConfigs.getFeeds()) {
                LOGGER.info("Will fetch from {}", config.getUrl());
            }
        } catch (IOException e) {
            LOGGER.error("{} in {} with:\n\n{}\n\n", e.getMessage(), RAW_CONFIG_FILE, json);
        }
    }


    private void loadDefaultProperties() {
        changeCron(settingsFacade.getProperty(Constants.FETCH_CRON_PROPERTY));
    }


    private void changeCron(String cronString) {
        //It's OK to pass a blank cron expression, we'll just unschedule the job.
        if (StringUtils.isBlank(cronString)) {
            fetchCron = "";
            sendScheduleFetchJobMessage();
            return;
        }
        try {
            CronExpression.validateExpression(cronString);
        } catch (ParseException ex) {
            throw new AtomClientConfigurationException(String.format("Cron expression %s is invalid: %s",
                    cronString, ex.getMessage()), ex);
        }
        if (!StringUtils.equals(fetchCron, cronString)) {
            fetchCron = cronString;
            sendScheduleFetchJobMessage();
        }
    }


    private void sendScheduleFetchJobMessage() {
        LOGGER.debug("sendScheduleFetchJobMessage");
        eventRelay.sendEventMessage(new MotechEvent(Constants.ATOMCLIENT_SCHEDULE_FETCH_JOB));
    }


    @MotechListener(subjects = { ConfigurationConstants.FILE_CHANGED_EVENT_SUBJECT })
    public void handleFileChanged(MotechEvent event) {
        String filePath = (String) event.getParameters().get(ConfigurationConstants.FILE_PATH);
        if (StringUtils.isBlank(filePath)) {
            return;
        }

        if (filePath.endsWith(PROPERTIES_FILE)) {
            LOGGER.info("{} has changed.", filePath);
            loadDefaultProperties();
        }

        if (filePath.endsWith(RAW_CONFIG_FILE)) {
            LOGGER.info("{} has changed.", filePath);
            loadFeedConfigs();
        }
    }


    @Override
    public FeedConfigs getFeedConfigs() {
        return feedConfigs;
    }


    @Override
    public void setFeedConfigs(FeedConfigs feedConfigs) {
        this.feedConfigs = feedConfigs;
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
