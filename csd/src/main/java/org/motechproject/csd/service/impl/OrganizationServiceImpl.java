package org.motechproject.csd.service.impl;

import org.joda.time.DateTime;
import org.motechproject.csd.domain.Organization;
import org.motechproject.csd.mds.OrganizationDataService;
import org.motechproject.csd.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("organizationService")
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrganizationDataService organizationDataService;

    @Override
    @Transactional
    public List<Organization> allOrganizations() {
        return organizationDataService.retrieveAll();
    }

    @Override
    @Transactional
    public void deleteAll() {
        organizationDataService.deleteAll();
    }

    @Override
    @Transactional
    public Organization getOrganizationByEntityID(String entityID) {
        return organizationDataService.findByEntityID(entityID);
    }

    @Override
    @Transactional
    public void update(Organization organization) {
        delete(organization.getEntityID());
        organizationDataService.create(organization);
    }

    @Override
    @Transactional
    public void delete(String entityID) {
        Organization organization = getOrganizationByEntityID(entityID);
        if (organization != null) {
            organizationDataService.delete(organization);
        }
    }

    @Override
    @Transactional
    public void update(Set<Organization> organizations) {
        for (Organization organization : organizations) {
            update(organization);
        }
    }

    @Override
    @Transactional
    public Set<Organization> getModifiedAfter(DateTime date) {
        List<Organization> allOrganizations = allOrganizations();
        Set<Organization> modifiedOrganizations = new HashSet<>();

        for (Organization organization : allOrganizations) {
            if (organization.getRecord().getUpdated().isAfter(date)) {
                modifiedOrganizations.add(organization);
            }
        }
        return modifiedOrganizations;
    }
}
