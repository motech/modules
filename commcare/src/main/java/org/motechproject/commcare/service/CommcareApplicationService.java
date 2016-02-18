package org.motechproject.commcare.service;

import org.motechproject.commcare.domain.CommcareApplicationJson;

import java.util.List;

/**
 * A service to CommcareApplicationService is responsible for retriving applications by configuration name
 */
public interface CommcareApplicationService {

    /**
     * Retrieves applications by configuration name
     *
     * @return the list of matching applications
     */
    List<CommcareApplicationJson> getByConfigName(String applicationName);
}
