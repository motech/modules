package org.motechproject.csd.service.impl;

import org.motechproject.csd.domain.Service;
import org.motechproject.csd.mds.ServiceDataService;
import org.motechproject.csd.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@org.springframework.stereotype.Service("serviceService")
public class ServiceServiceImpl implements ServiceService {

    @Autowired
    private ServiceDataService serviceDataService;

    @Override
    public List<Service> allServices() {
        return serviceDataService.retrieveAll();
    }

    @Override
    public Service getServiceByEntityID(String entityID) {
        return serviceDataService.findByEntityID(entityID);
    }

    @Override
    public Service removeAndCreate(Service service) {
        Service serviceToRemove = getServiceByEntityID(service.getEntityID());
        serviceDataService.create(service);
        return serviceToRemove;
    }

    @Override
    public Set<Service> removeAndCreate(Set<Service> services) {
        Set<Service> servicesToRemove = new HashSet<>();
        for (Service service : services) {
            Service serviceToRemove = removeAndCreate(service);
            if (serviceToRemove != null) {
                servicesToRemove.add(serviceToRemove);
            }
        }
        return servicesToRemove;
    }
}
