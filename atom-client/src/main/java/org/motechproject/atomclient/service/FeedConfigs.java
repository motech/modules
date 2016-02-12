package org.motechproject.atomclient.service;

import java.util.HashSet;
import java.util.Set;

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
