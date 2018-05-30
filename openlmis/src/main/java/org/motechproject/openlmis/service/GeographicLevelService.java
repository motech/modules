package org.motechproject.openlmis.service;

import java.util.List;

import org.motechproject.openlmis.domain.GeographicLevel;
import org.motechproject.openlmis.rest.domain.GeographicLevelDto;

/**
 * Manages CRUD operations for a {@link org.motechproject.openlmis.domain.GeographicLevel}
 */
public interface GeographicLevelService extends GenericCrudService<GeographicLevel> {
    GeographicLevel findByCode(String code);
    GeographicLevel findByOpenlmisId(Integer id);
    List<GeographicLevel> findByName(String name);
    GeographicLevel createFromDetails(GeographicLevelDto details);
}
