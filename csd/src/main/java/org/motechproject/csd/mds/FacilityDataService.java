package org.motechproject.csd.mds;

import org.motechproject.csd.domain.Facility;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;

import java.util.List;

/**
 * MDS generated CallDetailRecord database queries
 */
public interface FacilityDataService extends MotechDataService<Facility> {
    @Lookup
    Facility findByUUID(@LookupField(name = "uuid") String uuid);

    @Lookup
    List<Facility> findByPrimaryName(@LookupField(name = "primaryName") String primaryName);
}
