package org.motechproject.dhis2.service;

import org.motechproject.dhis2.domain.TrackedEntityAttribute;
import org.motechproject.dhis2.rest.domain.TrackedEntityAttributeDto;

/**
 * Manages CRUD operations for a {@link org.motechproject.dhis2.domain.TrackedEntityAttribute}
 */
public interface TrackedEntityAttributeService extends GenericCrudService<TrackedEntityAttribute> {
    TrackedEntityAttribute findById(String id);
    TrackedEntityAttribute createFromDetails(TrackedEntityAttributeDto details);
}
