package org.motechproject.atomclient.service;


public interface AtomClientConfigService {

    void loadFeedConfigs();
    void loadDefaultProperties();
    FeedConfigs getFeedConfigs();
    String getFetchCron();
    void setFeedConfigs(FeedConfigs feedConfigs);
    void setFetchCron(String fetchCron);
}
