package org.motechproject.openlmis.service.impl;

import java.util.List;

import org.motechproject.openlmis.domain.GeographicLevel;
import org.motechproject.openlmis.repository.GeographicLevelDataService;
import org.motechproject.openlmis.rest.domain.GeographicLevelDto;
import org.motechproject.openlmis.service.GeographicLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Implementation of {@link org.motechproject.openlmis.service.GeographicLevelService}
 */
@Service("geographicLevelService")
public class GeographicLevelServiceImpl implements GeographicLevelService {
    @Autowired
    private GeographicLevelDataService geographicLevelDataService;
    
    @Override
    public GeographicLevel findByOpenlmisId(Integer id) {
        return geographicLevelDataService.findByOpenlmisId(id);
    }
    
    @Override
    public List<GeographicLevel> findByName(String name) {
        return geographicLevelDataService.findByName(name);
    }

    @Override
    public void update(GeographicLevel program) {
        geographicLevelDataService.update(program);
    }

    @Override
    public void delete(GeographicLevel program) {
        geographicLevelDataService.delete(program);
    }

    @Override
    public GeographicLevel createFromDetails(GeographicLevelDto details) {
        GeographicLevel geographicLevel = new GeographicLevel();
        geographicLevel.setCode(details.getCode());
        geographicLevel.setLevelNumber(details.getLevelNumber());
        geographicLevel.setName(details.getName());
        geographicLevel.setOpenlmisid(details.getId());
        return geographicLevelDataService.create(geographicLevel);
    }

    @Override
    public void deleteAll() {
        geographicLevelDataService.deleteAll();
    }

    @Override
    public List<GeographicLevel> findAll() {
        return geographicLevelDataService.retrieveAll();
    }

    @Override
    public GeographicLevel findByCode(String code) {
        return geographicLevelDataService.findByCode(code);
    }

}
