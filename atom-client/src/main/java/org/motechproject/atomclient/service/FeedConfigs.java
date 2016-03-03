package org.motechproject.atomclient.service;

import java.util.HashSet;
import java.util.Set;

/**
 * POJO representation of a set of {@link FeedConfig} used to store the list of atom feeds we want to fetch
 */
public class FeedConfigs {

    private Set<FeedConfig> feeds;

    public FeedConfigs() {
        this(new HashSet<>());
    }

    public FeedConfigs(Set<FeedConfig> feeds) {
        this.feeds = feeds;
    }

    public Set<FeedConfig> getFeeds() {
        return feeds;
    }

    public void setFeeds(Set<FeedConfig> feeds) {
        this.feeds = feeds;
    }

}
