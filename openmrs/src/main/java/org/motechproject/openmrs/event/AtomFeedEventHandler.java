package org.motechproject.openmrs.event;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.service.OpenMRSAtomFeedService;
import org.motechproject.openmrs.service.OpenMRSConfigService;
import org.motechproject.openmrs.tasks.constants.EventSubjects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Handles:
 * - (raw or text) config changes by triggering the reload of the appropriate configuration items
 * - the rescheduling of the fetch job
 * - fetch jobs
 */
@Component
public class AtomFeedEventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AtomFeedEventHandler.class);
    private static final String CONFIG_NAME_KEY = "configName";

    private final OpenMRSConfigService configService;
    private final OpenMRSAtomFeedService atomFeedService;

    @Autowired
    public AtomFeedEventHandler(OpenMRSAtomFeedService atomFeedService, OpenMRSConfigService configService) {
        this.atomFeedService = atomFeedService;
        this.configService = configService;
    }

    @MotechListener(subjects = { EventSubjects.SCHEDULE_OR_UNSCHEDULE_FETCH_JOB })
    public void handleConfigChange(MotechEvent event) {
        LOGGER.trace("handleConfigChenge {}", event);
        Config config = configService.getConfigByName((String) event.getParameters().get(CONFIG_NAME_KEY));
        atomFeedService.shouldScheduleFetchJob(config);
    }

    @MotechListener(subjects = { EventSubjects.FETCH_ENCOUNTER_MESSAGE })
    public void handleFetchEncounter(MotechEvent event) {
        LOGGER.trace("handleFetchEncounter {}", event);
        atomFeedService.fetch(event.getParameters().get(CONFIG_NAME_KEY).toString());
    }

    @MotechListener(subjects = { EventSubjects.FETCH_PATIENT_MESSAGE })
    public void handleFetchPatient(MotechEvent event) {
        LOGGER.trace("handleFetchPatient {}", event);
        atomFeedService.fetch(event.getParameters().get(CONFIG_NAME_KEY).toString());
    }
}
