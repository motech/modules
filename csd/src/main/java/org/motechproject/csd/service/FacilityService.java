package org.motechproject.csd.service;


import org.motechproject.csd.domain.Facility;

import java.util.List;

/**
 * See {@link org.motechproject.csd.domain.Facility}
 */
public interface FacilityService {

    Facility getFacility(String uuid);

    List<Facility> allFacilities();

    Facility getFacilityByEntityID(String entityID);

    Facility update(Facility facility);

    void delete(String entityID);

    List<Facility> update(List<Facility> facilities);
}
