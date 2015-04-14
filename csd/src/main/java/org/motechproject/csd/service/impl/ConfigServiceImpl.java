package org.motechproject.csd.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.motechproject.config.core.constants.ConfigurationConstants;
import org.motechproject.csd.constants.CSDEventKeys;
import org.motechproject.csd.domain.Config;
import org.motechproject.csd.scheduler.CSDScheduler;
import org.motechproject.csd.service.ConfigService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
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

@Service("configService")
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private CSDScheduler csdScheduler;

    private static final String CSD_CONFIG_FILE_NAME = "csd-configs.json";
    private static final String CSD_CONFIG_FILE_PATH = "/" + ConfigurationConstants.RAW_DIR + "/" +
            CSD_CONFIG_FILE_NAME;
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigServiceImpl.class);
    private SettingsFacade settingsFacade;
    private Map<String, Config> configs = new HashMap<>();

    private synchronized void loadConfigs() {
        List<Config> configList;
        try (InputStream is = settingsFacade.getRawConfig(CSD_CONFIG_FILE_NAME)) {
            String jsonText = IOUtils.toString(is);
            LOGGER.debug("Loading {}", CSD_CONFIG_FILE_NAME);
            Gson gson = new Gson();
            configList = gson.fromJson(jsonText, new TypeToken<List<Config>>() { } .getType());
        } catch (Exception e) {
            String message = String.format("There seems to be a problem with the json text in %s: %s", CSD_CONFIG_FILE_NAME,
                    e.getMessage());
            LOGGER.debug(message);
            throw new JsonIOException(message, e);
        }

        configs = new HashMap<>();
        for (Config config : configList) {
            configs.put(config.getXmlUrl(), config);
        }
    }

    @Autowired
    public ConfigServiceImpl(@Qualifier("csdSettings") SettingsFacade settingsFacade) {
        this.settingsFacade = settingsFacade;
        loadConfigs();
    }

    @MotechListener(subjects = { ConfigurationConstants.FILE_CHANGED_EVENT_SUBJECT })
    public void handleFileChanged(MotechEvent event) {
        String filePath = (String) event.getParameters().get(ConfigurationConstants.FILE_PATH);
        if (!StringUtils.isBlank(filePath) && filePath.endsWith(CSD_CONFIG_FILE_PATH)) {
            LOGGER.info("{} has changed, reloading configs.", CSD_CONFIG_FILE_NAME);
            loadConfigs();
        }
    }

    @Override
    public List<Config> getConfigs() {
        return new ArrayList<Config>(configs.values());
    }

    @Override
    public Config getConfig(String xmlUrl) {
        if (configs.containsKey(xmlUrl)) {
            return configs.get(xmlUrl);
        }
        throw new IllegalArgumentException(String.format("Unknown config: '%s'.", xmlUrl));
    }

    @Override
    public void updateConfigs(List<Config> configs) {
        csdScheduler.unscheduleXmlConsumerRepeatingJobs();
        Gson gson = new Gson();
        String jsonText = gson.toJson(configs);
        ByteArrayResource resource = new ByteArrayResource(jsonText.getBytes());
        settingsFacade.saveRawConfig(CSD_CONFIG_FILE_NAME, resource);
        loadConfigs();
        Integer i = 0;
        for (Config config : getConfigs()) {
            if (config.isSchedulerEnabled()) {
                schedule(config, i++);
            }
        }
    }

    private void schedule(Config config, Integer key) {
        String xmlUrl = config.getXmlUrl();
        Period period = new Period(0, 0, 0, config.getPeriodDays(), config.getPeriodHours(),
                config.getPeriodMinutes(), 0, 0);
        String startDate = config.getStartDate();

        Map<String, Object> eventParameters = new HashMap<>();
        eventParameters.put(CSDEventKeys.XML_URL, xmlUrl);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put(CSDEventKeys.EVENT_PARAMETERS, eventParameters);
        parameters.put(CSDEventKeys.PERIOD, period);
        if (startDate != null && !startDate.isEmpty()) {
            parameters.put(CSDEventKeys.START_DATE, DateTime.parse(startDate, DateTimeFormat.forPattern(Config.DATE_TIME_PICKER_FORMAT)));
        }

        csdScheduler.scheduleXmlConsumerRepeatingJob(parameters, key.toString());
    }
}
