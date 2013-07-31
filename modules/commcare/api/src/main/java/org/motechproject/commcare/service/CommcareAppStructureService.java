package org.motechproject.commcare.service;

import org.motechproject.commcare.domain.CommcareApplicationJson;

import java.util.List;

public interface CommcareAppStructureService {

    /**
     * Query CommCareHQ for structures of all applications.
     * @return A list of CommcareApplicationJson objects representing all applications found on the configured domain of CommCareHQ
     */
    List<CommcareApplicationJson> getAllApplications();
}
