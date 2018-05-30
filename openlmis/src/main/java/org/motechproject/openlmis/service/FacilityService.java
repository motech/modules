package org.motechproject.openlmis.service;

import java.util.List;

import org.motechproject.openlmis.domain.Facility;
import org.motechproject.openlmis.rest.domain.FacilityDto;

/**
 * Manages CRUD operations for a {@link org.motechproject.openlmis.domain.Facility}
 */
public interface FacilityService extends GenericCrudService<Facility> {
    Facility findByCode(String code);
    Facility findByOpenlmisId(Integer id);
    List<Facility> findByName(String name);
    Facility createFromDetails(FacilityDto details);
}
