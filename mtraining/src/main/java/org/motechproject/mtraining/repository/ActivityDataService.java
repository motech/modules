package org.motechproject.mtraining.repository;

import org.motechproject.mtraining.domain.ActivityRecord;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.mtraining.domain.ActivityState;

import java.util.List;

/**
 * Interface for repository that persists simple records and allows CRUD.
 * MotechDataService base class will provide the implementation of this class as well
 * as methods for adding, deleting, saving and finding all instances.  In this class we
 * define and custom lookups we may need.
 */
public interface ActivityDataService extends MotechDataService<ActivityRecord> {
    @Lookup
    List<ActivityRecord> findRecordsForUser(@LookupField(name = "externalId") String externalId);

    @Lookup
    List<ActivityRecord> findCompletedRecordsForUser(@LookupField(name = "externalId") String externalId,
                                                     @LookupField(name = "state") ActivityState state);
}
