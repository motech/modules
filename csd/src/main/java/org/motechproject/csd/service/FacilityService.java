package org.motechproject.csd.service;


import org.joda.time.DateTime;
import org.motechproject.csd.domain.Facility;

import java.util.List;
import java.util.Set;

/**
 * See {@link org.motechproject.csd.domain.Facility}
 */
public interface FacilityService {

    List<Facility> allFacilities();

    void deleteAll();

    Facility getFacilityByEntityID(String entityID);

    void update(Facility facility);

    void delete(String entityID);

    void update(Set<Facility> facilities);

    Set<Facility> getModifiedAfter(DateTime date);
}
