package org.motechproject.openmrs19.service.impl;

import org.motechproject.openmrs19.domain.Provider;
import org.motechproject.openmrs19.exception.HttpException;
import org.motechproject.openmrs19.resource.ProviderResource;
import org.motechproject.openmrs19.service.OpenMRSProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("providerService")
public class OpenMRSProviderServiceImpl implements OpenMRSProviderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenMRSProviderService.class);

    private final ProviderResource providerResource;

    @Autowired
    OpenMRSProviderServiceImpl(ProviderResource providerResource) {
        this.providerResource = providerResource;
    }

    @Override
    public Provider createProvider(Provider provider) {
        try {
            return providerResource.createProvider(provider);
        } catch (HttpException e) {
            LOGGER.error("Error while saving provider!");
            return null;
        }
    }

    @Override
    public Provider getProviderByUuid(String uuid) {
        try {
            return providerResource.getByUuid(uuid);
        } catch (HttpException e) {
            LOGGER.error("Error while fetching provider with UUID: " + uuid);
            return null;
        }
    }

    @Override
    public void deleteProvider(String uuid) {
        try {
            providerResource.deleteProvider(uuid);
        } catch (HttpException e) {
            LOGGER.error("Error while deleting provider with UUID: " + uuid);
        }
    }
}
