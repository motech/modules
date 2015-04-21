package org.motechproject.csd.service;


import org.motechproject.csd.domain.Facility;
import org.motechproject.mds.annotations.InstanceLifecycleListener;
import org.motechproject.mds.domain.InstanceLifecycleListenerType;

import java.util.List;
import java.util.Set;

/**
 * See {@link org.motechproject.csd.domain.Facility}
 */
public interface FacilityService {

    List<Facility> allFacilities();

    Facility getFacilityByEntityID(String entityID);

    Facility removeAndCreate(Facility facility);

    Set<Facility> removeAndCreate(Set<Facility> facilities);

    @InstanceLifecycleListener(InstanceLifecycleListenerType.POST_STORE)
    void updateRecord(Facility facility);

}
