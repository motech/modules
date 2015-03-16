package org.motechproject.csd.service.impl;

import org.motechproject.csd.domain.Organization;
import org.motechproject.csd.mds.OrganizationDataService;
import org.motechproject.csd.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Organization update(Organization organization) {
        delete(organization.getEntityID());
        return organizationDataService.create(organization);
    }

    @Override
    public void delete(String entityID) {
        Organization organization = getOrganizationByEntityID(entityID);

        if (organization != null) {
            organizationDataService.delete(organization);
        }
    }

    @Override
    public List<Organization> update(List<Organization> organizations) {
        for (Organization organization : organizations) {
            update(organization);
        }
        return organizationDataService.retrieveAll();
    }
}
