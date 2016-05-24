package org.motechproject.openmrs.service;

import org.motechproject.openmrs.domain.Relationship;

import java.util.List;

/**
 * Interface for handling relationships on the OpenMRS server.
 */
public interface OpenMRSRelationshipService {

    /**
     * Returns a list of relationships of the type with the given {@code typeUuid} for the person with the given
     * {@code personUuid}.
     *
     * @param configName  the name of the configuration to be used while performing this action
     * @param typeUuid  the UUID of the type
     * @param personUuid  the UUID of the person
     * @return the list o matching relationships
     */
    List<Relationship> getByTypeUuidAndPersonUuid(String configName, String typeUuid, String personUuid);

}
