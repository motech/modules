package org.motechproject.ipf.service;

import org.motechproject.ipf.domain.IPFTemplate;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
/**
 * The data service for managing {@link org.motechproject.ipf.domain.IPFTemplate}
 * objects.
 */
public interface IPFTemplateDataService  extends MotechDataService<IPFTemplate> {

    /**
     * Retrieves template with the given name.
     *
     * @param templateName the name of template
     * @return template with the given name
     */
    @Lookup
    IPFTemplate findByName(@LookupField(name = "templateName") String templateName);
}
