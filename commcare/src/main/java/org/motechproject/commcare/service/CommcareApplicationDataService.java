package org.motechproject.commcare.service;

import org.motechproject.commcare.domain.CommcareApplicationJson;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;

import java.util.List;

/**
 * Data service for the {@link CommcareApplicationJson} class. Provides methods for managing instances of said class.
 */
public interface CommcareApplicationDataService extends MotechDataService<CommcareApplicationJson> {

    /**
     * Returns application with the given {@code applicationName}.
     *
     * @param applicationName  the name of the application
     * @return the matching application, null if application with the given {@code applicationName} does not exist
     */
    @Lookup(name = "By Application Name")
    CommcareApplicationJson byApplicationName(@LookupField(name = "applicationName") String applicationName);

    /**
     * Returns a list of the applications that originate from the configuration with the given
     * {@code configurationName}.
     *
     * @param configurationName  the name of the configuration
     * @return the list of matching applications
     */
    @Lookup(name = "By Source configuration")
    List<CommcareApplicationJson> bySourceConfiguration(@LookupField(name = "configName") String configurationName);
}
