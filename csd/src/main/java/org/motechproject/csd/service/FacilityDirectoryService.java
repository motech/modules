package org.motechproject.csd.service;

import org.joda.time.DateTime;
import org.motechproject.csd.domain.Facility;
import org.motechproject.csd.domain.FacilityDirectory;

import java.util.List;

public interface FacilityDirectoryService {

    FacilityDirectory getFacilityDirectory();

    void update(FacilityDirectory facilityDirectory);

    List<Facility> getModifiedAfter(DateTime date);
}
