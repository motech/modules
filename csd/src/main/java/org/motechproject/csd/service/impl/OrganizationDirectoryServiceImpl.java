package org.motechproject.csd.service.impl;

import org.joda.time.DateTime;
import org.motechproject.csd.domain.Organization;
import org.motechproject.csd.domain.OrganizationDirectory;
import org.motechproject.csd.mds.OrganizationDirectoryDataService;
import org.motechproject.csd.service.OrganizationDirectoryService;
import org.motechproject.csd.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import java.util.ArrayList;
import java.util.List;

@Service("organizationDirectoryService")
public class OrganizationDirectoryServiceImpl implements OrganizationDirectoryService {

    @Autowired
    private OrganizationDirectoryDataService organizationDirectoryDataService;

    @Autowired
    private OrganizationService organizationService;

    @Override
    public OrganizationDirectory getOrganizationDirectory() {

        List<OrganizationDirectory> organizationDirectoryList = organizationDirectoryDataService.retrieveAll();

        if (organizationDirectoryList.size() > 1) {
            throw new IllegalArgumentException("In the database can be only one OrganizationDirectory element");
        }

        if (organizationDirectoryList.isEmpty()) {
            return null;
        } else {
            return organizationDirectoryList.get(0);
        }
    }

    @Override
    public void update(final OrganizationDirectory directory) {

        if (directory != null && directory.getOrganizations() != null && !directory.getOrganizations().isEmpty()) {
            organizationDirectoryDataService.doInTransaction(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                    List<Organization> updatedOrganizations = organizationService.update(directory.getOrganizations());
                    OrganizationDirectory organizationDirectory = getOrganizationDirectory();

                    if (organizationDirectory != null) {
                        organizationDirectory.getOrganizations().addAll(updatedOrganizations);
                        organizationDirectoryDataService.update(organizationDirectory);
                    } else {
                        organizationDirectoryDataService.create(directory);
                    }
                }
            });
        }
    }

    @Override
    public List<Organization> getModifiedAfter(DateTime date) {
        List<Organization> allOrganizations = organizationService.allOrganizations();
        List<Organization> modifiedOrganizations = new ArrayList<>();

        for (Organization organization : allOrganizations) {
            if (organization.getRecord().getUpdated().isAfter(date)) {
                modifiedOrganizations.add(organization);
            }
        }
        return modifiedOrganizations;
    }
}
