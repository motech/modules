package org.motechproject.dhis2.service;

import org.motechproject.dhis2.domain.TrackedEntityInstanceMapping;

/**
 * Manages CRUD operations for a {@link TrackedEntityInstanceMapping}
 */
public interface TrackedEntityInstanceMappingService extends GenericCrudService<TrackedEntityInstanceMapping> {
    TrackedEntityInstanceMapping findByExternalId(String externalId);
    TrackedEntityInstanceMapping create(String externalId, String dhisId);
    String mapFromExternalId(String externalId);
}
