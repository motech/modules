package org.motechproject.csd.service;

import org.joda.time.DateTime;
import org.motechproject.csd.domain.Facility;
import org.motechproject.csd.domain.FacilityDirectory;

import java.util.Set;

public interface FacilityDirectoryService {

    FacilityDirectory getFacilityDirectory();

    void update(FacilityDirectory facilityDirectory);

    Set<Facility> getModifiedAfter(DateTime date);
}
