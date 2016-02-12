package org.motechproject.ipf.service;

import org.motechproject.ipf.domain.IPFTemplate;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;

public interface IPFTemplateDataService  extends MotechDataService<IPFTemplate> {

    @Lookup
    IPFTemplate findByName(@LookupField(name = "templateName") String templateName);
}
