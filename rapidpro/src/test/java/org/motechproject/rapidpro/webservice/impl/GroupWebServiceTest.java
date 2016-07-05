package org.motechproject.rapidpro.webservice.impl;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.rapidpro.exception.RapidProClientException;
import org.motechproject.rapidpro.exception.WebServiceException;
import org.motechproject.rapidpro.webservice.GroupWebService;
import org.motechproject.rapidpro.webservice.MediaFormat;
import org.motechproject.rapidpro.webservice.RapidProHttpClient;
import org.motechproject.rapidpro.webservice.dto.Group;
import org.motechproject.rapidpro.webservice.dto.PaginatedResponse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Tests for {@link org.motechproject.rapidpro.webservice.GroupWebService}
 */
public class GroupWebServiceTest {

    private GroupWebService groupWebService;

    @Mock
    private RapidProHttpClient rapidProHttpClient;

    @Before
    public void setup () {
        initMocks(this);
        groupWebService = new GroupWebServiceImpl(rapidProHttpClient);
    }

    @Test
    public void shouldReturnGroup() throws Exception {
        String name = "name";
        String uuid = "uuid";
        Group group = new Group();
        group.setName(name);
        group.setUuid(uuid);
        group.setSize(1);

        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        List<Group> results = new ArrayList<>();
        results.add(group);

        PaginatedResponse<Group> response = new PaginatedResponse<>();
        response.setResults(results);
        response.setCount("1");

        String json = new ObjectMapper().writeValueAsString(response);
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        when(rapidProHttpClient.executeGet("/groups", MediaFormat.JSON, params)).thenReturn(inputStream);

        Group fromWebService = groupWebService.getGroupByName(name);

        assertNotNull(fromWebService);
        assertEquals(fromWebService.getName(), name);
        assertEquals(fromWebService.getUuid(), uuid);
    }

    @Test
    public void shouldReturnNull() throws Exception {
        Map<String, String> params = new HashMap<>();
        String name = "name";
        params.put("name", name);
        List<Group> results = new ArrayList<>();
        PaginatedResponse<Group> paginatedResponse = new PaginatedResponse<>();
        paginatedResponse.setCount("0");
        paginatedResponse.setResults(results);

        String json = new ObjectMapper().writeValueAsString(paginatedResponse);
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        when(rapidProHttpClient.executeGet("/groups", MediaFormat.JSON, params)).thenReturn(inputStream);

        Group fromWebservice = groupWebService.getGroupByName(name);
        assertNull(fromWebservice);
    }

    @Test(expected = WebServiceException.class)
    public void shouldThrowExceptionHttpClient() throws Exception {
        String name = "name";
        when(rapidProHttpClient.executeGet("/groups", MediaFormat.JSON, null)).thenThrow(new RapidProClientException("Exception"));
        Group fromWebservice = groupWebService.getGroupByName(name);
    }

    @Test(expected = WebServiceException.class)
    public void shouldThrowExceptionJsonUtils() throws Exception {
        Map<String, String> params = new HashMap<>();
        String name = "name";
        params.put("name", name);

        String json = "not json";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        when(rapidProHttpClient.executeGet("/groups", MediaFormat.JSON, params)).thenReturn(inputStream);
        Group fromWebservice = groupWebService.getGroupByName(name);
    }
}
