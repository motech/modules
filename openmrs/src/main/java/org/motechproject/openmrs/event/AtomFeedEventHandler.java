package org.motechproject.openmrs.event;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.service.EventKeys;
import org.motechproject.openmrs.service.OpenMRSAtomFeedService;
import org.motechproject.openmrs.service.OpenMRSConfigService;
import org.motechproject.openmrs.tasks.constants.EventSubjects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles:
 * - (raw or text) config changes by triggering the reload of the appropriate configuration items
 * - the rescheduling of the fetch job
 * - fetch jobs
 */
@Component
public class AtomFeedEventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AtomFeedEventHandler.class);

    private final OpenMRSConfigService configService;
    private final OpenMRSAtomFeedService atomFeedService;

    @Autowired
    public AtomFeedEventHandler(OpenMRSAtomFeedService atomFeedService, OpenMRSConfigService configService) {
        this.atomFeedService = atomFeedService;
        this.configService = configService;
    }

    @MotechListener(subjects = { EventSubjects.FETCH_ENCOUNTER_MESSAGE })
    public void handleFetchEncounter(MotechEvent event) {
        LOGGER.trace("handleFetch {}", event);

        prepareConfig(event, EventKeys.ENCOUNTER_SCHEDULE_KEY, EventKeys.ATOM_FEED_ENCOUNTER_PAGE_ID);
        atomFeedService.fetch("");
    }

    @MotechListener(subjects = { EventSubjects.FETCH_PATIENT_MESSAGE })
    public void handleFetchPatient(MotechEvent event) {
        LOGGER.trace("handleFetch {}", event);

        prepareConfig(event, EventKeys.PATIENT_SCHEDULE_KEY, EventKeys.ATOM_FEED_PATIENT_PAGE_ID);
        atomFeedService.fetch("");
    }

    private void prepareConfig(MotechEvent event, String cronKey, String firstPageId) {
        Config config = configService.getConfigByName(event.getParameters().get("configName").toString());
        Map<String, String> atomFeeds = new HashMap<>();

        atomFeeds.put(cronKey, config.getFeedConfig().getAtomFeeds().get(cronKey));
        atomFeeds.put(firstPageId, config.getFeedConfig().getAtomFeeds().get(firstPageId));

        config.getFeedConfig().setAtomFeeds(atomFeeds);
        atomFeedService.setActualConfig(config);
    }
}
