package org.motechproject.csd.mds;

import org.motechproject.csd.domain.Provider;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;

public interface ProviderDataService extends MotechDataService<Provider> {

    @Lookup
    Provider findByEntityID(@LookupField(name = "entityID") String entityID);
}
