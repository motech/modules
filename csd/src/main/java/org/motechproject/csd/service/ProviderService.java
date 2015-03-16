package org.motechproject.csd.service;

import org.motechproject.csd.domain.Provider;

import java.util.List;

public interface ProviderService {

    List<Provider> allProviders();

    Provider getProviderByEntityID(String entityID);

    Provider update(Provider provider);

    void delete(String entityID);

    List<Provider> update(List<Provider> providers);
}
