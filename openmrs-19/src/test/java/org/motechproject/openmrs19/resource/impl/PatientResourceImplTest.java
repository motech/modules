package org.motechproject.openmrs19.resource.impl;

import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.motechproject.openmrs19.domain.Identifier;
import org.motechproject.openmrs19.domain.Patient;
import org.motechproject.openmrs19.domain.PatientListResult;
import org.mockito.Captor;
import org.mockito.Mock;
import org.motechproject.openmrs19.config.Config;
import org.motechproject.openmrs19.config.ConfigDummyData;
import org.motechproject.openmrs19.resource.PatientResource;
import org.motechproject.openmrs19.util.JsonUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestOperations;

import java.net.URI;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PatientResourceImplTest extends AbstractResourceImplTest {

    private static final String PATIENT_IDENTIFIER_LIST_RESPONSE_JSON = "json/patient-identifier-list-response.json";
    private static final String PATIENT_LIST_RESPONSE_JSON = "json/patient-list-response.json";
    private static final String PATIENT_RESPONSE_JSON = "json/patient-response.json";
    private static final String CREATE_PATIENT_JSON = "json/patient-create.json";
    private static final String UPDATE_PATIENT_IDENTIFIER_JSON = "json/patient-identifier-update.json";

    @Mock
    private RestOperations restOperations;

    @Captor
    private ArgumentCaptor<HttpEntity<String>> requestCaptor;

    private PatientResource patientResource;

    private Config config;

    @Before
    public void setUp() {
        initMocks(this);
        patientResource = new PatientResourceImpl(restOperations);
        config = ConfigDummyData.prepareConfig("one");
    }

    @Test
    public void shouldCreatePatient() throws Exception {
        Patient patient = preparePatient();
        URI url = config.toInstancePath("/patient");

        when(restOperations.exchange(eq(url), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponse(PATIENT_RESPONSE_JSON));

        Patient created = patientResource.createPatient(config, patient);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.POST), requestCaptor.capture(), eq(String.class));

        assertThat(created, equalTo(patient));
        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForPost(config)));
        assertThat(JsonUtils.readJson(requestCaptor.getValue().getBody(), JsonObject.class),
                equalTo(readFromFile(CREATE_PATIENT_JSON, JsonObject.class)));
    }

    @Test
    public void shouldQueryForPatient() throws Exception {
        String patientId = "558";
        URI url = config.toInstancePathWithParams("/patient?q={motechId}", patientId);

        when(restOperations.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponse(PATIENT_LIST_RESPONSE_JSON));

        PatientListResult result = patientResource.queryForPatient(config, patientId);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.GET), requestCaptor.capture(), eq(String.class));

        assertThat(result, equalTo(readFromFile(PATIENT_LIST_RESPONSE_JSON, PatientListResult.class)));
        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForGet(config)));
        assertThat(requestCaptor.getValue().getBody(), nullValue());
    }

    @Test
    public void shouldGetPatientById() throws Exception {
        String patientId = "123";
        URI url = config.toInstancePathWithParams("/patient/{uuid}?v=full", patientId);

        when(restOperations.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponse(PATIENT_RESPONSE_JSON));

        Patient patient = patientResource.getPatientById(config, patientId);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.GET), requestCaptor.capture(), eq(String.class));

        assertThat(patient, equalTo(readFromFile(PATIENT_RESPONSE_JSON, Patient.class)));
        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForGet(config)));
        assertThat(requestCaptor.getValue().getBody(), nullValue());
    }

    @Test
    public void shouldGetMotechPatientIdentifierUuid() throws Exception {
        URI url = config.toInstancePath("/patientidentifiertype?v=full");

        when(restOperations.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponse(PATIENT_IDENTIFIER_LIST_RESPONSE_JSON));

        String uuid = patientResource.getMotechPatientIdentifierUuid(config);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.GET), requestCaptor.capture(), eq(String.class));

        assertThat(uuid, equalTo("III"));
        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForGet(config)));
        assertThat(requestCaptor.getValue().getBody(), nullValue());
    }

    @Test
    public void shouldUpdatePatientIdentifiers() throws Exception {
        Patient patient = preparePatient();
        String patientId = patient.getUuid();
        String identifierId = patient.getIdentifiers().get(0).getUuid();
        URI url = config.toInstancePathWithParams("/patient/{uuid}/identifier/{identifierId}", patientId, identifierId);

        when(restOperations.exchange(eq(url), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponse(PATIENT_RESPONSE_JSON));

        patient.getIdentifiers().get(0).setIdentifier("1000");

        patientResource.updatePatientIdentifier(config, patient.getUuid(), patient.getIdentifiers().get(0));

        verify(restOperations).exchange(eq(url), eq(HttpMethod.POST), requestCaptor.capture(), eq(String.class));

        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForPost(config)));
        assertEquals(JsonUtils.readJson(requestCaptor.getValue().getBody(), Identifier.class), readFromFile(UPDATE_PATIENT_IDENTIFIER_JSON, Identifier.class));
    }

    private Patient preparePatient() throws Exception {
        return (Patient) readFromFile(PATIENT_RESPONSE_JSON, Patient.class);
    }
}
