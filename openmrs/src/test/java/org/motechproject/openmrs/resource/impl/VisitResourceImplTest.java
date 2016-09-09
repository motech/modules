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
import org.motechproject.openmrs.domain.Visit;
import org.motechproject.openmrs.util.JsonUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestOperations;

import java.net.URI;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class VisitResourceImplTest extends AbstractResourceImplTest {

    private static final String VISIT_RESPONSE = "json/visit/visit-response.json";

    private static final String CREATE_VISIT_JSON = "json/visit/visit-create.json";
    private static final String PREPARE_VISIT_JSON = "json/visit/visit-prepare.json";

    @Mock
    private RestOperations restOperations;

    @Mock
    private HttpClient httpClient;

    @Captor
    private ArgumentCaptor<HttpEntity<String>> requestCaptor;

    private VisitResourceImpl visitResource;

    private Config config;

    @Before
    public void setUp() {
        initMocks(this);
        when(httpClient.getState()).thenReturn(new HttpState());

        visitResource = new VisitResourceImpl(restOperations, httpClient);
        config = ConfigDummyData.prepareConfig("one");
    }

    @Test
    public void shouldCreateVisit() throws Exception {
        Visit visit = prepareVisit();
        URI url = config.toInstancePath("/visit");

        when(restOperations.exchange(eq(url), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponseFromFile(VISIT_RESPONSE));

        Visit created = visitResource.createVisit(config, visit);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.POST), requestCaptor.capture(), eq(String.class));

        assertThat(created.getStartDatetime(), equalTo(visit.getStartDatetime()));
        assertThat(created.getStopDatetime(), equalTo(visit.getStopDatetime()));
        assertThat(created.getPatient().getUuid(), equalTo(visit.getPatient().getUuid()));
        assertThat(created.getVisitType().getUuid(), equalTo(visit.getVisitType().getUuid()));

        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForPost(config)));
        assertThat(JsonUtils.readJson(requestCaptor.getValue().getBody(), JsonObject.class),
                equalTo(readFromFile(CREATE_VISIT_JSON, JsonObject.class)));
    }

    private Visit prepareVisit() throws Exception {
        return (Visit) readFromFile(PREPARE_VISIT_JSON, Visit.class);
    }
}
