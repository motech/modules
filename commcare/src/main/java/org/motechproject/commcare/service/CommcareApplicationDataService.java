package org.motechproject.commcare.service;

import org.motechproject.commcare.domain.CommcareApplicationJson;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;

import java.util.List;

public interface CommcareApplicationDataService extends MotechDataService<CommcareApplicationJson> {

    @Lookup(name = "By Application Name")
    CommcareApplicationJson byApplicationName(@LookupField(name = "applicationName") String applicationName);

    @Lookup(name = "By Source configuration")
    List<CommcareApplicationJson> bySourceConfiguration(@LookupField(name = "configName") String configurationName);
}
