package org.motechproject.atomclient.service;


public interface AtomClientConfigService {

    FeedConfigs getFeedConfigs();
    String getFetchCron();

    void setFeedConfigs(FeedConfigs feedConfigs);
    void setFetchCron(String fetchCron);
}
