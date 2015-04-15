package org.motechproject.csd.service.impl;

import org.joda.time.DateTime;
import org.motechproject.csd.domain.Organization;
import org.motechproject.csd.domain.OrganizationDirectory;
import org.motechproject.csd.mds.OrganizationDirectoryDataService;
import org.motechproject.csd.service.OrganizationDirectoryService;
import org.motechproject.csd.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            OrganizationDirectory organizationDirectory = getOrganizationDirectory();

            if (organizationDirectory != null) {
                Set<Organization> organizationsToRemove = organizationService.removeAndCreate(directory.getOrganizations());
                organizationDirectory.getOrganizations().removeAll(organizationsToRemove);
                organizationDirectory.getOrganizations().addAll(directory.getOrganizations());
                organizationDirectoryDataService.update(organizationDirectory);
            } else {
                organizationDirectoryDataService.create(directory);
            }
        }
    }

    @Override
    public Set<Organization> getModifiedAfter(DateTime date) {
        List<Organization> allOrganizations = organizationService.allOrganizations();
        Set<Organization> modifiedOrganizations = new HashSet<>();

        for (Organization organization : allOrganizations) {
            if (organization.getRecord().getUpdated().isAfter(date)) {
                modifiedOrganizations.add(organization);
            }
        }
        return modifiedOrganizations;
    }
}
