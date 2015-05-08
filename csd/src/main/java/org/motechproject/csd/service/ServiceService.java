package org.motechproject.csd.service;

import org.joda.time.DateTime;
import org.motechproject.csd.domain.Service;

import java.util.List;
import java.util.Set;

public interface ServiceService {

    List<Service> allServices();

    void deleteAll();

    Service getServiceByEntityID(String entityID);

    void update(Service service);

    void delete(String entityID);

    void update(Set<Service> services);

    Set<Service> getModifiedAfter(DateTime date);
}
