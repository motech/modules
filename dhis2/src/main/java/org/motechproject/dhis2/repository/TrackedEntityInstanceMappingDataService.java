package org.motechproject.dhis2.repository;

import org.motechproject.dhis2.domain.TrackedEntityInstanceMapping;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;

/**
 * MDS data service for {@link TrackedEntityInstanceMapping}
 */
public interface TrackedEntityInstanceMappingDataService extends MotechDataService<TrackedEntityInstanceMapping> {
    @Lookup
    TrackedEntityInstanceMapping findByExternalName(@LookupField(name = "externalName") String externalName);
}
