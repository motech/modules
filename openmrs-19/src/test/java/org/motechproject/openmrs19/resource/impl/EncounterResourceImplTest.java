package org.motechproject.openmrs19.resource.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.motechproject.openmrs19.domain.Concept;
import org.motechproject.openmrs19.domain.Encounter;
import org.motechproject.openmrs19.domain.EncounterListResult;
import org.motechproject.openmrs19.domain.EncounterType;
import org.motechproject.openmrs19.domain.Location;
import org.motechproject.openmrs19.domain.Observation;
import org.motechproject.openmrs19.domain.Patient;
import org.motechproject.openmrs19.domain.Person;
import org.motechproject.openmrs19.exception.HttpException;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class EncounterResourceImplTest extends AbstractResourceImplTest {

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldWriteEncounterCreateJson() throws IOException, HttpException {
        Encounter encounter = getExpectedEncounter();

        EncounterResourceImpl impl = new EncounterResourceImpl(getClient(), getInstance());
        impl.createEncounter(encounter);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(getClient()).postForJson(Mockito.any(URI.class), captor.capture());

        String expectedJson = readJsonFromFile("json/encounter-create.json");
        JsonElement expectedJsonObj = getGson().fromJson(expectedJson, JsonObject.class);
        JsonElement foundJsonObj = getGson().fromJson(captor.getValue(), JsonObject.class);

        assertEquals(expectedJsonObj, foundJsonObj);
    }

    private Encounter getExpectedEncounter() {
        Encounter encounter = new Encounter();
        EncounterType type = new EncounterType("ADULTINITIAL");
        encounter.setEncounterType(type);

        Location loc = new Location();
        loc.setUuid("LLL");
        encounter.setLocation(loc);

        Patient patient = new Patient();
        patient.setUuid("PPP");
        encounter.setPatient(patient);

        Person provider = new Person();
        provider.setUuid("PPR");
        encounter.setProvider(provider);

        Observation obs = new Observation();
        obs.setUuid("OOO");
        Concept concept = new Concept();
        concept.setUuid("CCC");
        obs.setConcept(concept);
        Observation.ObservationValue value = new Observation.ObservationValue("Test Value");
        obs.setValue(value);
        List<Observation> observations = new ArrayList<Observation>();
        observations.add(obs);
        encounter.setObs(observations);

        return encounter;
    }

    @Test
    public void shouldReadEncounterListResult() throws IOException, HttpException {
        String responseJson = readJsonFromFile("json/encounter-by-patient-response.json");

        Mockito.when(getClient().getJson(Mockito.any(URI.class))).thenReturn(responseJson);

        EncounterResourceImpl impl = new EncounterResourceImpl(getClient(), getInstance());
        EncounterListResult result = impl.queryForAllEncountersByPatientId("200");

        assertEquals(asList("446d0bec-5e65-4f25-aacd-ee7da78ec616"), extract(result.getResults(), on(Encounter.class).getUuid()));
    }
}
