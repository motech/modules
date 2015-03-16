package org.motechproject.csd.service.impl;

import org.joda.time.DateTime;
import org.motechproject.csd.domain.Provider;
import org.motechproject.csd.domain.ProviderDirectory;
import org.motechproject.csd.mds.ProviderDirectoryDataService;
import org.motechproject.csd.service.ProviderDirectoryService;
import org.motechproject.csd.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public ProviderDirectory update(ProviderDirectory directory) {

        if (directory != null) {
            List<Provider> updatedProviders = providerService.update(directory.getProviders());
            ProviderDirectory providerDirectory = getProviderDirectory();

            if (providerDirectory != null) {
                providerDirectory.setProviders(updatedProviders);
            } else {
                providerDirectory = new ProviderDirectory(updatedProviders);
            }

            providerDirectoryDataService.update(providerDirectory);
            return providerDirectory;
        }

        return null;
    }

    @Override
    public List<Provider> getModifiedAfter(DateTime date) {
        List<Provider> allProviders = providerService.allProviders();
        List<Provider> modifiedProviders = new ArrayList<>();

        for (Provider provider : allProviders) {
            if (provider.getRecord().getUpdated().isAfter(date)) {
                modifiedProviders.add(provider);
            }
        }
        return modifiedProviders;
    }
}
