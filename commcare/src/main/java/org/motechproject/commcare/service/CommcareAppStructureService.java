package org.motechproject.commcare.service;

import org.motechproject.commcare.domain.CommcareApplicationJson;

import java.util.List;

/**
 * A service to perform queries against CommCareHQ application structure APIs.
 */
public interface CommcareAppStructureService {

    /**
     * Query CommCareHQ for structures of all applications.
     *
     * @param configName  the name of the configuration used for connecting to CommcareHQ, null means default configuration
     * @return  the list of the CommcareApplicationJson objects representing all applications found on the configured
     *          domain of CommCareHQ
     */
    List<CommcareApplicationJson> getAllApplications(String configName);

    /**
     * Same as {@link #getAllApplications(String) getAllApplications} but uses default Commcare configuration.
     */
    List<CommcareApplicationJson> getAllApplications();
}
