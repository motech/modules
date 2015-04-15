package org.motechproject.csd.service;

import org.motechproject.csd.domain.Service;

import java.util.List;
import java.util.Set;

public interface ServiceService {

    List<Service> allServices();

    Service getServiceByEntityID(String entityID);

    Service removeAndCreate(Service service);

    Set<Service> removeAndCreate(Set<Service> services);
}
