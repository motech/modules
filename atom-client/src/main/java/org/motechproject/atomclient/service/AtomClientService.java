package org.motechproject.atomclient.service;

/**
 * The main service for this module. Enables fetching of the atom feed(s) specified in the settings and also enables
 * changing the automatic fetch job schedule
 */
public interface AtomClientService {
    /**
     * Schedules the fetch job on the given cron expression, or unschedules the fetch job given an empty cron expression
     *
     * Note: the fetch job is automatically scheduled (from the current settings) when the module starts
     *
     * @param cronExpression a <a href="http://www.quartz-scheduler.org/documentation/quartz-1.x/tutorials/crontrigger">cron expression</a> that describes how often to fetch, or an empty string which disables
     *                       automatic fetching
     */
    void scheduleFetchJob(String cronExpression);

    /**
     * Fetches the atom feed(s) specified in atom-client-feeds.json setting file.
     *
     * Note: a org.motechproject.atomclient.feedchange MOTECH event will be sent for each feed entry content that
     * changed since the last time the feed was fetched.
     */
    void fetch();

    /**
     * Reads the atom feed(s) specified in task action.
     * @param currentUrl last url that is already fetched
     * @param lastUrl last known url that can be fetched
     */
    void read(String currentUrl, String lastUrl);
}
