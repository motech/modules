package org.motechproject.csd.service;


import org.motechproject.csd.domain.Facility;

import java.util.List;

/**
 * See {@link org.motechproject.csd.domain.Facility}
 */
public interface FacilityService {
    Facility getFacility(String uuid);
    List<Facility> allFacilities();
}
