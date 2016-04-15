package org.motechproject.openmrs19.resource.impl;

import com.google.gson.JsonElement;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.motechproject.openmrs19.domain.Identifier;
import org.motechproject.openmrs19.domain.IdentifierType;
import org.motechproject.openmrs19.domain.Location;
import org.motechproject.openmrs19.domain.Patient;
import org.motechproject.openmrs19.domain.PatientListResult;
import org.motechproject.openmrs19.domain.Person;
import org.motechproject.openmrs19.exception.HttpException;

import java.io.IOException;
import java.net.URI;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PatientResourceImplTest extends AbstractResourceImplTest {

    private PatientResourceImpl impl;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        impl = new PatientResourceImpl(getClient(), getInstance());
    }

    @Test
    public void shouldCreatePatient() throws HttpException, IOException {
        Patient patient = buildPatient();

        impl.createPatient(patient);

        ArgumentCaptor<String> sentJson = ArgumentCaptor.forClass(String.class);

        Mockito.verify(getClient()).postForJson(Mockito.any(URI.class), sentJson.capture());

        String expectedJson = readJsonFromFile("json/patient-create.json");
        JsonElement expectedJsonObj = stringToJsonElement(expectedJson);
        JsonElement sentJsonObj = stringToJsonElement(sentJson.getValue());

        assertEquals(expectedJsonObj, sentJsonObj);
    }

    private Patient buildPatient() {
        Patient patient = new Patient();
        Person person = new Person();
        person.setUuid("AAA");
        patient.setPerson(person);

        Identifier identifier = new Identifier();
        Location location = new Location();
        location.setUuid("LLL");
        IdentifierType it = new IdentifierType();
        it.setUuid("III");

        identifier.setIdentifier("558");
        identifier.setLocation(location);
        identifier.setIdentifierType(it);

        return patient;
    }

    @Test
    public void shouldQueryForPatient() throws HttpException, IOException {
        Mockito.when(getClient().getJson(Mockito.any(URI.class))).thenReturn(
                readJsonFromFile("json/patient-list-response.json"));

        PatientListResult result = impl.queryForPatient("558");

        assertEquals(asList("PPP"), extract(result.getResults(), on(Patient.class).getUuid()));
    }



    @Test
    public void shouldGetPatientById() throws HttpException, IOException {
        Mockito.when(getClient().getJson(Mockito.any(URI.class))).thenReturn(
                readJsonFromFile("json/patient-response.json"));

        Patient patient = impl.getPatientById("123");

        assertNotNull(patient);
    }

    @Test
    public void shouldFindMotechIdentifierType() throws HttpException, IOException {
        Mockito.when(getClient().getJson(Mockito.any(URI.class))).thenReturn(
                readJsonFromFile("json/patient-identifier-list-response.json"));

        Mockito.when(getInstance().getMotechPatientIdentifierTypeName()).thenReturn("MoTeCH Id");
        String uuid = impl.getMotechPatientIdentifierUuid();

        assertEquals("III", uuid);
    }
}
