package org.motechproject.openlmis.service.impl;

import java.util.List;

import org.motechproject.openlmis.domain.FacilityType;
import org.motechproject.openlmis.repository.FacilityTypeDataService;
import org.motechproject.openlmis.repository.GeographicLevelDataService;
import org.motechproject.openlmis.rest.domain.FacilityTypeDto;
import org.motechproject.openlmis.service.FacilityTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Implementation of {@link org.motechproject.openlmis.service.FacilityTypeService}
 */
@Service("facilityTypeService")
public class FacilityTypeServiceImpl implements FacilityTypeService {
    
    @Autowired
    private FacilityTypeDataService facilityTypeDataService;
    
    @Autowired
    private GeographicLevelDataService geographicLevelDataService;

    @Override
    public FacilityType findByOpenlmisId(Integer id) {
        return facilityTypeDataService.findByOpenlmisId(id);
    }
    
    @Override
    public List<FacilityType> findByName(String name) {
        return facilityTypeDataService.findByName(name);
    }

    @Override
    public void update(FacilityType facilityType) {
        facilityTypeDataService.update(facilityType);
    }

    @Override
    public void delete(FacilityType facilityType) {
        facilityTypeDataService.delete(facilityType);
    }

    @Override
    public FacilityType createFromDetails(FacilityTypeDto details) {
        FacilityType facilityType = new FacilityType();
        facilityType.setActive(details.getActive());
        facilityType.setCode(details.getCode());
        facilityType.setDescription(details.getDescription());
        facilityType.setDisplayOrder(details.getDisplayOrder());
        if (details.getLevelId() != null) {
            facilityType.setLevel(geographicLevelDataService.findById((long) details.getLevelId()));
        }
        facilityType.setName(details.getName());
        facilityType.setNominalEop(details.getNominalEop());
        facilityType.setNominalMaxMonth(details.getNominalMaxMonth());
        facilityType.setOpenlmisid(details.getId());
        return facilityTypeDataService.create(facilityType);
    }

    @Override
    public void deleteAll() {
        facilityTypeDataService.deleteAll();
    }

    @Override
    public List<FacilityType> findAll() {
        return facilityTypeDataService.retrieveAll();
    }

    @Override
    public FacilityType findByCode(String code) {
        return facilityTypeDataService.findByCode(code);
    }

}
