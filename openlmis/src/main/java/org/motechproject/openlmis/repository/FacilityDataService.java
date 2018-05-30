package org.motechproject.openlmis.repository;

import java.util.List;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.openlmis.domain.Facility;


/**
 * MDS data service for {@link org.motechproject.openlmis.domain.Facility}
 */
public interface FacilityDataService extends MotechDataService<Facility> {

    @Lookup
    Facility findByOpenlmisId(@LookupField(name = "openlmisid") Integer openlmisId);

    @Lookup
    Facility findByCode(@LookupField(name = "code") String code);
    
    @Lookup
    List<Facility> findByName(@LookupField(name = "name") String name);
}
