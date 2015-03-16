package org.motechproject.csd.service;

import org.joda.time.DateTime;
import org.motechproject.csd.domain.Provider;
import org.motechproject.csd.domain.ProviderDirectory;

import java.util.List;

public interface ProviderDirectoryService {

    ProviderDirectory getProviderDirectory();

    ProviderDirectory update(ProviderDirectory providerDirectory);

    List<Provider> getModifiedAfter(DateTime date);
}
