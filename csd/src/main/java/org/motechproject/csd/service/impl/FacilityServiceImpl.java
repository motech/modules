package org.motechproject.csd.service.impl;

import org.motechproject.csd.domain.Facility;
import org.motechproject.csd.mds.FacilityDataService;
import org.motechproject.csd.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("facilityService")
public class FacilityServiceImpl implements FacilityService {

    @Autowired
    private FacilityDataService facilityDataService;

    @Override
    public List<Facility> allFacilities() {
        return facilityDataService.retrieveAll();
    }

    @Override
    public Facility getFacilityByEntityID(String entityID) {
        return facilityDataService.findByEntityID(entityID);
    }

    @Override
    public Facility removeAndCreate(Facility facility) {
        Facility facilityToRemove = getFacilityByEntityID(facility.getEntityID());
        facilityDataService.create(facility);
        return facilityToRemove;
    }

    @Override
    public Set<Facility> removeAndCreate(Set<Facility> facilities) {
        Set<Facility> facilitiesToRemove = new HashSet<>();
        for (Facility facility : facilities) {
            Facility facilityToRemove = removeAndCreate(facility);
            if (facilityToRemove != null) {
                facilitiesToRemove.add(facilityToRemove);
            }
        }
        return facilitiesToRemove;
    }
}
