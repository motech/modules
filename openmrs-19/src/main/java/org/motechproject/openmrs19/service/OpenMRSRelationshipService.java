package org.motechproject.openmrs19.service;

import org.motechproject.openmrs19.domain.Relationship;

/**
 * Interface for handling relationships on the OpenMRS server.
 */
public interface OpenMRSRelationshipService {

    /**
     * Returns relationship of the type with the given {@code typeUuid} for the person with the given
     * {@code personUuid}.
     *
     * @param configName  the name of the configuration to be used while performing this action
     * @param typeUuid  the UUID of the type
     * @param personUuid  the UUID of the person
     * @return the matching relationship
     */
    Relationship getByTypeUuidAndPersonUuid(String configName, String typeUuid, String personUuid);

}
