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

    /**
     * Finds a pill regimen by external ID.
     * @param externalId the external ID of the pill regimen
     * @return the matching pill regimen
     */
    @Lookup(name = "Find by External ID")
    PillRegimen findByExternalId(@LookupField(name = "externalId") String externalId);
}
