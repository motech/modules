package org.motechproject.openlmis.service.impl;

import java.util.List;

import org.motechproject.openlmis.domain.DosageUnit;
import org.motechproject.openlmis.repository.DosageUnitDataService;
import org.motechproject.openlmis.rest.domain.DosageUnitDto;
import org.motechproject.openlmis.service.DosageUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Implementation of {@link org.motechproject.openlmis.service.DosageUnitService}
 */
@Service("dosageUnitService")
public class DosageUnitServiceImpl implements DosageUnitService {
    @Autowired
    private DosageUnitDataService dosageUnitDataService;

    @Override
    public DosageUnit findByOpenlmisId(Integer id) {
        return dosageUnitDataService.findByOpenlmisId(id);
    }
    
    @Override
    public void update(DosageUnit dosageUnit) {
        dosageUnitDataService.update(dosageUnit);
    }

    @Override
    public void delete(DosageUnit dosageUnit) {
        dosageUnitDataService.delete(dosageUnit);
    }

    @Override
    public DosageUnit createFromDetails(DosageUnitDto details) {
        DosageUnit dosageUnit = new DosageUnit();
        dosageUnit.setCode(details.getCode());
        dosageUnit.setDisplayOrder(details.getDisplayOrder());
        dosageUnit.setOpenlmisid(details.getId());
        return dosageUnitDataService.create(dosageUnit);
    }

    @Override
    public void deleteAll() {
        dosageUnitDataService.deleteAll();
    }

    @Override
    public List<DosageUnit> findAll() {
        return dosageUnitDataService.retrieveAll();
    }

    @Override
    public DosageUnit findByCode(String code) {
        return dosageUnitDataService.findByCode(code);
    }

    

}
