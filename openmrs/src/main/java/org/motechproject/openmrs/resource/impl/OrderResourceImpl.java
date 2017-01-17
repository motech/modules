package org.motechproject.openmrs.resource.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.httpclient.HttpClient;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.Concept;
import org.motechproject.openmrs.domain.Encounter;
import org.motechproject.openmrs.domain.Order;
import org.motechproject.openmrs.domain.Patient;
import org.motechproject.openmrs.domain.Provider;
import org.motechproject.openmrs.resource.OrderResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

@Component
public class OrderResourceImpl extends BaseResource implements OrderResource {

    @Autowired
    public OrderResourceImpl(RestOperations restOperations, HttpClient httpClient) {
        super(restOperations, httpClient);
    }

    @Override
    public Order createOrder(Config config, Order order) {
        String requestJson = buildGson().toJson(order);
        String responseJson = postForJson(config, requestJson, "/order");
        return buildGsonDeserializer().fromJson(responseJson, Order.class);
    }

    private Gson buildGson() {
        GsonBuilder builder = new GsonBuilder();

        builder
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(Encounter.class, new Encounter.EncounterUuidSerializer())
                .registerTypeAdapter(Provider.class, new Provider.ProviderSerializer())
                .registerTypeAdapter(Patient.class, new Patient.PatientSerializer())
                .registerTypeAdapter(Concept.class, new Concept.ConceptSerializer());

        return builder.create();
    }

    private Gson buildGsonDeserializer() {
        GsonBuilder builder = new GsonBuilder();

        builder
                .registerTypeAdapter(Order.CareSetting.class, new Order.CareSettingDeserializer());

        return builder.create();
    }
}
