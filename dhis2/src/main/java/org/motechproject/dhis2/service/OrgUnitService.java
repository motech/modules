package org.motechproject.dhis2.service;

import org.motechproject.dhis2.domain.OrgUnit;
import org.motechproject.dhis2.rest.domain.OrganisationUnitDto;

/**
 * Manages CRUD operations for a {@link org.motechproject.dhis2.domain.OrgUnit}
 */
public interface OrgUnitService extends GenericCrudService<OrgUnit> {
    OrgUnit findById(String id);
    OrgUnit findByName(String name);
    OrgUnit createFromDetails(OrganisationUnitDto details);
}
