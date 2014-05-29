package org.motechproject.mtraining.service;

import java.util.List;

import org.motechproject.mtraining.domain.EnrollmentRecord;

/**
 * Service interface for CRUD on simple repository records.
 */
public interface HelloWorldRecordService {

    void create(String name, String message);

    void add(EnrollmentRecord record);

    EnrollmentRecord findRecordByName(String recordName);

    List<EnrollmentRecord> getRecords();

    void delete(EnrollmentRecord record);

    void update(EnrollmentRecord record);
}
