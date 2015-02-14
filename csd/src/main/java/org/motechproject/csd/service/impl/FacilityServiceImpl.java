package org.motechproject.csd.service.impl;

import org.motechproject.csd.domain.Facility;
import org.motechproject.csd.service.FacilityService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("facilityService")
public class FacilityServiceImpl implements FacilityService {
    private EventRelay eventRelay;

    @Autowired
    public FacilityServiceImpl(EventRelay eventRelay) {
        this.eventRelay = eventRelay;
    }

    public Facility getFacility(String uuid) {
        eventRelay.sendEventMessage(new MotechEvent("foobar"));
        return null;
    }

    public List<Facility> allFacilities() {
        return null;
    }
}
