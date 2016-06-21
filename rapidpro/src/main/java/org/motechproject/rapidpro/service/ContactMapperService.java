package org.motechproject.rapidpro.service;

import org.motechproject.rapidpro.domain.ContactMapping;
import org.motechproject.rapidpro.exception.NoMappingException;

import java.util.UUID;


/**
 * Service interface for CRUD operations on {@link ContactMapping}
 */
public interface ContactMapperService {

    /**
     * Provides the Rapidpro UUID for the contact from the External ID.
     *
     * @param externalId The external ID mapping to a Rapidpro UUID.
     * @return The Contact UUID, if it exists.
     * @throws NoMappingException If no mapping from an External ID to a Rapidpro UUID exists.
     */
    UUID getRapidproUUIDFromExternalId(String externalId) throws NoMappingException;

    /**
     * Deletes the ContactMapper if it exists.
     *
     * @param externalId The external ID mapping to a Rapidpro UUID.
     * @throws NoMappingException If no mapping from an External ID to a Rapidpro UUID exists.
     */
    void delete(String externalId) throws NoMappingException;

    /**
     * Creates a new mapping from an external ID to a Rapidpro UUID.
     *
     * @param externalId   The external ID mapping to a Rapidpro UUID.
     * @param rapidproUUID The Rapidpro UUID.
     */
    void create(String externalId, UUID rapidproUUID);

    /**
     * Checks if a mapping exists from an external ID to a Rapidpro UUID.
     *
     * @param externalId The external ID mapping to a Rapidpro UUID.
     * @return True if a mapping exists, False otherwise.
     */
    boolean exists(String externalId);
}
