package org.motechproject.atomclient.service;

import org.apache.commons.lang.StringUtils;
import org.motechproject.config.core.constants.ConfigurationConstants;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Handles:
 * - (raw or text) config changes by triggering the reload of the appropriate configuration items
 * -
 */
@Component
public class AtomClientEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AtomClientEventHandler.class);
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
}
