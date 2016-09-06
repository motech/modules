package org.motechproject.rapidpro.webservice.impl;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.rapidpro.exception.RapidProClientException;
import org.motechproject.rapidpro.exception.WebServiceException;
import org.motechproject.rapidpro.util.JsonUtils;
import org.motechproject.rapidpro.webservice.FlowRunWebService;
import org.motechproject.rapidpro.webservice.MediaFormat;
import org.motechproject.rapidpro.webservice.RapidProHttpClient;
import org.motechproject.rapidpro.webservice.dto.FlowRunRequest;
import org.motechproject.rapidpro.webservice.dto.FlowRunResponse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Test class for {@link FlowRunWebService}
 */
public class FlowRunWebServiceTest {

    private FlowRunWebService flowRunWebService;

    @Mock
    private RapidProHttpClient rapidProHttpClient;

    @Before
    public void setup() {
        initMocks(this);
        this.flowRunWebService = new FlowRunWebServiceImpl(rapidProHttpClient);
    }

    @Test
    public void shouldReturnResponse() throws Exception {
        FlowRunRequest.FlowRunRequestBuilder builder = new FlowRunRequest.FlowRunRequestBuilder(UUID.randomUUID())
                .addContact(UUID.randomUUID());

        FlowRunRequest runRequest = builder.build();
        byte[] body = JsonUtils.toByteArray(runRequest);

        FlowRunResponse runResponse = new FlowRunResponse();
        UUID uuid = UUID.randomUUID();
        UUID contactUUID = UUID.randomUUID();
        runResponse.setFlowUUID(uuid);
        runResponse.setContact(contactUUID);

        List<FlowRunResponse> responses = new ArrayList<>();
        responses.add(runResponse);

        String json = new ObjectMapper().writeValueAsString(responses);
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());

        when(rapidProHttpClient.executePost("/runs", body, MediaFormat.JSON, MediaFormat.JSON)).thenReturn(inputStream);
        List<FlowRunResponse> fromWebService = flowRunWebService.startFlowRuns(runRequest);
        assertEquals(fromWebService.size(), 1);
        assertEquals(fromWebService.get(0).getContact(), contactUUID);
        assertEquals(fromWebService.get(0).getFlowUUID(), uuid);
    }

    @Test(expected = WebServiceException.class)
    public void shouldThrowExceptionHttpClient() throws Exception {
        FlowRunRequest.FlowRunRequestBuilder builder = new FlowRunRequest.FlowRunRequestBuilder(UUID.randomUUID())
                .addContact(UUID.randomUUID());

        FlowRunRequest runRequest = builder.build();
        byte[] body = JsonUtils.toByteArray(runRequest);

        when(rapidProHttpClient.executePost("/runs", body, MediaFormat.JSON, MediaFormat.JSON)).thenThrow(new RapidProClientException("Exception"));
        flowRunWebService.startFlowRuns(runRequest);
    }

    @Test(expected = WebServiceException.class)
    public void shouldThrowExceptionJsonUtils() throws Exception {
        FlowRunRequest.FlowRunRequestBuilder builder = new FlowRunRequest.FlowRunRequestBuilder(UUID.randomUUID())
                .addContact(UUID.randomUUID());

        FlowRunRequest runRequest = builder.build();
        String json = "not json";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        when(rapidProHttpClient.executeGet("/runs", MediaFormat.JSON, null)).thenReturn(inputStream);
        flowRunWebService.startFlowRuns(runRequest);
    }

}
