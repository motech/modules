package org.motechproject.atomclient.service;


public interface AtomClientConfigService {

    /**
     * Load the atom feed configurations from the atom-client-feeds.json settings file (or DB, depending on how MOTECH
     * is setup)
     */
    void loadFeedConfigs();

    /**
     * Load the module's default properties (just the fetch job cron for now) from the atom-client-defaults.properties
     * file (or DB, depending on how MOTECH is setup)
     */
    void loadDefaultProperties();

    /**
     * Returns the set of atom feeds to fetch from
     * @return a FeedConfigs POJO with a getFeeds method that returns a Set of FeedConfig objects
     */
    FeedConfigs getFeedConfigs();

    /**
     * Returns the current fetch job cron expression
     * @return a string containing the cron expression
     */
    String getFetchCron();


    /**
     * Sets the module's feed configurations, this sets the in-memory feeds, and is not persisted
     *
     * @param feedConfigs
     */
    void setFeedConfigs(FeedConfigs feedConfigs);

    /**
     * Sets the module fetch job cron, ie: when the atom feeds will be fetched, but does not persist the setting
     *
     * @param fetchCron
     */
    void setFetchCron(String fetchCron);

    /**
     * Returns the content extraction regex for the given feed URL, if any, or returns an empty string.
     *
     * @param url the feed URL
     * @return a regex or an empty string
     */
    String getRegexForFeedUrl(String url);
}
