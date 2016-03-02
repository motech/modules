package org.motechproject.openlmis.service;

import org.motechproject.openlmis.domain.DosageUnit;
import org.motechproject.openlmis.rest.domain.DosageUnitDto;

/**
 * Manages CRUD operations for a {@link org.motechproject.openlmis.domain.DosageUnit}
 */
public interface DosageUnitService extends GenericCrudService<DosageUnit> {
    DosageUnit findByCode(String code);
    DosageUnit findByOpenlmisId(Integer id);
    DosageUnit createFromDetails(DosageUnitDto details);
}
