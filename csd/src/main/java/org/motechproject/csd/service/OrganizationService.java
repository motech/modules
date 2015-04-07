package org.motechproject.csd.service;

import org.motechproject.csd.domain.Organization;

import java.util.List;
import java.util.Set;

public interface OrganizationService {

    List<Organization> allOrganizations();

    Organization getOrganizationByEntityID(String entityID);

    Organization update(Organization organization);

    void delete(String entityID);

    Set<Organization> update(Set<Organization> organizations);
}
