package org.motechproject.commcare.service;

import org.motechproject.commcare.domain.CommcareApplicationJson;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;

public interface CommcareApplicationDataService extends MotechDataService<CommcareApplicationJson> {

    @Lookup(name = "By Application Name")
    CommcareApplicationJson byApplicationName(@LookupField(name = "applicationName") String applicationName);
}
