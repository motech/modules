package org.motechproject.mtraining.repository;

import org.motechproject.mtraining.domain.EnrollmentRecord;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;

/**
 * Interface for repository that persists simple records and allows CRUD.
 * MotechDataService base class will provide the implementation of this class as well
 * as methods for adding, deleting, saving and finding all instances.  In this class we
 * define and custom lookups we may need.
 */
public interface HelloWorldRecordsDataService extends MotechDataService<EnrollmentRecord> {
    @Lookup
    EnrollmentRecord findRecordByName(@LookupField(name = "name") String recordName);
}
