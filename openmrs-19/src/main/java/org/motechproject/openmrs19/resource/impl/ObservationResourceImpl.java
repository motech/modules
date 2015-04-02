package org.motechproject.openmrs19.resource.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringUtils;
import org.motechproject.openmrs19.OpenMrsInstance;
import org.motechproject.openmrs19.exception.HttpException;
import org.motechproject.openmrs19.resource.ObservationResource;
import org.motechproject.openmrs19.resource.model.Observation;
import org.motechproject.openmrs19.resource.model.Observation.ObservationValue;
import org.motechproject.openmrs19.resource.model.Observation.ObservationValueDeserializer;
import org.motechproject.openmrs19.resource.model.ObservationListResult;
import org.motechproject.openmrs19.rest.RestClient;
import org.motechproject.openmrs19.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Component
public class ObservationResourceImpl implements ObservationResource {

    private final RestClient restClient;
    private final OpenMrsInstance openmrsInstance;

    @Autowired
    public ObservationResourceImpl(RestClient restClient, OpenMrsInstance openmrsInstance) {
        this.restClient = restClient;
        this.openmrsInstance = openmrsInstance;
    }

    @Override
    public ObservationListResult queryForObservationsByPatientId(String uuid) throws HttpException {
        String responseJson = restClient.getJson(openmrsInstance.toInstancePathWithParams("/obs?patient={uuid}&v=full",
                uuid));

        Map<Type, Object> adapters = getObsAdapters();
        ObservationListResult result = (ObservationListResult) JsonUtils.readJsonWithAdapters(responseJson,
                ObservationListResult.class, adapters);
        return result;
    }

    @Override
    public void voidObservation(String id, String reason) throws HttpException {
        URI uri;
        if (StringUtils.isEmpty(reason)) {
            uri = openmrsInstance.toInstancePathWithParams("/obs/{uuid}?!purge", id);
        } else {
            uri = openmrsInstance.toInstancePathWithParams("/obs/{uuid}?!purge&reason={reason}", id, reason);
        }

        restClient.delete(uri);
    }

    @Override
    public Observation getObservationById(String uuid) throws HttpException {
        Map<Type, Object> adapters = getObsAdapters();
        String responseJson = restClient.getJson(openmrsInstance.toInstancePathWithParams("/obs/{uuid}?v=full", uuid));
        return (Observation) JsonUtils.readJsonWithAdapters(responseJson, Observation.class, adapters);
    }

    @Override
    public Observation createObservation(Observation observation) throws HttpException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Observation.class, new Observation.ObservationSerializer())
                .create();
        String responseJson = restClient.postForJson(openmrsInstance.toInstancePath("/obs"), gson.toJson(observation));
        return (Observation) JsonUtils.readJsonWithAdapters(responseJson, Observation.class, getObsAdapters());
    }

    @Override
    public void deleteObservation(String uuid) throws HttpException {
        restClient.delete(openmrsInstance.toInstancePathWithParams("/obs/{uuid}?purge", uuid));
    }

    private Map<Type, Object> getObsAdapters() {
        Map<Type, Object> adapters = new HashMap<Type, Object>();
        adapters.put(ObservationValue.class, new ObservationValueDeserializer());
        return adapters;
    }

}
