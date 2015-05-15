package org.motechproject.csd.mds;

import org.motechproject.csd.domain.Organization;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;

public interface OrganizationDataService extends MotechDataService<Organization> {

    @Lookup
    Organization findByEntityID(@LookupField(name = "entityID") String entityID);
}
