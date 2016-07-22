package org.motechproject.openmrs.resource.impl;

import com.google.gson.JsonObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.config.ConfigDummyData;
import org.motechproject.openmrs.domain.Encounter;
import org.motechproject.openmrs.domain.EncounterListResult;
import org.motechproject.openmrs.resource.EncounterResource;
import org.motechproject.openmrs.util.JsonUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestOperations;

import java.net.URI;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class EncounterResourceImplTest extends AbstractResourceImplTest {

    private static final String ENCOUNTER_BY_PATIENT_RESPONSE_V1_9_JSON = "json/encounter/encounter-by-patient-response-v1-9.json";
    private static final String ENCOUNTER_BY_PATIENT_RESPONSE_V1_12_JSON = "json/encounter/encounter-by-patient-response-v1-12.json";
    private static final String ENCOUNTER_RESPONSE_V1_9_JSON = "json/encounter/encounter-response-v1-9.json";
    private static final String ENCOUNTER_RESPONSE_V1_12_JSON = "json/encounter/encounter-response-v1-12.json";
    private static final String ENCOUNTER_LIST_RESULTS_JSON = "json/encounter/encounter-list-results.json";

    private static final String CREATE_ENCOUNTER_JSON = "json/encounter/encounter-create.json";
    private static final String PREPARE_ENCOUNTER_JSON = "json/encounter/encounter-prepare.json";

    private static final String OPENMRS_V1_9 = "1.9";
    private static final String OPENMRS_V1_12 = "1.10+";


    @Mock
    private RestOperations restOperations;

    @Mock
    private HttpClient httpClient;

    @Captor
    private ArgumentCaptor<HttpEntity<String>> requestCaptor;

    private EncounterResource encounterResource;

    private Config config;

    @Before
    public void setUp() {
        initMocks(this);
        when(httpClient.getState()).thenReturn(new HttpState());

        encounterResource = new EncounterResourceImpl(restOperations, httpClient);
        config = ConfigDummyData.prepareConfig("one");
    }

    @Test
    public void shouldCreateEncounterV19() throws Exception {
        createEncounter(OPENMRS_V1_9, ENCOUNTER_RESPONSE_V1_9_JSON);
    }

    @Test
    public void shouldCreateEncounterV112() throws Exception {
        createEncounter(OPENMRS_V1_12, ENCOUNTER_RESPONSE_V1_12_JSON);
    }

    @Test
    public void shouldQueryAllEncountersBYPatientIdV19() throws Exception {
        queryAllEncountersBYPatientId(OPENMRS_V1_9, ENCOUNTER_BY_PATIENT_RESPONSE_V1_9_JSON);
    }

    @Test
    public void shouldQueryAllEncountersBYPatientIdV112() throws Exception {
        queryAllEncountersBYPatientId(OPENMRS_V1_12, ENCOUNTER_BY_PATIENT_RESPONSE_V1_12_JSON);
    }

    public Encounter prepareEncounter() throws Exception {
        return (Encounter) readFromFile(PREPARE_ENCOUNTER_JSON, Encounter.class);
    }

    private void createEncounter(String version, String responseJson) throws Exception {
        config.setOpenMrsVersion(version);

        Encounter encounter = prepareEncounter();
        URI url = config.toInstancePath("/encounter?v=full");

        when(restOperations.exchange(eq(url), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponse(responseJson));

        Encounter created = encounterResource.createEncounter(config, encounter);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.POST), requestCaptor.capture(), eq(String.class));

        assertThat(created, equalTo(encounter));
        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForPost(config)));
        assertThat(JsonUtils.readJson(requestCaptor.getValue().getBody(), JsonObject.class),
                equalTo(readFromFile(CREATE_ENCOUNTER_JSON, JsonObject.class)));
    }

    private void queryAllEncountersBYPatientId(String version, String responseJson) throws Exception {
        config.setOpenMrsVersion(version);

        String patientId = "200";
        URI url = config.toInstancePathWithParams("/encounter?patient={id}&v=full", patientId);

        when(restOperations.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponse(responseJson));

        EncounterListResult result = encounterResource.queryForAllEncountersByPatientId(config, patientId);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.GET), requestCaptor.capture(), eq(String.class));

        assertThat(result, equalTo(readFromFile(ENCOUNTER_LIST_RESULTS_JSON, EncounterListResult.class)));
        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForGet(config)));
        assertThat(requestCaptor.getValue().getBody(), nullValue());
    }
}
