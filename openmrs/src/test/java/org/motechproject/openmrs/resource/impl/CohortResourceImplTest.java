package org.motechproject.openmrs.resource.impl;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.http.client.utils.URIBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.config.ConfigDummyData;
import org.motechproject.openmrs.domain.CohortQueryReport;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestOperations;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CohortResourceImplTest extends AbstractResourceImplTest {

    private static final String GET_COHORT_QUERY_REPORT_RESPONSE_JSON = "json/cohort/get-cohort-query-report-response.json";

    @Mock
    private RestOperations restOperations;

    @Mock
    private HttpClient httpClient;

    @Captor
    private ArgumentCaptor<HttpEntity<String>> requestCaptor;

    private CohortResourceImpl cohortResource;

    private Config config;

    @Before
    public void setUp() {
        initMocks(this);
        when(httpClient.getState()).thenReturn(new HttpState());

        cohortResource = new CohortResourceImpl(restOperations, httpClient);
        config = ConfigDummyData.prepareConfig("one");
    }

    @Test
    public void shouldGetCohortQueryReportWithoutParameters() throws Exception {
        getCohortQueryReport(false);
    }

    @Test
    public void shouldGetCohortQueryReportWithGivenParameters() throws Exception {
        getCohortQueryReport(true);
    }

    public void getCohortQueryReport(boolean withParameters) throws Exception {
        CohortQueryReport cohortQueryReport = buildCohortQueryReport();
        URI url = config.toInstancePathWithParams("/reportingrest/cohort/{uuid}", cohortQueryReport.getUuid());

        Map<String, String> parameters = new HashMap<>();
        if (withParameters) {
            parameters.put("param1", "value");
            parameters.put("param2", "value");

            url = addParametersToPath(url, parameters);
        }

        when(restOperations.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponseFromFile(GET_COHORT_QUERY_REPORT_RESPONSE_JSON));

        CohortQueryReport created = cohortResource.getCohortQueryReport(config, cohortQueryReport.getUuid(), parameters);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.GET), requestCaptor.capture(), eq(String.class));

        assertEquals(cohortQueryReport, created);
        assertEquals(getHeadersForGet(config), requestCaptor.getValue().getHeaders());
    }

    private CohortQueryReport buildCohortQueryReport() throws Exception {
        return (CohortQueryReport) readFromFile(GET_COHORT_QUERY_REPORT_RESPONSE_JSON, CohortQueryReport.class);
    }

    private URI addParametersToPath(URI url, Map<String, String> parameters) throws Exception {
        URI urlWithParameters = new URIBuilder(url).build();
        for (Map.Entry<String, String> parameter : parameters.entrySet()) {
            urlWithParameters = new URIBuilder(urlWithParameters).addParameter(parameter.getKey(), parameter.getValue()).build();
        }

        return urlWithParameters;
    }
}
