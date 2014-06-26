package org.motechproject.pillreminder.dao;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.pillreminder.domain.PillRegimen;

/**
 * The data service for managing {@link org.motechproject.pillreminder.domain.PillRegimen}
 * objects. The service implementation is generated and injected by Motech Data Services.
 */
public interface PillRegimenDataService extends MotechDataService<PillRegimen> {

    @Lookup(name = "Find by External ID")
    PillRegimen findByExternalId(@LookupField(name = "externalId") String externalId);

    @Lookup(name = "Find by Regimen ID")
    PillRegimen findById(@LookupField(name = "id") Long id);
}
