package org.motechproject.atomclient.service.impl;

import org.apache.commons.lang.StringUtils;
import org.motechproject.atomclient.service.AtomClientConfigService;
import org.motechproject.atomclient.service.AtomClientService;
import org.motechproject.atomclient.service.Constants;
import org.motechproject.config.core.constants.ConfigurationConstants;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Handles:
 * - (raw or text) config changes by triggering the reload of the appropriate configuration items
 * - the rescheduling of the fetch job
 * - fetch jobs
 */
@Component
public class AtomClientEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AtomClientEventHandler.class);
    private static final String CURRENT_URL = "currentFeedUrl";
    private static final String LAST_URL = "lastFeedUrl";
    private AtomClientConfigService atomClientConfigService;
    private AtomClientService atomClientService;

    @Autowired
    public void setAtomClientConfigService(AtomClientConfigService atomClientConfigService) {
        this.atomClientConfigService = atomClientConfigService;
    }


    @Autowired
    public void setAtomClientService(AtomClientService atomClientService) {
        this.atomClientService = atomClientService;
    }


    @MotechListener(subjects = { ConfigurationConstants.FILE_CHANGED_EVENT_SUBJECT })
    public void handleFileChanged(MotechEvent event) {
        LOGGER.trace("handleFileChanged {}", event);

        String filePath = (String) event.getParameters().get(ConfigurationConstants.FILE_PATH);
        if (StringUtils.isBlank(filePath)) {
            return;
        }

        if (filePath.endsWith(Constants.PROPERTIES_FILE)) {
            LOGGER.info("{} has changed.", filePath);
            atomClientConfigService.loadDefaultProperties();
        }

        if (filePath.endsWith(Constants.RAW_CONFIG_FILE)) {
            LOGGER.info("{} has changed.", filePath);
            atomClientConfigService.loadFeedConfigs();
        }
    }

    @MotechListener(subjects = { Constants.RESCHEDULE_FETCH_JOB })
    public void handleRescheduleFetchJob(MotechEvent event) {
        LOGGER.trace("handleRescheduleFetchJob {}", event);

        atomClientService.scheduleFetchJob(atomClientConfigService.getFetchCron());
    }


    @MotechListener(subjects = { Constants.FETCH_MESSAGE })
    public void handleFetch(MotechEvent event) {
        LOGGER.trace("handleFetch {}", event);

        atomClientService.fetch();
    }

    @MotechListener(subjects = { Constants.READ_MESSAGE })
    public void handleRead(MotechEvent event) {
        LOGGER.trace("handleRead {}", event);
        Map<String, Object> params = event.getParameters();

        String currentUrl = (String) params.get(CURRENT_URL);
        String lastUrl = (String) params.get(LAST_URL);

        atomClientService.read(currentUrl, lastUrl);
    }
}
