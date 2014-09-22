package org.motechproject.ivr.repository;

import org.motechproject.ivr.domain.Template;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;

/**
 * MDS generated Template database queries
 */
public interface TemplateDataService extends MotechDataService<Template> {
    @Lookup
    Template findByName(@LookupField(name = "name") String name);
}
