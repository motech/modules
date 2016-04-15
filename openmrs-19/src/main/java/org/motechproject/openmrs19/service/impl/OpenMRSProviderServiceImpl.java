package org.motechproject.openmrs19.service.impl;

import org.motechproject.openmrs19.config.Config;
import org.motechproject.openmrs19.domain.Provider;
import org.motechproject.openmrs19.resource.ProviderResource;
import org.motechproject.openmrs19.service.OpenMRSConfigService;
import org.motechproject.openmrs19.service.OpenMRSProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service("providerService")
public class OpenMRSProviderServiceImpl implements OpenMRSProviderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenMRSProviderService.class);

    private final OpenMRSConfigService configService;

    private final ProviderResource providerResource;

    @Autowired
    OpenMRSProviderServiceImpl(ProviderResource providerResource, OpenMRSConfigService configService) {
        this.providerResource = providerResource;
        this.configService = configService;
    }

    @Override
    public Provider createProvider(String configName, Provider provider) {
        try {
            Config config = configService.getConfigByName(configName);
            return providerResource.createProvider(config, provider);
        } catch (HttpClientErrorException e) {
            LOGGER.error("Error while saving provider!");
            return null;
        }
    }

    @Override
    public Provider getProviderByUuid(String configName, String uuid) {
        try {
            Config config = configService.getConfigByName(configName);
            return providerResource.getByUuid(config, uuid);
        } catch (HttpClientErrorException e) {
            LOGGER.error("Error while fetching provider with UUID: " + uuid);
            return null;
        }
    }

    @Override
    public void deleteProvider(String configName, String uuid) {
        try {
            Config config = configService.getConfigByName(configName);
            providerResource.deleteProvider(config, uuid);
        } catch (HttpClientErrorException e) {
            LOGGER.error("Error while deleting provider with UUID: " + uuid);
        }
    }

    @Override
    public Provider createProvider(Provider provider) {
        return createProvider(null, provider);
    }

    @Override
    public Provider getProviderByUuid(String uuid) {
        return getProviderByUuid(null, uuid);
    }

    @Override
    public void deleteProvider(String uuid) {
        deleteProvider(null, uuid);
    }
}
