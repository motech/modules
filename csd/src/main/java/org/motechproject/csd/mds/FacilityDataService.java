package org.motechproject.csd.mds;

import org.motechproject.csd.domain.Facility;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;

public interface FacilityDataService extends MotechDataService<Facility> {

    @Lookup
    Facility findByEntityID(@LookupField(name = "entityID") String entityID);
}
