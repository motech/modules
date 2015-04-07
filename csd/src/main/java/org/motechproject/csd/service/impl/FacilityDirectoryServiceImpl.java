package org.motechproject.csd.service.impl;

import org.joda.time.DateTime;
import org.motechproject.csd.domain.Facility;
import org.motechproject.csd.domain.FacilityDirectory;
import org.motechproject.csd.mds.FacilityDirectoryDataService;
import org.motechproject.csd.service.FacilityDirectoryService;
import org.motechproject.csd.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("facilityDirectoryService")
public class FacilityDirectoryServiceImpl implements FacilityDirectoryService {

    @Autowired
    private FacilityDirectoryDataService facilityDirectoryDataService;

    @Autowired
    private FacilityService facilityService;

    @Override
    public FacilityDirectory getFacilityDirectory() {

        List<FacilityDirectory> facilityDirectoryList = facilityDirectoryDataService.retrieveAll();

        if (facilityDirectoryList.size() > 1) {
            throw new IllegalArgumentException("In the database can be only one FacilityDirectory element");
        }

        if (facilityDirectoryList.isEmpty()) {
            return null;
        } else {
            return facilityDirectoryList.get(0);
        }
    }

    @Override
    public void update(final FacilityDirectory directory) {

        if (directory != null && directory.getFacilities() != null && !directory.getFacilities().isEmpty()) {
            facilityDirectoryDataService.doInTransaction(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                    Set<Facility> updatedFacilities = facilityService.update(directory.getFacilities());
                    FacilityDirectory facilityDirectory = getFacilityDirectory();

                    if (facilityDirectory != null) {
                        facilityDirectory.getFacilities().addAll(updatedFacilities);
                        facilityDirectoryDataService.update(facilityDirectory);
                    } else {
                        facilityDirectoryDataService.create(directory);
                    }
                }
            });
        }
    }

    @Override
    public Set<Facility> getModifiedAfter(DateTime date) {
        List<Facility> allFacilities = facilityService.allFacilities();
        Set<Facility> modifiedFacilities = new HashSet<>();

        for (Facility facility : allFacilities) {
            if (facility.getRecord().getUpdated().isAfter(date)) {
                modifiedFacilities.add(facility);
            }
        }
        return modifiedFacilities;
    }
}
