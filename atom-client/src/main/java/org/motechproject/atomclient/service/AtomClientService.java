package org.motechproject.atomclient.service;

public interface AtomClientService {

    void setupFetchJob(String urlString, String cronExpression);
    void fetch();
}
