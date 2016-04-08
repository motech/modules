package org.motechproject.ihe.interop.service;

import org.motechproject.ihe.interop.domain.CdaTemplate;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
/**
 * The data service for managing {@link CdaTemplate}
 * objects.
 */
public interface IHETemplateDataService extends MotechDataService<CdaTemplate> {

    /**
     * Retrieves template with the given name.
     *
     * @param templateName the name of template
     * @return template with the given name
     */
    @Lookup
    CdaTemplate findByName(@LookupField(name = "templateName") String templateName);
}
