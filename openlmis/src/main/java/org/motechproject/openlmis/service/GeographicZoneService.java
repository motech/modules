package org.motechproject.openlmis.service;

import java.util.List;

import org.motechproject.openlmis.domain.GeographicZone;
import org.motechproject.openlmis.rest.domain.GeographicZoneDto;

/**
 * Manages CRUD operations for a {@link org.motechproject.openlmis.domain.GeographicZone}
 */
public interface GeographicZoneService extends GenericCrudService<GeographicZone> {
    GeographicZone findByCode(String code);
    GeographicZone findByOpenlmisId(Integer id);
    List<GeographicZone> findByName(String name);
    GeographicZone createFromDetails(GeographicZoneDto details);
}
