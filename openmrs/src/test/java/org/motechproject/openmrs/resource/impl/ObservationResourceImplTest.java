package org.motechproject.openmrs.resource.impl;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.openmrs.domain.ObservationListResult;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.config.ConfigDummyData;
import org.motechproject.openmrs.resource.ObservationResource;
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

public class ObservationResourceImplTest extends AbstractResourceImplTest {

    private static final String OBSERVATION_LIST_RESPONSE_JSON = "json/observation/observation-list-response.json";

    @Mock
    private RestOperations restOperations;

    @Captor
    private ArgumentCaptor<HttpEntity<String>> requestCaptor;

    private ObservationResource observationResource;

    private Config config;

    @Before
    public void setUp() {
        initMocks(this);
        observationResource = new ObservationResourceImpl(restOperations);
        config = ConfigDummyData.prepareConfig("one");
    }

    @Test
    public void shouldQueryForObservationByPatientId() throws Exception {
        String patientId = "OOO";
        URI url = config.toInstancePathWithParams("/obs?patient={uuid}&v=full", patientId);

        when(restOperations.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponse(OBSERVATION_LIST_RESPONSE_JSON));

        ObservationListResult result = observationResource.queryForObservationsByPatientId(config, patientId);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.GET), requestCaptor.capture(), eq(String.class));

        assertThat(result, equalTo(readFromFile(OBSERVATION_LIST_RESPONSE_JSON, ObservationListResult.class)));
        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForGet(config)));
        assertThat(requestCaptor.getValue().getBody(), nullValue());
    }

}
