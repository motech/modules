package org.motechproject.openmrs.resource.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.Person;
import org.motechproject.openmrs.domain.Provider;
import org.motechproject.openmrs.resource.ProviderResource;
import org.motechproject.openmrs.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

@Component
public class ProviderResourceImpl extends BaseResource implements ProviderResource {

    @Autowired
    public ProviderResourceImpl(RestOperations restOperations) {
        super(restOperations);
    }

    @Override
    public Provider createProvider(Config config, Provider provider) {
        String requestJson = buildGson().toJson(provider, Provider.class);
        String responseJson = postForJson(config, requestJson, "/provider");
        return (Provider) JsonUtils.readJson(responseJson, Provider.class);
    }

    @Override
    public Provider getByUuid(Config config, String uuid) {
        String responseJson = getJson(config, "/provider/{uuid}", uuid);
        return (Provider) JsonUtils.readJson(responseJson, Provider.class);
    }

    @Override
    public void deleteProvider(Config config, String uuid) {
        delete(config, "/provider/{uuid}?purge", uuid);
    }

    private Gson buildGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Person.class, new Person.PersonSerializer())
                .create();
    }
}
