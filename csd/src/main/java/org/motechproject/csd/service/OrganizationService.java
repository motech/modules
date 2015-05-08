package org.motechproject.csd.service;

import org.joda.time.DateTime;
import org.motechproject.csd.domain.Organization;

import java.util.List;
import java.util.Set;

public interface OrganizationService {

    List<Organization> allOrganizations();

    void deleteAll();

    Organization getOrganizationByEntityID(String entityID);

    void update(Organization organization);

    void delete(String entityID);

    void update(Set<Organization> organizations);

    Set<Organization> getModifiedAfter(DateTime date);
}
