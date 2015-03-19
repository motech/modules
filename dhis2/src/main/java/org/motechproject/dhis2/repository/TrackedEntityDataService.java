package org.motechproject.dhis2.repository;

import org.motechproject.dhis2.domain.TrackedEntity;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;


/**
 * MDS data service for {@link org.motechproject.dhis2.domain.TrackedEntity}
 */
public interface TrackedEntityDataService extends MotechDataService<TrackedEntity> {


    @Lookup
    TrackedEntity findByUuid(@LookupField(name = "uuid") String uuid);
}
