package org.motechproject.csd.service.impl;

import org.joda.time.DateTime;
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
    public void deleteAll() {
        providerDataService.deleteAll();
    }

    @Override
    public Provider getProviderByEntityID(String entityID) {
        return providerDataService.findByEntityID(entityID);
    }

    @Override
    public void update(Provider provider) {
        delete(provider.getEntityID());
        providerDataService.create(provider);
    }

    @Override
    public void delete(String entityID) {
        Provider provider = getProviderByEntityID(entityID);
        if (provider != null) {
            providerDataService.delete(provider);
        }
    }

    @Override
    public void update(Set<Provider> providers) {
        for (Provider provider : providers) {
            update(provider);
        }
    }

    @Override
    public Set<Provider> getModifiedAfter(DateTime date) {
        List<Provider> allProviders = allProviders();
        Set<Provider> modifiedProviders = new HashSet<>();

        for (Provider provider : allProviders) {
            if (provider.getRecord().getUpdated().isAfter(date)) {
                modifiedProviders.add(provider);
            }
        }
        return modifiedProviders;
    }
}
