package org.motechproject.csd.service;

import org.joda.time.DateTime;
import org.motechproject.csd.domain.Provider;

import java.util.List;
import java.util.Set;

public interface ProviderService {

    List<Provider> allProviders();

    void deleteAll();

    Provider getProviderByEntityID(String entityID);

    void update(Provider provider);

    void delete(String entityID);

    void update(Set<Provider> providers);

    Set<Provider> getModifiedAfter(DateTime date);
}
