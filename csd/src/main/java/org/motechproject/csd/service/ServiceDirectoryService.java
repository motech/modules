package org.motechproject.csd.service;

import org.joda.time.DateTime;
import org.motechproject.csd.domain.Service;
import org.motechproject.csd.domain.ServiceDirectory;

import java.util.List;

public interface ServiceDirectoryService {

    ServiceDirectory getServiceDirectory();

    ServiceDirectory update(ServiceDirectory serviceDirectory);

    List<Service> getModifiedAfter(DateTime date);
}
