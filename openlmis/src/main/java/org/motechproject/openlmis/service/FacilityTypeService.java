package org.motechproject.openlmis.service;

import java.util.List;

import org.motechproject.openlmis.domain.FacilityType;
import org.motechproject.openlmis.rest.domain.FacilityTypeDto;

/**
 * Manages CRUD operations for a {@link org.motechproject.ProgramDto.domain.FacilityType}
 */
public interface FacilityTypeService extends GenericCrudService<FacilityType> {
    FacilityType findByCode(String code);
    FacilityType findByOpenlmisId(Integer id);
    List<FacilityType> findByName(String name);
    FacilityType createFromDetails(FacilityTypeDto details);
}
