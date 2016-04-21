package org.motechproject.openmrs19.resource.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.motechproject.openmrs19.config.Config;
import org.motechproject.openmrs19.domain.Encounter;
import org.motechproject.openmrs19.domain.EncounterListResult;
import org.motechproject.openmrs19.domain.EncounterType;
import org.motechproject.openmrs19.domain.Location;
import org.motechproject.openmrs19.domain.Observation;
import org.motechproject.openmrs19.domain.Patient;
import org.motechproject.openmrs19.domain.Person;
import org.motechproject.openmrs19.resource.EncounterResource;
import org.motechproject.openmrs19.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

@Component
public class EncounterResourceImpl extends BaseResource implements EncounterResource {

    @Autowired
    public EncounterResourceImpl(RestOperations restOperations) {
        super(restOperations);
    }

    @Override
    public Encounter createEncounter(Config config, Encounter encounter) {
        String requestJson = buildGsonWithAdapters().toJson(encounter);
        String responseJson = postForJson(config, requestJson, "/encounter?v=full");
        return (Encounter) JsonUtils.readJson(responseJson, Encounter.class);
    }

    @Override
    public EncounterListResult queryForAllEncountersByPatientId(Config config, String id) {
        String responseJson = getJson(config, "/encounter?patient={id}&v=full", id);
        return (EncounterListResult) JsonUtils.readJson(responseJson, EncounterListResult.class);
    }

    @Override
    public Encounter getEncounterById(Config config, String uuid) {
        String responseJson = getJson(config, "/encounter/{uuid}?v=full", uuid);
        return (Encounter) JsonUtils.readJson(responseJson, Encounter.class);
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

    private Gson buildGsonWithAdapters() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .registerTypeAdapter(Location.class, new Location.LocationSerializer())
                .registerTypeAdapter(Patient.class, new Patient.PatientSerializer())
                .registerTypeAdapter(Person.class, new Person.PersonSerializer())
                .registerTypeAdapter(EncounterType.class, new EncounterType.EncounterTypeSerializer())
                .registerTypeAdapter(Observation.class, new Observation.ObservationSerializer())
                .create();
    }
}
