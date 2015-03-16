package org.motechproject.csd.service;

import org.motechproject.csd.domain.Service;

import java.util.List;

public interface ServiceService {

    List<Service> allServices();

    Service getServiceByEntityID(String entityID);

    Service update(Service service);

    void delete(String entityID);

    List<Service> update(List<Service> services);
}
