package org.motechproject.csd.service.impl;

import org.motechproject.csd.domain.Facility;
import org.motechproject.csd.mds.FacilityDataService;
import org.motechproject.csd.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("facilityService")
public class FacilityServiceImpl implements FacilityService {

    @Autowired
    private FacilityDataService facilityDataService;

    public Facility getFacility(String uuid) {
        return null;
    }

    public List<Facility> allFacilities() {
        return facilityDataService.retrieveAll();
    }

    @Override
    public Facility getFacilityByEntityID(String entityID) {
        return facilityDataService.findByEntityID(entityID);
    }

    @Override
    public Facility update(Facility facility) {
        delete(facility.getEntityID());
        return facilityDataService.create(facility);
    }

    @Override
    public void delete(String entityID) {
        Facility facility = getFacilityByEntityID(entityID);

        if (facility != null) {
            facilityDataService.delete(facility);
        }
    }

    @Override
    public List<Facility> update(List<Facility> facilities) {
        for (Facility facility : facilities) {
            update(facility);
        }
        return facilityDataService.retrieveAll();
    }
}
