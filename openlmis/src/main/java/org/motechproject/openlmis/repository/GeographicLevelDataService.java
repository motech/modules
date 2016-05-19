package org.motechproject.openlmis.repository;

import java.util.List;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.openlmis.domain.GeographicLevel;


/**
 * MDS data service for {@link org.motechproject.openlmis.domain.GeographicLevel}
 */
public interface GeographicLevelDataService extends MotechDataService<GeographicLevel> {

    @Lookup
    GeographicLevel findByOpenlmisId(@LookupField(name = "openlmisid") Integer openlmisId);

    @Lookup
    GeographicLevel findByCode(@LookupField(name = "code") String code);
    
    @Lookup
    List<GeographicLevel> findByName(@LookupField(name = "name") String name);
}
