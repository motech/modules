package org.motechproject.openmrs19.resource.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.motechproject.openmrs19.OpenMrsInstance;
import org.motechproject.openmrs19.domain.Provider;
import org.motechproject.openmrs19.exception.HttpException;
import org.motechproject.openmrs19.resource.ProviderResource;
import org.motechproject.openmrs19.rest.RestClient;
import org.motechproject.openmrs19.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProviderResourceImpl implements ProviderResource {

    private final RestClient restClient;
    private final OpenMrsInstance openmrsInstance;

    @Autowired
    public ProviderResourceImpl(RestClient restClient, OpenMrsInstance openmrsInstance) {
        this.restClient = restClient;
        this.openmrsInstance = openmrsInstance;
    }

    @Override
    public Provider createProvider(Provider provider) throws HttpException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Provider.class, new Provider.ProviderSerializer())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();

        String requestJson = gson.toJson(provider, Provider.class);
        String responseJson = restClient.postForJson(openmrsInstance.toInstancePath("/provider"), requestJson);
        return (Provider) JsonUtils.readJson(responseJson, Provider.class);
    }

    @Override
    public Provider getByUuid(String uuid) throws HttpException {
        String responseJson = restClient.getJson(openmrsInstance.toInstancePathWithParams("/provider/{uuid}", uuid));
        return (Provider) JsonUtils.readJson(responseJson, Provider.class);
    }

    @Override
    public void deleteProvider(String uuid) throws HttpException {
        restClient.delete(openmrsInstance.toInstancePathWithParams("/provider/{uuid}?purge", uuid));
    }
}
