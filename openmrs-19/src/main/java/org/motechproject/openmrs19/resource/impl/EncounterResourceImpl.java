package org.motechproject.openmrs19.resource.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.motechproject.openmrs19.OpenMrsInstance;
import org.motechproject.openmrs19.domain.Encounter;
import org.motechproject.openmrs19.domain.EncounterListResult;
import org.motechproject.openmrs19.domain.EncounterType;
import org.motechproject.openmrs19.domain.Location;
import org.motechproject.openmrs19.domain.Observation;
import org.motechproject.openmrs19.domain.Patient;
import org.motechproject.openmrs19.domain.Person;
import org.motechproject.openmrs19.exception.HttpException;
import org.motechproject.openmrs19.resource.EncounterResource;
import org.motechproject.openmrs19.rest.RestClient;
import org.motechproject.openmrs19.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EncounterResourceImpl implements EncounterResource {

    private final RestClient restClient;
    private final OpenMrsInstance openmrsInstance;

    @Autowired
    public EncounterResourceImpl(RestClient restClient, OpenMrsInstance openmrsInstance) {
        this.restClient = restClient;
        this.openmrsInstance = openmrsInstance;
    }

    @Override
    public Encounter createEncounter(Encounter encounter) throws HttpException {
        Gson gson = new GsonBuilder().registerTypeAdapter(Location.class, new Location.LocationSerializer())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .registerTypeAdapter(Patient.class, new Patient.PatientSerializer())
                .registerTypeAdapter(Person.class, new Person.PersonSerializer())
                .registerTypeAdapter(EncounterType.class, new EncounterType.EncounterTypeSerializer())
                .registerTypeAdapter(Observation.class, new Observation.ObservationSerializer()).create();

        String requestJson = gson.toJson(encounter);

        String responseJson = restClient.postForJson(openmrsInstance.toInstancePath("/encounter?v=full"), requestJson);

        return (Encounter) JsonUtils.readJson(responseJson, Encounter.class);
    }

    @Override
    public EncounterListResult queryForAllEncountersByPatientId(String id) throws HttpException {
        String responseJson = restClient.getJson(openmrsInstance.toInstancePathWithParams(
                "/encounter?patient={id}&v=full", id));

        EncounterListResult result = (EncounterListResult) JsonUtils.readJson(responseJson,
                EncounterListResult.class);

        return result;
    }

    @Override
    public Encounter getEncounterById(String uuid) throws HttpException {
        String responseJson = restClient.getJson(openmrsInstance.toInstancePathWithParams("/encounter/{uuid}?v=full",
                uuid));
        return (Encounter) JsonUtils.readJson(responseJson, Encounter.class);
    }

    @Override
    public EncounterType createEncounterType(EncounterType encounterType) throws HttpException {
        Gson gson = new GsonBuilder().create();
        String requestJson = gson.toJson(encounterType, EncounterType.class);
        String responseJson = restClient.postForJson(openmrsInstance.toInstancePath("/encountertype"), requestJson);
        return (EncounterType) JsonUtils.readJson(responseJson, EncounterType.class);
    }

    @Override
    public EncounterType getEncounterTypeByUuid(String uuid) throws HttpException {
        String responseJson = restClient.getJson(openmrsInstance.toInstancePathWithParams("/encountertype/{uuid}", uuid));
        return (EncounterType) JsonUtils.readJson(responseJson, EncounterType.class);
    }

    @Override
    public void deleteEncounterType(String uuid) throws HttpException {
        restClient.delete(openmrsInstance.toInstancePathWithParams("/encountertype/{uuid}?purge", uuid));
    }

    @Override
    public void deleteEncounter(String uuid) throws HttpException {
        restClient.delete(openmrsInstance.toInstancePathWithParams("/encounter/{uuid}?purge", uuid));
    }
}
