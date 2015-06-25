package org.motechproject.openmrs19.service;

import org.motechproject.openmrs19.domain.OpenMRSProvider;

/**
 * Interface for handling providers on the OpenMRS server.
 */
public interface OpenMRSProviderService {

    /**
     * Creates the given provider on the OpenMRS server.
     *
     * @param provider  the provider to be created
     * @return  the created provider
     */
    OpenMRSProvider createProvider(OpenMRSProvider provider);

    /**
     * Returns the provider with the given {@code uuid}.
     *
     * @param uuid  the UUID of the provider
     * @return  the provider with the given UUID, null if the provider doesn't exist
     */
    OpenMRSProvider getProviderByUuid(String uuid);

    /**
     * Deletes the provider with the given {@code uuid}. If the provider with the given {@code uuid} doesn't exist an
     * error will be logged.
     *
     * @param uuid  the UUID of the provider
     */
    void deleteProvider(String uuid);
}
