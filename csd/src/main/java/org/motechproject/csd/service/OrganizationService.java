package org.motechproject.csd.service;

import org.motechproject.csd.domain.Organization;

import java.util.List;

public interface OrganizationService {

    List<Organization> allOrganizations();

    Organization getOrganizationByEntityID(String entityID);

    Organization update(Organization organization);

    void delete(String entityID);

    List<Organization> update(List<Organization> organizations);
}
