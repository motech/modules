package org.motechproject.csd.service;

import org.joda.time.DateTime;
import org.motechproject.csd.domain.Provider;
import org.motechproject.csd.domain.ProviderDirectory;

import java.util.Set;

public interface ProviderDirectoryService {

    ProviderDirectory getProviderDirectory();

    void update(ProviderDirectory providerDirectory);

    Set<Provider> getModifiedAfter(DateTime date);
}
