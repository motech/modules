package org.motechproject.csd.service.impl;

import org.motechproject.csd.domain.Organization;
import org.motechproject.csd.mds.OrganizationDataService;
import org.motechproject.csd.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("organizationService")
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrganizationDataService organizationDataService;

    @Override
    public List<Organization> allOrganizations() {
        return organizationDataService.retrieveAll();
    }

    @Override
    public Organization getOrganizationByEntityID(String entityID) {
        return organizationDataService.findByEntityID(entityID);
    }

    @Override
    public Organization removeAndCreate(Organization organization) {
        Organization organizationToRemove = getOrganizationByEntityID(organization.getEntityID());
        organizationDataService.create(organization);
        return organizationToRemove;
    }

    @Override
    public Set<Organization> removeAndCreate(Set<Organization> organizations) {
        Set<Organization> organizationsToRemove = new HashSet<>();
        for (Organization organization : organizations) {
            Organization organizationToRemove = removeAndCreate(organization);
            if (organizationToRemove != null) {
                organizationsToRemove.add(organizationToRemove);
            }
        }
        return organizationsToRemove;
    }
}
