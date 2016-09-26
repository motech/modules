package org.motechproject.openmrs.resource;


import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.Form;

/**
 * Interface for forms management.
 */
public interface FormResource {

    /**
     * Gets the form by its UUID. The given {@code config} will be used while performing this action.
     *
     * @param config the name of the configuration
     * @param uuid   the UUID of the visit
     * @return the form with the given UUID
     */
    Form getFormByUuid(Config config, String uuid);
}
