package org.motechproject.commcare.service;

import org.motechproject.commcare.domain.report.ReportsMetadataInfo;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;

import java.util.List;

/**
 * Data service for the {@link ReportsMetadataInfo} class. Provides methods for managing instances of said class.
 */
public interface ReportsMetadataDataService extends MotechDataService<ReportsMetadataInfo> {

    /**
     * Returns a list of the reports that originate from the configuration with the given
     * {@code configurationName}.
     *
     * @param configurationName  the name of the configuration
     * @return the list of matching reports
     */
    @Lookup(name = "By Source configuration")
    List<ReportsMetadataInfo> bySourceConfiguration(@LookupField(name = "configName") String configurationName);

}
