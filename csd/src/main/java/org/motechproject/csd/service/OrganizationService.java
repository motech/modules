package org.motechproject.csd.service;

import org.motechproject.csd.domain.Organization;

import java.util.List;
import java.util.Set;

public interface OrganizationService {

    List<Organization> allOrganizations();

    Organization getOrganizationByEntityID(String entityID);

    Organization removeAndCreate(Organization organization);

    Set<Organization> removeAndCreate(Set<Organization> organizations);
}
