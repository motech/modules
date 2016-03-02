package org.motechproject.openlmis.repository;

import java.util.List;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.openlmis.domain.FacilityType;


/**
 * MDS data service for {@link org.motechproject.openlmis.domain.FacilityType}
 */
public interface FacilityTypeDataService extends MotechDataService<FacilityType> {

    @Lookup
    FacilityType findByOpenlmisId(@LookupField(name = "openlmisid") Integer openlmisId);

    @Lookup
    FacilityType findByCode(@LookupField(name = "code") String code);
    
    @Lookup
    List<FacilityType> findByName(@LookupField(name = "name") String name);
}
