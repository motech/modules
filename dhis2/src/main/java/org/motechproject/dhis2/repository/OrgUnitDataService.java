package org.motechproject.dhis2.repository;

import org.motechproject.dhis2.domain.OrgUnit;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;


/**
 * MDS data service for {@link org.motechproject.dhis2.domain.OrgUnit}
 */
public interface OrgUnitDataService extends MotechDataService<OrgUnit> {
    @Lookup
    OrgUnit findByName(@LookupField(name = "name") String name);

    @Lookup
    OrgUnit findByUuid(@LookupField(name = "uuid") String uuid);
}
