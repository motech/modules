package org.motechproject.openmrs.resource.impl;

import com.google.gson.JsonObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.config.ConfigDummyData;
import org.motechproject.openmrs.domain.Observation;
import org.motechproject.openmrs.domain.ObservationListResult;
import org.motechproject.openmrs.resource.ObservationResource;
import org.motechproject.openmrs.util.JsonUtils;
import org.motechproject.openmrs.domain.ObservationDummyData;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestOperations;

import java.net.URI;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ObservationResourceImplTest extends AbstractResourceImplTest {

    private static final String OBSERVATION_LIST_RESPONSE_JSON = "json/observation/observation-list-response.json";
    private static final String OBSERVATION_QUERY_RESPONSE_JSON = "json/observation/observation-query-response.json";

    private static final String OBSERVATION_RESPONSE_JSON = "json/observation/observation-response.json";
    private static final String OBSERVATION_WITH_CONCEPT_VALUE_RESPONSE_JSON = "json/observation/observation-with-concept-value-response.json";
    private static final String PREPARE_OBSERVATION_JSON = "json/observation/prepare-observation.json";
    private static final String OBSERVATION_ENCOUNTER_LOOKUP_RESPONSE = "json/observation/observation-encounter-lookup-response.json";

    private static final String OBSERVATION_UUID = "01a4703c-f89f-4316-803b-dc9d08da5a0a0";

    @Mock
    private RestOperations restOperations;
    @Mock
    private HttpClient httpClient;

    @Captor
    private ArgumentCaptor<HttpEntity<String>> requestCaptor;

    private ObservationResource observationResource;

    private Config config;

    @Before
    public void setUp() {
        initMocks(this);
        when(httpClient.getState()).thenReturn(new HttpState());

        observationResource = new ObservationResourceImpl(restOperations, httpClient);
        config = ConfigDummyData.prepareConfig("one");
    }

    @Test
    public void shouldQueryForObservationByPatientId() throws Exception {
        String patientId = "OOO";
        URI url = config.toInstancePathWithParams("/obs?patient={uuid}&v=full", patientId);

        when(restOperations.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponseFromFile(OBSERVATION_LIST_RESPONSE_JSON));

        ObservationListResult result = observationResource.queryForObservationsByPatientId(config, patientId);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.GET), requestCaptor.capture(), eq(String.class));

        assertThat(result, equalTo(readFromFile(OBSERVATION_LIST_RESPONSE_JSON, ObservationListResult.class)));
        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForGet(config)));
        assertThat(requestCaptor.getValue().getBody(), nullValue());
    }

    @Test
    public void shouldQueryForObservationByPatientIdAndConceptId() throws Exception {
        String patientId = "OOO";
        String conceptId = "CCC";
        URI url = config.toInstancePathWithParams("/obs?patient={patientUUID}&concept={conceptUUID}&v=full", patientId, conceptId);

        when(restOperations.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponseFromFile(OBSERVATION_QUERY_RESPONSE_JSON));

        ObservationListResult result = observationResource.getObservationByPatientUUIDAndConceptUUID(config, patientId, conceptId);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.GET), requestCaptor.capture(), eq(String.class));

        assertThat(result, equalTo(readFromFile(OBSERVATION_QUERY_RESPONSE_JSON, ObservationListResult.class)));
        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForGet(config)));
    }

    @Test
    public void shouldQueryForObservationByEncounterIdAndConceptId() throws Exception {
        String encounterId = "EEE";
        String conceptId = "CCC";
        URI url = config.toInstancePathWithParams("/obs?encounter={encounterUUID}&concept={conceptUUID}&v=full", encounterId, conceptId);

        when(restOperations.exchange(eq(url), eq(HttpMethod.GET), requestCaptor.capture(), eq(String.class)))
                .thenReturn(getResponseFromFile(OBSERVATION_ENCOUNTER_LOOKUP_RESPONSE));

        ObservationListResult result = observationResource.getObservationByEncounterUUIDAndConceptUUID(config, encounterId, conceptId);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.GET), requestCaptor.capture(), eq(String.class));

        assertThat(result, equalTo(readFromFile(OBSERVATION_ENCOUNTER_LOOKUP_RESPONSE, ObservationListResult.class)));
        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForGet(config)));
    }

    @Test
    public void shouldCreateObservation() throws Exception {
        String observationJson = prepareObservationJson();
        ObservationFromJSON observation = prepareObservation();

        URI url = config.toInstancePath("/obs");

        when(restOperations.exchange(eq(url), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponseFromFile(OBSERVATION_RESPONSE_JSON));

        Observation created = observationResource.createObservationFromJson(config, observationJson);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.POST), requestCaptor.capture(), eq(String.class));

        compareObservations(created, observation);
    }

    @Test
    public void shouldUpdateObservation() throws Exception {
        String observationJson = prepareObservationJson();
        ObservationFromJSON observation = prepareObservation();

        URI url = config.toInstancePath("/obs/" + OBSERVATION_UUID);

        when(restOperations.exchange(eq(url), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponseFromFile(OBSERVATION_RESPONSE_JSON));

        Observation updated = observationResource.updateObservation(config, OBSERVATION_UUID, observationJson);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.POST), requestCaptor.capture(), eq(String.class));

        compareObservations(updated, observation);
    }

    @Test
    public void shouldGetObservationWithObjectAsValue() throws Exception {
        Observation expected = ObservationDummyData.prepareObservationWithConceptValue();

        URI url = config.toInstancePath("/obs/" + OBSERVATION_UUID + "?v=full");

        when(restOperations.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponseFromFile(OBSERVATION_WITH_CONCEPT_VALUE_RESPONSE_JSON));

        Observation fetched = observationResource.getObservationById(config, OBSERVATION_UUID);
        verify(restOperations).exchange(eq(url), eq(HttpMethod.GET), requestCaptor.capture(), eq(String.class));

        assertEquals(expected, fetched);
    }

    private String prepareObservationJson() throws Exception {
        return readJsonFromFile(PREPARE_OBSERVATION_JSON);
    }

    private ObservationFromJSON prepareObservation() throws Exception {
        return (ObservationFromJSON) readFromFile(PREPARE_OBSERVATION_JSON, ObservationFromJSON.class);
    }

    private void compareObservations(Observation created, ObservationFromJSON expected) throws Exception {
        assertEquals(expected.person, created.getPerson().getUuid());
        assertEquals(expected.concept, created.getConcept().getUuid());
        assertEquals(new DateTime(expected.obsDatetime).toDate(), created.getObsDatetime());
        assertEquals(Double.parseDouble(expected.value), Double.parseDouble(created.getValue().getDisplay()));

        assertEquals(getHeadersForPost(config), requestCaptor.getValue().getHeaders());
        assertEquals(readFromFile(PREPARE_OBSERVATION_JSON, JsonObject.class), JsonUtils.readJson(requestCaptor.getValue().getBody(), JsonObject.class));
    }

    private class ObservationFromJSON {
        private String person;
        private Date obsDatetime;
        private String concept;
        private String encounter;
        private String value;
        private List<String> groupsMembers;
    }
}
