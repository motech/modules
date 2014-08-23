package org.motechproject.ivr.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.ivr.domain.Config;

/**
 * MDS generated ConfigDataService database queries
 */
public interface ConfigDataService extends MotechDataService<Config> {
    @Lookup
    Config findByName(@LookupField(name = "name") String name);
}
