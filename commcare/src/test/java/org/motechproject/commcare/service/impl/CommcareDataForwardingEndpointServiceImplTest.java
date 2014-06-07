package org.motechproject.commcare.service.impl;

import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import org.motechproject.commcare.client.CommCareAPIHttpClient;
import org.motechproject.commcare.domain.CommcareDataForwardingEndpoint;

import java.util.List;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CommcareDataForwardingEndpointServiceImplTest {
    private final int HTTP_CODE_CREATED = 201;
    private final int HTTP_CODE_NOCONTENT = 204;

    private CommcareDataForwardingEndpointServiceImpl dataForwardingEndpointService;

    @Mock
    private CommCareAPIHttpClient commcareHttpClient;

    @Before
    public void setUp() {
        initMocks(this);
        dataForwardingEndpointService = new CommcareDataForwardingEndpointServiceImpl(commcareHttpClient);
    }

    @Test
    public void testAllDataForwardingEndpoints() {
        when(commcareHttpClient.dataForwardingEndpointsRequest()).thenReturn(dataForwardingEndpointsGetResponse());

        List<CommcareDataForwardingEndpoint> dataForwardingEndpoints = dataForwardingEndpointService.getAllDataForwardingEndpoints();

        assertEquals(asList("ccbadc6655b2e7692dccbbd884c14418", "ccbadc6655b2e7692dccbbd884c148b3",
                "ccbadc6655b2e7692dccbbd884c13d60"),
                extract(dataForwardingEndpoints, on(CommcareDataForwardingEndpoint.class).getId()));
    }

    @Test
    public void testCreatingNewDataForwardingRule() {
        when(commcareHttpClient.dataForwardingEndpointUploadRequest(dataForwardingEndpointJson())).thenReturn(HTTP_CODE_CREATED);

        CommcareDataForwardingEndpoint dataForwardingEndpoint = new CommcareDataForwardingEndpoint();
        dataForwardingEndpoint.setDomain("demo");
        dataForwardingEndpoint.setId("ccbadc6655b2e7692dccbbd884c14418");
        dataForwardingEndpoint.setResourceUri("/a/demo/api/v0.4/data-forwarding/ccbadc6655b2e7692dccbbd884c14418/");
        dataForwardingEndpoint.setType("CaseRepeater");
        dataForwardingEndpoint.setUrl("http://www.example.com/case-endpoint/");
        dataForwardingEndpoint.setVersion("2.0");

        boolean creatingNewRuleStatus = dataForwardingEndpointService.createNewDataForwardingRule(dataForwardingEndpoint);

        assertTrue(creatingNewRuleStatus);
    }

    @Test
    public void testUpdatingDataForwardingRule() {
        when(commcareHttpClient.dataForwardingEndpointUpdateRequest(updatedResourceId(), dataForwardingEndpointJsonFull())).thenReturn(HTTP_CODE_NOCONTENT);

        CommcareDataForwardingEndpoint dataForwardingEndpoint = new CommcareDataForwardingEndpoint();
        dataForwardingEndpoint.setDomain("demo");
        dataForwardingEndpoint.setId("ccbadc6655b2e7692dccbbd884c14418");
        dataForwardingEndpoint.setResourceUri("/a/demo/api/v0.4/data-forwarding/ccbadc6655b2e7692dccbbd884c14418/");
        dataForwardingEndpoint.setType("CaseRepeater");
        dataForwardingEndpoint.setUrl("http://www.example.com/case-endpoint/");
        dataForwardingEndpoint.setVersion("2.0");

        boolean updatingDataForwardingEndpointStatus = dataForwardingEndpointService.updateDataForwardingRule(dataForwardingEndpoint);

        assertTrue(updatingDataForwardingEndpointStatus);
    }

    private String dataForwardingEndpointsGetResponse() {
        return "{\"meta\":{\"limit\":20,\"next\":null,\"offset\":0,\"previous\":null,\"total_count\":3},\"objects\":[{\"domain\":\"demo\",\"id\":\"ccbadc6655b2e7692dccbbd884c14418\",\"resource_uri\":\"/a/demo/api/v0.4/data-forwarding/ccbadc6655b2e7692dccbbd884c14418/\",\"type\":\"CaseRepeater\",\"url\":\"http://www.example.com/case-endpoint/\",\"version\":\"2.0\"},{\"domain\":\"demo\",\"id\":\"ccbadc6655b2e7692dccbbd884c148b3\",\"resource_uri\":\"/a/demo/api/v0.4/data-forwarding/ccbadc6655b2e7692dccbbd884c148b3/\",\"type\":\"FormRepeater\",\"url\":\"http://www.example.com/form-endpoint/\",\"version\":null},{\"domain\":\"demo\",\"id\":\"ccbadc6655b2e7692dccbbd884c13d60\",\"resource_uri\":\"/a/demo/api/v0.4/data-forwarding/ccbadc6655b2e7692dccbbd884c13d60/\",\"type\":\"ShortFormRepeater\",\"url\":\"http://www.example.com/short-form-endpoint/\",\"version\":\"2.0\"}]}";
    }

    private String dataForwardingEndpointJsonFull() {
        return "{\"domain\":\"demo\",\"id\":\"ccbadc6655b2e7692dccbbd884c14418\",\"resource_uri\":\"/a/demo/api/v0.4/data-forwarding/ccbadc6655b2e7692dccbbd884c14418/\",\"type\":\"CaseRepeater\",\"url\":\"http://www.example.com/case-endpoint/\",\"version\":\"2.0\"}";
    }

    private String dataForwardingEndpointJson() {
        return "{\"domain\":\"demo\",\"type\":\"CaseRepeater\",\"url\":\"http://www.example.com/case-endpoint/\",\"version\":\"2.0\"}";
    }

    private String updatedResourceId() {
        return "ccbadc6655b2e7692dccbbd884c14418";
    }
}
