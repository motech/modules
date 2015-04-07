package org.motechproject.csd.service;

import org.motechproject.csd.domain.Service;

import java.util.List;
import java.util.Set;

public interface ServiceService {

    List<Service> allServices();

    Service getServiceByEntityID(String entityID);

    Service update(Service service);

    void delete(String entityID);

    Set<Service> update(Set<Service> services);
}
