package org.motechproject.openmrs.resource.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringUtils;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.Concept;
import org.motechproject.openmrs.domain.Observation;
import org.motechproject.openmrs.domain.ObservationListResult;
import org.motechproject.openmrs.domain.Person;
import org.motechproject.openmrs.resource.ObservationResource;
import org.motechproject.openmrs.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

@Component
public class ObservationResourceImpl extends BaseResource implements ObservationResource {

    @Autowired
    public ObservationResourceImpl(RestOperations restOperations) {
        super(restOperations);
    }

    @Override
    public ObservationListResult queryForObservationsByPatientId(Config config, String uuid) {
        String responseJson = getJson(config, "/obs?patient={uuid}&v=full", uuid);
        return (ObservationListResult) JsonUtils.readJson(responseJson, ObservationListResult.class);
    }

    @Override
    public void voidObservation(Config config, String id, String reason) {
        if (StringUtils.isEmpty(reason)) {
            delete(config, "/obs/{uuid}?!purge", id);
        } else {
            delete(config, "/obs/{uuid}?!purge&reason={reason}", id, reason);
        }
    }

    @Override
    public Observation getObservationById(Config config, String uuid) {
        String responseJson = getJson(config, "/obs/{uuid}?v=full", uuid);
        return (Observation) JsonUtils.readJson(responseJson, Observation.class);
    }

    @Override
    public Observation createObservation(Config config, Observation observation) {
        String requestJson = buildGson().toJson(observation);
        String responseJson = postForJson(config, requestJson, "/obs");
        return (Observation) JsonUtils.readJson(responseJson, Observation.class);
    }

    @Override
    public void deleteObservation(Config config, String uuid) {
        delete(config, "/obs/{uuid}?purge", uuid);
    }

    private Gson buildGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Observation.ObservationValue.class, new Observation.ObservationValueSerializer())
                .registerTypeAdapter(Concept.class, new Concept.ConceptSerializer())
                .registerTypeAdapter(Person.class, new Person.PersonSerializer())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();
    }
}
