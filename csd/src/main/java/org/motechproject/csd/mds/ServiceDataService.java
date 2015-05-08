package org.motechproject.csd.mds;

import org.motechproject.csd.domain.Service;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;

public interface ServiceDataService extends MotechDataService<Service> {

    @Lookup
    Service findByEntityID(@LookupField(name = "entityID") String entityID);
}
