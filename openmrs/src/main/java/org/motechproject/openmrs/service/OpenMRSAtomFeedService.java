package org.motechproject.openmrs.service;

import org.motechproject.openmrs.config.Config;

import java.util.Map;

/**
 * Enables fetching of the atom feed(s) specified in the OpenMRS settings and also enables
 * changing the automatic fetch job schedule
 */
public interface OpenMRSAtomFeedService {
    /**
     * Schedules the fetch job on the given cron expression, or unschedules the fetch job given an empty cron expression
     *
     * Note: the fetch job is automatically scheduled (from the current settings) when the module starts
     *
     * @param feeds a <a href="http://www.quartz-scheduler.org/documentation/quartz-1.x/tutorials/crontrigger">Cron expression</a>
     *              that describes how often to fetch, or an empty string which disables automatic fetching patient and encounter OpenMRS atom feeds.
     */
    void shouldScheduleFetchJob(Map<String, String> feeds);

    /**
     * Fetches the atom feed(s) specified in OpenMRS configuration. Configuration with the given {@code configName} will be used while performing this action.
     *
     * Note: a org.motechproject.openmrs.feedchange.patient or org.motechproject.openmrs.feedchange.encounter MOTECH event will be sent for each feed entry content that
     * changed since the last time the feed was fetched.
     *
     * @param configName the Open MRS config name.
     */
    void fetch(String configName);

    /**
     * Sets OpenMRS configuration which will be used while fetch action performing.
     *
     * @param config
     */
    void setActualConfig(Config config);

}
