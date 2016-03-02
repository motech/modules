package org.motechproject.openlmis.repository;

import java.util.List;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.openlmis.domain.GeographicZone;


/**
 * MDS data service for {@link org.motechproject.openlmis.domain.GeographicZone}
 */
public interface GeographicZoneDataService extends MotechDataService<GeographicZone> {

    @Lookup
    GeographicZone findByOpenlmisId(@LookupField(name = "openlmisid") Integer openlmisId);

    @Lookup
    GeographicZone findByCode(@LookupField(name = "code") String code);
    
    @Lookup
    List<GeographicZone> findByName(@LookupField(name = "name") String name);
}
