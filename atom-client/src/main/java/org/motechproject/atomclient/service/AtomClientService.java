package org.motechproject.atomclient.service;

public interface AtomClientService {
    void scheduleFetchJob(String cronExpression);
    void fetch();
}
