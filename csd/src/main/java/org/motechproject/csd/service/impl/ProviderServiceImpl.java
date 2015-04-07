package org.motechproject.csd.service.impl;

import org.motechproject.csd.domain.Provider;
import org.motechproject.csd.mds.ProviderDataService;
import org.motechproject.csd.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Provider update(Provider provider) {
        delete(provider.getEntityID());
        return providerDataService.create(provider);
    }

    @Override
    public void delete(String entityID) {
        Provider provider = getProviderByEntityID(entityID);

        if (provider != null) {
            providerDataService.delete(provider);
        }
    }

    @Override
    public Set<Provider> update(Set<Provider> providers) {
        for (Provider provider : providers) {
            update(provider);
        }
        return providers;
    }
}
