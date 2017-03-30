package org.motechproject.openmrs.service;

import org.motechproject.openmrs.config.Config;

/**
 * Enables fetching of the atom feed(s) specified in the OpenMRS settings and also enables
 * changing the automatic fetch job schedule
 */
public interface OpenMRSAtomFeedService {
    /**
     * Schedules the fetch job on the given cron expression in the given config, or unschedules the fetch job given an empty cron expression
     *
     * Note: the fetch job is automatically scheduled (from the current settings) when the module starts
     *
     * @param config the OpenMRS config with atom feed configuration
     */
    void shouldScheduleFetchJob(Config config);

    /**
     * Fetches the atom feed(s) specified in OpenMRS configuration. Configuration with the given {@code configName} will be used while performing this action.
     *
     * Note: a org.motechproject.openmrs.feedchange.patient or org.motechproject.openmrs.feedchange.encounter MOTECH event will be sent for each feed entry content that
     * changed since the last time the feed was fetched.
     *
     * @param configName the Open MRS config name.
     *
     */
    void fetch(String configName);
}
