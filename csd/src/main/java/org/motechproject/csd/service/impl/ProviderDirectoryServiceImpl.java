package org.motechproject.csd.service.impl;

import org.joda.time.DateTime;
import org.motechproject.csd.domain.Provider;
import org.motechproject.csd.domain.ProviderDirectory;
import org.motechproject.csd.mds.ProviderDirectoryDataService;
import org.motechproject.csd.service.ProviderDirectoryService;
import org.motechproject.csd.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("providerDirectoryService")
public class ProviderDirectoryServiceImpl implements ProviderDirectoryService {

    @Autowired
    private ProviderDirectoryDataService providerDirectoryDataService;

    @Autowired
    private ProviderService providerService;

    @Override
    public ProviderDirectory getProviderDirectory() {

        List<ProviderDirectory> providerDirectoryList = providerDirectoryDataService.retrieveAll();

        if (providerDirectoryList.size() > 1) {
            throw new IllegalArgumentException("In the database can be only one CSD element");
        }

        if (providerDirectoryList.isEmpty()) {
            return null;
        } else {
            return providerDirectoryList.get(0);
        }
    }

    @Override
    public void update(final ProviderDirectory directory) {

        if (directory != null && directory.getProviders() != null && !directory.getProviders().isEmpty()) {
            ProviderDirectory providerDirectory = getProviderDirectory();

            if (providerDirectory != null) {
                Set<Provider> providersToRemove = providerService.removeAndCreate(directory.getProviders());
                providerDirectory.getProviders().removeAll(providersToRemove);
                providerDirectory.getProviders().addAll(directory.getProviders());
                providerDirectoryDataService.update(providerDirectory);
            } else {
                providerDirectoryDataService.create(directory);
            }
        }
    }

    @Override
    public Set<Provider> getModifiedAfter(DateTime date) {
        List<Provider> allProviders = providerService.allProviders();
        Set<Provider> modifiedProviders = new HashSet<>();

        for (Provider provider : allProviders) {
            if (provider.getRecord().getUpdated().isAfter(date)) {
                modifiedProviders.add(provider);
            }
        }
        return modifiedProviders;
    }
}
