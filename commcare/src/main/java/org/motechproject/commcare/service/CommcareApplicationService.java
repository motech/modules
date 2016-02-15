package org.motechproject.commcare.service;

import org.motechproject.commcare.domain.CommcareApplicationJson;

import java.util.List;

/**
 * Created by user on 12.02.16.
 */
public interface CommcareApplicationService {

    /**
     * Retrieves all application names
     * @return List of all applications
     */
    List<CommcareApplicationJson> getByConfigName(String applicationName);
}
