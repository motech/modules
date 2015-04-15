package org.motechproject.csd.service.impl;

import org.motechproject.csd.domain.Provider;
import org.motechproject.csd.mds.ProviderDataService;
import org.motechproject.csd.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("providerService")
public class ProviderServiceImpl implements ProviderService {

    @Autowired
    private ProviderDataService providerDataService;

    @Override
    public List<Provider> allProviders() {
        return providerDataService.retrieveAll();
    }

    @Override
    public Provider getProviderByEntityID(String entityID) {
        return providerDataService.findByEntityID(entityID);
    }

    @Override
    public Provider removeAndCreate(Provider provider) {
        Provider providerToRemove = getProviderByEntityID(provider.getEntityID());
        providerDataService.create(provider);
        return providerToRemove;
    }

    @Override
    public Set<Provider> removeAndCreate(Set<Provider> providers) {
        Set<Provider> providersToRemove = new HashSet<>();
        for (Provider provider : providers) {
            Provider providerToRemove = removeAndCreate(provider);
            if (providerToRemove != null) {
                providersToRemove.add(providerToRemove);
            }
        }
        return providersToRemove;
    }
}
