package org.motechproject.csd.service;

import org.joda.time.DateTime;
import org.motechproject.csd.domain.Organization;
import org.motechproject.csd.domain.OrganizationDirectory;

import java.util.List;

public interface OrganizationDirectoryService {

    OrganizationDirectory getOrganizationDirectory();

    OrganizationDirectory update(OrganizationDirectory organizationDirectory);

    List<Organization> getModifiedAfter(DateTime date);
}
