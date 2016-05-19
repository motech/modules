package org.motechproject.openmrs.resource;

import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.RelationshipListResult;

/**
 * Interface for relationships management.
 */
public interface RelationshipResource {

    /**
     * Returns the {@link RelationshipListResult} of all relationships that the person with the given {@code personUuid}
     * is part of.
     *
     * @param config  the configuration to be used while performing this action
     * @param personUuid  the UUID of the person
     * @return the list of all relationships the person with the given {@code personUuid} is part of
     */
    RelationshipListResult getByPersonUuid(Config config, String personUuid);
}
