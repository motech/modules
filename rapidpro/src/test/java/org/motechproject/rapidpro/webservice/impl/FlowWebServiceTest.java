package org.motechproject.rapidpro.webservice.impl;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.rapidpro.exception.RapidProClientException;
import org.motechproject.rapidpro.exception.WebServiceException;
import org.motechproject.rapidpro.webservice.FlowWebService;
import org.motechproject.rapidpro.webservice.MediaFormat;
import org.motechproject.rapidpro.webservice.RapidProHttpClient;
import org.motechproject.rapidpro.webservice.dto.Flow;
import org.motechproject.rapidpro.webservice.dto.PaginatedResponse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Test Class for {@link org.motechproject.rapidpro.webservice.FlowWebService}
 */
public class FlowWebServiceTest {

    private FlowWebService flowWebService;

    @Mock
    private RapidProHttpClient rapidProHttpClient;

    @Before
    public void setup () {
        initMocks(this);
        flowWebService = new FlowWebServiceImpl(rapidProHttpClient);
    }

    @Test
    public void shouldReturnFlowByName() throws Exception {
        List<Flow> results = new ArrayList<>();
        String name = "name";
        UUID uuid = UUID.randomUUID();

        Flow flow = new Flow();
        flow.setName(name);
        flow.setUuid(uuid);
        results.add(flow);

        PaginatedResponse<Flow> paginatedResponse = new PaginatedResponse<>();
        paginatedResponse.setCount("1");
        paginatedResponse.setResults(results);

        String json = new ObjectMapper().writeValueAsString(paginatedResponse);
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        when(rapidProHttpClient.executeGet("/flows", MediaFormat.JSON, null)).thenReturn(inputStream);

        Flow fromWebservice = flowWebService.getFlow(name);
        assertEquals(fromWebservice.getName(), name);
        assertEquals(fromWebservice.getUuid(), uuid);
    }

    @Test
    public void shouldReturnFlowByUUID() throws Exception {
        List<Flow> results = new ArrayList<>();
        String name = "name";
        UUID uuid = UUID.randomUUID();

        Flow flow = new Flow();
        flow.setName(name);
        flow.setUuid(uuid);
        results.add(flow);

        PaginatedResponse<Flow> paginatedResponse = new PaginatedResponse<>();
        paginatedResponse.setCount("1");
        paginatedResponse.setResults(results);

        String json = new ObjectMapper().writeValueAsString(paginatedResponse);
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        when(rapidProHttpClient.executeGet("/flows", MediaFormat.JSON, null)).thenReturn(inputStream);

        Flow fromWebservice = flowWebService.getFlow(uuid);
        assertEquals(fromWebservice.getName(), name);
        assertEquals(fromWebservice.getUuid(), uuid);
    }

    @Test
    public void shouldReturnNull() throws Exception {
        List<Flow> results = new ArrayList<>();
        String name = "name";
        UUID uuid = UUID.randomUUID();
        UUID otherUUID = UUID.randomUUID();

        Flow flow = new Flow();
        flow.setName(name);
        flow.setUuid(uuid);
        results.add(flow);

        PaginatedResponse<Flow> paginatedResponse = new PaginatedResponse<>();
        paginatedResponse.setCount("1");
        paginatedResponse.setResults(results);

        String json = new ObjectMapper().writeValueAsString(paginatedResponse);
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        when(rapidProHttpClient.executeGet("/flows", MediaFormat.JSON, null)).thenReturn(inputStream);

        Flow fromWebservice = flowWebService.getFlow(otherUUID);
        assertNull(fromWebservice);
    }

    @Test(expected = WebServiceException.class)
    public void shouldThrowExceptionMoreThanOne() throws Exception {
        List<Flow> results = new ArrayList<>();
        String name = "name";
        UUID uuid = UUID.randomUUID();

        Flow flow = new Flow();
        flow.setName(name);
        flow.setUuid(uuid);
        results.add(flow);

        Flow flow2 = new Flow();
        flow2.setName(name);
        flow2.setUuid(UUID.randomUUID());
        results.add(flow2);

        PaginatedResponse<Flow> paginatedResponse = new PaginatedResponse<>();
        paginatedResponse.setCount("1");
        paginatedResponse.setResults(results);

        String json = new ObjectMapper().writeValueAsString(paginatedResponse);
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        when(rapidProHttpClient.executeGet("/flows", MediaFormat.JSON, null)).thenReturn(inputStream);

        flowWebService.getFlow(name);
    }

    @Test(expected = WebServiceException.class)
    public void shouldThrowExceptionHttpClient() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(rapidProHttpClient.executeGet("/flows", MediaFormat.JSON, null)).thenThrow(new RapidProClientException("Exception"));
        flowWebService.getFlow(uuid);
    }

    @Test(expected = WebServiceException.class)
    public void shouldThrowExceptionJsonUtils() throws Exception {
        String json = "not json";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        when(rapidProHttpClient.executeGet("/flows", MediaFormat.JSON, null)).thenReturn(inputStream);
        flowWebService.getFlow(UUID.randomUUID());
    }
}
