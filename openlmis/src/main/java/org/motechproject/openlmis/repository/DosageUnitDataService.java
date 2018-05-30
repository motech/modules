package org.motechproject.openlmis.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.openlmis.domain.DosageUnit;


/**
 * MDS data service for {@link org.motechproject.openlmis.domain.DosageUnit}
 */
public interface DosageUnitDataService extends MotechDataService<DosageUnit> {

    @Lookup
    DosageUnit findByOpenlmisId(@LookupField(name = "openlmisid") Integer openlmisId);

    @Lookup
    DosageUnit findByCode(@LookupField(name = "code") String code);
    
}
