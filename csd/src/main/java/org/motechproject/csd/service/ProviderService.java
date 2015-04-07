package org.motechproject.csd.service;

import org.motechproject.csd.domain.Provider;

import java.util.List;
import java.util.Set;

public interface ProviderService {

    List<Provider> allProviders();

    Provider getProviderByEntityID(String entityID);

    Provider update(Provider provider);

    void delete(String entityID);

    Set<Provider> update(Set<Provider> providers);
}
