package org.motechproject.csd.service;

import org.joda.time.DateTime;
import org.motechproject.csd.domain.Organization;
import org.motechproject.csd.domain.OrganizationDirectory;

import java.util.Set;

public interface OrganizationDirectoryService {

    OrganizationDirectory getOrganizationDirectory();

    void update(OrganizationDirectory organizationDirectory);

    Set<Organization> getModifiedAfter(DateTime date);
}
