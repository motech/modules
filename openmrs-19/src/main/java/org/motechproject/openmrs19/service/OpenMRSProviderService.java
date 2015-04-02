package org.motechproject.openmrs19.service;

import org.motechproject.openmrs19.domain.OpenMRSProvider;

/**
 * Interface for creating and deleting providers
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
     * Fetches provider with given UUID.
     *
     * @param uuid  the UUID of the provider
     * @return  the provider with given UUID
     */
    OpenMRSProvider getByUuid(String uuid);

    /**
     * Deletes provider with the given UUID.
     *
     * @param uuid  the UUID of the provider
     */
    void deleteProvider(String uuid);
}
