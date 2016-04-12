package org.motechproject.openmrs19.resource.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringUtils;
import org.motechproject.openmrs19.OpenMrsInstance;
import org.motechproject.openmrs19.domain.Concept;
import org.motechproject.openmrs19.domain.Observation;
import org.motechproject.openmrs19.domain.Observation.ObservationValue;
import org.motechproject.openmrs19.domain.ObservationListResult;
import org.motechproject.openmrs19.domain.Person;
import org.motechproject.openmrs19.exception.HttpException;
import org.motechproject.openmrs19.resource.ObservationResource;
import org.motechproject.openmrs19.rest.RestClient;
import org.motechproject.openmrs19.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;

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

        ObservationListResult result = (ObservationListResult) JsonUtils.readJson(responseJson,
                ObservationListResult.class);
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
        String responseJson = restClient.getJson(openmrsInstance.toInstancePathWithParams("/obs/{uuid}?v=full", uuid));
        return (Observation) JsonUtils.readJson(responseJson, Observation.class);
    }

    @Override
    public Observation createObservation(Observation observation) throws HttpException {
        Gson gson = new GsonBuilder().registerTypeAdapter(ObservationValue.class, new Observation.ObservationValueSerializer())
                .registerTypeAdapter(Concept.class, new Concept.ConceptSerializer())
                .registerTypeAdapter(Person.class, new Person.PersonSerializer())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();

        String requestJson = gson.toJson(observation);

        String responseJson = restClient.postForJson(openmrsInstance.toInstancePath("/obs"), requestJson);
        return (Observation) JsonUtils.readJson(responseJson, Observation.class);
    }

    @Override
    public void deleteObservation(String uuid) throws HttpException {
        restClient.delete(openmrsInstance.toInstancePathWithParams("/obs/{uuid}?purge", uuid));
    }
}
