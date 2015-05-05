package org.motechproject.dhis2.service;

import org.motechproject.dhis2.domain.TrackedEntity;
import org.motechproject.dhis2.rest.domain.TrackedEntityDto;

/**
 * Manages CRUD operations for a {@link org.motechproject.dhis2.domain.TrackedEntity}
 */
public interface TrackedEntityService extends GenericCrudService<TrackedEntity> {
    TrackedEntity findById(String id);
    TrackedEntity createFromDetails(TrackedEntityDto details);
}
