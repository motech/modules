package org.motechproject.dhis2.service;

import org.motechproject.dhis2.domain.DataElement;
import org.motechproject.dhis2.rest.domain.DataElementDto;

/**
 * Manages CRUD operations for a {@link org.motechproject.dhis2.domain.DataElement}
 */
public interface DataElementService extends GenericCrudService<DataElement> {
    DataElement findById(String id);
    DataElement createFromDetails(DataElementDto details);
}
