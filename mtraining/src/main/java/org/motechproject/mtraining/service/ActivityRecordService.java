package org.motechproject.mtraining.service;

import org.motechproject.mtraining.domain.EnrollmentRecord;

import java.util.List;

/**
 * Service interface for CRUD on simple repository records.
 */
public interface ActivityRecordService {

    void create(String name, String message);

    void add(EnrollmentRecord record);

    EnrollmentRecord findRecordByName(String recordName);

    List<EnrollmentRecord> getRecords();

    void delete(EnrollmentRecord record);

    void update(EnrollmentRecord record);
}
