package org.motechproject.csd.service.impl;

import org.joda.time.DateTime;
import org.motechproject.csd.domain.Service;
import org.motechproject.csd.domain.ServiceDirectory;
import org.motechproject.csd.mds.ServiceDirectoryDataService;
import org.motechproject.csd.service.ServiceDirectoryService;
import org.motechproject.csd.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service("serviceDirectoryService")
public class ServiceDirectoryServiceImpl implements ServiceDirectoryService {

    @Autowired
    private ServiceDirectoryDataService serviceDirectoryDataService;

    @Autowired
    private ServiceService serviceService;

    @Override
    public ServiceDirectory getServiceDirectory() {

        List<ServiceDirectory> serviceDirectoryList = serviceDirectoryDataService.retrieveAll();

        if (serviceDirectoryList.size() > 1) {
            throw new IllegalArgumentException("In the database can be only one ServiceDirectory element");
        }

        if (serviceDirectoryList.isEmpty()) {
            return null;
        } else {
            return serviceDirectoryList.get(0);
        }
    }

    @Override
    public ServiceDirectory update(ServiceDirectory directory) {

        if (directory != null) {
            List<Service> updatedServices = serviceService.update(directory.getServices());
            ServiceDirectory serviceDirectory = getServiceDirectory();

            if (serviceDirectory != null) {
                serviceDirectory.setServices(updatedServices);
            } else {
                serviceDirectory = new ServiceDirectory(updatedServices);
            }

            serviceDirectoryDataService.update(serviceDirectory);
            return serviceDirectory;
        }

        return null;
    }

    @Override
    public List<Service> getModifiedAfter(DateTime date) {
        List<Service> allServices = serviceService.allServices();
        List<Service> modifiedServices = new ArrayList<>();

        for (Service service : allServices) {
            if (service.getRecord().getUpdated().isAfter(date)) {
                modifiedServices.add(service);
            }
        }
        return modifiedServices;
    }
}
