package org.motechproject.openlmis.service.impl;

import java.util.List;

import org.motechproject.openlmis.domain.GeographicZone;
import org.motechproject.openlmis.repository.GeographicLevelDataService;
import org.motechproject.openlmis.repository.GeographicZoneDataService;
import org.motechproject.openlmis.rest.domain.GeographicZoneDto;
import org.motechproject.openlmis.service.GeographicZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Implementation of {@link org.motechproject.openlmis.service.GeographicZoneService}
 */
@Service("geographicZoneService")
public class GeographicZoneServiceImpl implements GeographicZoneService {
    
    @Autowired
    private GeographicZoneDataService geographicZoneDataService;
    
    @Autowired
    private GeographicLevelDataService geographicLevelDataService;
    
    @Override
    public GeographicZone findByOpenlmisId(Integer id) {
        return geographicZoneDataService.findByOpenlmisId(id);
    }
    
    @Override
    public List<GeographicZone> findByName(String name) {
        return geographicZoneDataService.findByName(name);
    }

    @Override
    public void update(GeographicZone program) {
        geographicZoneDataService.update(program);
    }

    @Override
    public void delete(GeographicZone program) {
        geographicZoneDataService.delete(program);
    }

    @Override
    public GeographicZone createFromDetails(GeographicZoneDto details) {
        GeographicZone geographicZone = new GeographicZone();
        geographicZone.setCatchmentPopulation(details.getCatchmentPopulation());
        geographicZone.setCode(details.getCode());
        geographicZone.setLatitude(details.getLatitude());
        geographicZone.setLevel(geographicLevelDataService.findById((long) details.getLevelId()));
        geographicZone.setLongitude(details.getLongitude());
        geographicZone.setName(details.getName());
        geographicZone.setOpenlmisid(details.getId());
        if (details.getParentId() != null) {
            Long parentId = (long) details.getParentId();
            geographicZone.setParent(geographicZoneDataService.findById(parentId));
        }
        return geographicZoneDataService.create(geographicZone);
    }

    @Override
    public void deleteAll() {
        geographicZoneDataService.deleteAll();
    }

    @Override
    public List<GeographicZone> findAll() {
        return geographicZoneDataService.retrieveAll();
    }

    @Override
    public GeographicZone findByCode(String code) {
        return geographicZoneDataService.findByCode(code);
    }

}
