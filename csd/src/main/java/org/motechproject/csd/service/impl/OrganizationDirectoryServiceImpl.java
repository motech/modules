package org.motechproject.csd.service.impl;

import org.joda.time.DateTime;
import org.motechproject.csd.domain.Organization;
import org.motechproject.csd.domain.OrganizationDirectory;
import org.motechproject.csd.mds.OrganizationDirectoryDataService;
import org.motechproject.csd.service.OrganizationDirectoryService;
import org.motechproject.csd.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public OrganizationDirectory update(OrganizationDirectory directory) {

        if (directory != null) {
            List<Organization> updatedOrganizations = organizationService.update(directory.getOrganizations());
            OrganizationDirectory organizationDirectory = getOrganizationDirectory();

            if (organizationDirectory != null) {
                organizationDirectory.setOrganizations(updatedOrganizations);
            } else {
                organizationDirectory = new OrganizationDirectory(updatedOrganizations);
            }

            organizationDirectoryDataService.update(organizationDirectory);
            return organizationDirectory;
        }

        return null;
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
