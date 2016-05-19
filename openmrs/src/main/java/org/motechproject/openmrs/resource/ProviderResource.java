package org.motechproject.openmrs.resource;

import org.motechproject.openmrs.domain.Provider;
import org.motechproject.openmrs.config.Config;

/**
 * Interface for providers management.
 */
public interface ProviderResource {

    /**
     * Creates the given provider on the OpenMRS server. The given {@code config} will be used while performing this
     * action.
     *
     * @param config  the configuration to be used while performing this action
     * @param provider  the provider to be created
     * @return  the saved provider
     */
    Provider createProvider(Config config, Provider provider);

    /**
     * Gets provider by its UUID. The given {@code config} will be used while performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param uuid  the UUID of the provider
     * @return  the provider with the given UUID
     */
    Provider getByUuid(Config config, String uuid);

    /**
     * Deletes the provider with the given UUID from the OpenMRS server. The given {@code config} will be used while
     * performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param uuid  the UUID of the provider
     */
    void deleteProvider(Config config, String uuid);

}
