package org.motechproject.dhis2.service.impl;

import org.motechproject.dhis2.domain.OrgUnit;
import org.motechproject.dhis2.repository.OrgUnitDataService;
import org.motechproject.dhis2.rest.domain.OrganisationUnitDto;
import org.motechproject.dhis2.service.OrgUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link org.motechproject.dhis2.service.OrgUnitService}
 */
@Service("orgUnitService")
public class OrgUnitServiceImpl implements OrgUnitService {
    @Autowired
    private OrgUnitDataService orgUnitDataService;

    @Override
    @Transactional
    public List<OrgUnit> findAll() {
        return orgUnitDataService.retrieveAll();
    }

    @Override
    @Transactional
    public OrgUnit findById(String id) {
        return orgUnitDataService.findByUuid(id);
    }

    @Override
    @Transactional
    public OrgUnit findByName(String name) {
        return orgUnitDataService.findByName(name);
    }

    @Override
    @Transactional
    public OrgUnit createFromDetails(OrganisationUnitDto details) {
        OrgUnit orgUnit = new OrgUnit();
        orgUnit.setUuid(details.getId());
        orgUnit.setName(details.getName());
        return orgUnitDataService.create(orgUnit);
    }

    @Override
    @Transactional
    public void update(OrgUnit orgUnit) {
        orgUnitDataService.update(orgUnit);
    }

    @Override
    @Transactional
    public void delete(OrgUnit orgUnit) {
        orgUnitDataService.delete(orgUnit);
    }

    @Override
    @Transactional
    public void deleteAll() {
        orgUnitDataService.deleteAll();
    }
}
