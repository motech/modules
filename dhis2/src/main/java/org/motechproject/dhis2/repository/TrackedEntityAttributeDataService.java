package org.motechproject.dhis2.repository;

import org.motechproject.dhis2.domain.TrackedEntityAttribute;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;



/**
 * MDS data service for {@link org.motechproject.dhis2.domain.TrackedEntityAttribute}
 */
public interface TrackedEntityAttributeDataService extends MotechDataService<TrackedEntityAttribute> {
    @Lookup
    TrackedEntityAttribute findByUuid(@LookupField(name = "uuid") String id);
}
