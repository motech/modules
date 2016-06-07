package org.motechproject.openmrs.resource.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.Encounter;
import org.motechproject.openmrs.domain.EncounterListResult;
import org.motechproject.openmrs.domain.EncounterType;
import org.motechproject.openmrs.domain.Observation;
import org.motechproject.openmrs.resource.EncounterResource;
import org.motechproject.openmrs.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import java.util.ArrayList;
import java.util.List;

@Component
public class EncounterResourceImpl extends BaseResource implements EncounterResource {

    @Autowired
    public EncounterResourceImpl(RestOperations restOperations) {
        super(restOperations);
    }

    @Override
    public Encounter createEncounter(Config config, Encounter encounter) {
        String requestJson = buildGsonWithAdaptersSerialize().toJson(encounter);
        String responseJson = postForJson(config, requestJson, "/encounter?v=full");

        return checkVersionAndSetEncounter(config, responseJson);
    }

    @Override
    public EncounterListResult queryForAllEncountersByPatientId(Config config, String id) {
        String responseJson = getJson(config, "/encounter?patient={id}&v=full", id);

        List<Encounter> encounterList = new ArrayList<>();
        JsonArray allEncounters = prepareJsonArray(responseJson);
        for (JsonElement encounter : allEncounters) {
            encounterList.add(checkVersionAndSetEncounter(config, encounter.toString()));
        }

        EncounterListResult encounters = new EncounterListResult();
        encounters.setResults(encounterList);

        return encounters;
    }

    @Override
    public Encounter getEncounterById(Config config, String uuid) {
        String responseJson = getJson(config, "/encounter/{uuid}?v=full", uuid);

        return  checkVersionAndSetEncounter(config, responseJson);
    }

    @Override
    public EncounterType createEncounterType(Config config, EncounterType encounterType) {
        String requestJson = buildGson().toJson(encounterType, EncounterType.class);
        String responseJson = postForJson(config, requestJson, "/encountertype");
        return (EncounterType) JsonUtils.readJson(responseJson, EncounterType.class);
    }

    @Override
    public EncounterType getEncounterTypeByUuid(Config config, String uuid) {
        String responseJson = getJson(config, "/encountertype/{uuid}", uuid);
        return (EncounterType) JsonUtils.readJson(responseJson, EncounterType.class);
    }

    @Override
    public void deleteEncounterType(Config config, String uuid) {
        delete(config, "/encountertype/{uuid}?purge", uuid);
    }

    @Override
    public void deleteEncounter(Config config, String uuid) {
        delete(config, "/encounter/{uuid}?purge", uuid);
    }

    private Gson buildGson() {
        return new GsonBuilder().create();
    }

    private Gson buildGsonWithAdaptersSerialize() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .registerTypeAdapter(Observation.class, new Observation.ObservationSerializer())
                .registerTypeAdapter(Encounter.class, new Encounter.EncounterSerializer())
                .create();
    }

    private Gson buildGsonWithAdaptersDeserialize() {
        return new GsonBuilder()
                .registerTypeAdapter(Encounter.class, new Encounter.EncounterDeserializer())
                .create();
    }

    private Encounter checkVersionAndSetEncounter(Config config, String responseJson) {
        Encounter createdEncounter;
        if ("1.9".equals(config.getOpenMrsVersion())) {
            createdEncounter = buildGsonWithAdaptersDeserialize().fromJson(responseJson, Encounter.class);
        } else {
            createdEncounter = (Encounter) JsonUtils.readJson(responseJson, Encounter.class);
        }
        return createdEncounter;
    }

    private  JsonArray prepareJsonArray(String json) {
        JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        return obj.getAsJsonArray("results");
    }
}
