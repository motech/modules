package org.motechproject.csd.service;

import org.joda.time.DateTime;
import org.motechproject.csd.domain.Service;
import org.motechproject.csd.domain.ServiceDirectory;

import java.util.Set;

public interface ServiceDirectoryService {

    ServiceDirectory getServiceDirectory();

    void update(ServiceDirectory serviceDirectory);

    Set<Service> getModifiedAfter(DateTime date);
}
