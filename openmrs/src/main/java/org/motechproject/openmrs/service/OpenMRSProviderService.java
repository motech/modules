package org.motechproject.openmrs.service;

import org.motechproject.openmrs.domain.Provider;

/**
 * Interface for handling providers on the OpenMRS server.
 */
public interface OpenMRSProviderService {

    /**
     * Creates the given provider on the OpenMRS server. Configuration with the given {@code configName} will be used
     * while performing this action.
     *
     * @param configName  the name of the configuration
     * @param provider  the provider to be created
     * @return  the created provider
     */
    Provider createProvider(String configName, Provider provider);

    /**
     * Returns the provider with the given {@code uuid}. Configuration with the given {@code configName} will be used
     * while performing this action.
     *
     * @param configName  the name of the configuration
     * @param uuid  the UUID of the provider
     * @return  the provider with the given UUID, null if the provider doesn't exist
     */
    Provider getProviderByUuid(String configName, String uuid);

    /**
     * Deletes the provider with the given {@code uuid}. If the provider with the given {@code uuid} doesn't exist an
     * error will be logged. Configuration with the given {@code configName} will be used while performing this action.
     *
     * @param configName  the name of the configuration
     * @param uuid  the UUID of the provider
     */
    void deleteProvider(String configName, String uuid);
}
