package org.motechproject.csd.service.impl;

import org.motechproject.csd.domain.Service;
import org.motechproject.csd.mds.ServiceDataService;
import org.motechproject.csd.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;

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
    public Service update(Service service) {
        delete(service.getEntityID());
        return serviceDataService.create(service);
    }

    @Override
    public void delete(String entityID) {
        Service service = getServiceByEntityID(entityID);

        if (service != null) {
            serviceDataService.delete(service);
        }
    }

    @Override
    public Set<Service> update(Set<Service> services) {
        for (Service service : services) {
            update(service);
        }
        return services;
    }
}
