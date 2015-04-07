package org.motechproject.csd.service.impl;

import org.joda.time.DateTime;
import org.motechproject.csd.domain.Service;
import org.motechproject.csd.domain.ServiceDirectory;
import org.motechproject.csd.mds.ServiceDirectoryDataService;
import org.motechproject.csd.service.ServiceDirectoryService;
import org.motechproject.csd.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public void update(final ServiceDirectory directory) {

        if (directory != null && directory.getServices() != null && !directory.getServices().isEmpty()) {
            serviceDirectoryDataService.doInTransaction(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                    Set<Service> updatedServices = serviceService.update(directory.getServices());
                    ServiceDirectory serviceDirectory = getServiceDirectory();

                    if (serviceDirectory != null) {
                        serviceDirectory.getServices().addAll(updatedServices);
                        serviceDirectoryDataService.update(serviceDirectory);
                    } else {
                        serviceDirectoryDataService.create(directory);
                    }
                }
            });
        }
    }

    @Override
    public Set<Service> getModifiedAfter(DateTime date) {
        List<Service> allServices = serviceService.allServices();
        Set<Service> modifiedServices = new HashSet<>();

        for (Service service : allServices) {
            if (service.getRecord().getUpdated().isAfter(date)) {
                modifiedServices.add(service);
            }
        }
        return modifiedServices;
    }
}
