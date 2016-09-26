package org.motechproject.openmrs.service;

import org.motechproject.openmrs.domain.Form;

public interface OpenMRSFormService {

    /**
     * Returns the form with the given {@code uuid}. Configuration with the given {@code configName} will be used
     * while performing this action.
     *
     * @param configName  the name of the configuration
     * @param uuid  the UUID of the visit
     * @return the form with the given UUID
     */
    Form getFormByUuid(String configName, String uuid);
}
