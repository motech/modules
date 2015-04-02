package org.motechproject.openmrs19.resource;

import org.motechproject.openmrs19.exception.HttpException;
import org.motechproject.openmrs19.resource.model.Provider;

/**
 * Interface for providers management.
 */
public interface ProviderResource {

    /**
     * Creates the given provider on the OpenMRS server.
     *
     * @param provider  the provider to be created
     * @return  the saved provider
     * @throws HttpException  when there were problems while creating provider
     */
    Provider createProvider(Provider provider) throws HttpException;

    /**
     * Gets provider by its UUID.
     *
     * @param uuid  the UUID of the provider
     * @return  the provider with the given UUID
     * @throws HttpException  when there were problems while fetching provider
     */
    Provider getByUuid(String uuid) throws HttpException;

    /**
     * Deletes the provider with the given UUID from the OpenMRS server.
     *
     * @param uuid  the UUID of the provider
     * @throws HttpException  when there were problems while deleting provider
     */
    void deleteProvider(String uuid) throws HttpException;

}
