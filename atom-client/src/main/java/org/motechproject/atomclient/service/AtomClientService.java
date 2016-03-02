package org.motechproject.atomclient.service;

public interface AtomClientService {
    /**
     * Schedules the fetch job on the given cron expression, or unschedules the fetch job given an empty cron expression
     *
     * Note: the fetch job is automatically scheduled (from the current settings) when the module starts
     *
     * @param cronExpression
     */
    void scheduleFetchJob(String cronExpression);

    /**
     * Fetches the atom feed(s) specified in atom-client-feeds.json setting file.
     *
     * Note: a org.motechproject.atomclient.feedchange MOTECH event will be sent for each feed entry content that
     * changed since the last time the feed was fetched.
     */
    void fetch();
}
