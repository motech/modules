package org.motechproject.dhis2.repository;

import org.motechproject.dhis2.domain.DataElement;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;

/**
 * MDS data service for {@link org.motechproject.dhis2.domain.DataElement}
 */
public interface DataElementDataService extends MotechDataService<DataElement> {

    @Lookup
    DataElement findByUuid(@LookupField(name = "uuid") String uuid);

    @Lookup
    DataElement findByName(@LookupField(name = "name") String name);
}
