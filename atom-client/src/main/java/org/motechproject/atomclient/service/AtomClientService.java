package org.motechproject.atomclient.service;

public interface AtomClientService {
    void rescheduleFetchJob(String cronExpression);
    void fetch();
}
