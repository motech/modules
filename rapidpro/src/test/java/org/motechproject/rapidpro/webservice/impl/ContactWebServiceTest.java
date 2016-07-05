package org.motechproject.rapidpro.webservice.impl;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.rapidpro.exception.RapidProClientException;
import org.motechproject.rapidpro.exception.WebServiceException;
import org.motechproject.rapidpro.webservice.ContactWebService;
import org.motechproject.rapidpro.webservice.MediaFormat;
import org.motechproject.rapidpro.webservice.RapidProHttpClient;
import org.motechproject.rapidpro.webservice.dto.Contact;
import org.motechproject.rapidpro.webservice.dto.PaginatedResponse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Test class for {@link ContactWebService}
 */
public class ContactWebServiceTest {

    private ContactWebService contactWebService;

    @Mock
    private RapidProHttpClient rapidProHttpClient;

    @Before
    public void setup () {
        initMocks(this);
        contactWebService = new ContactWebServiceImpl(rapidProHttpClient);
    }

    @Test
    public void shouldReturnContact() throws Exception {
        Map<String, String> params = new HashMap<>();
        UUID uuid = UUID.randomUUID();
        params.put("uuid", uuid.toString());
        Contact contact = new Contact();
        contact.setName("Name");
        contact.setUuid(uuid);
        ArrayList<String> urns = new ArrayList<>();
        urns.add("tel:+1234567890");
        contact.setUrns(urns);
        List<Contact> results = new ArrayList<>();
        results.add(contact);
        PaginatedResponse<Contact> paginatedResponse = new PaginatedResponse<>();
        paginatedResponse.setCount("1");
        paginatedResponse.setResults(results);

        String json = new ObjectMapper().writeValueAsString(paginatedResponse);
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        when(rapidProHttpClient.executeGet("/contacts", MediaFormat.JSON, params)).thenReturn(inputStream);

        Contact fromWebservice = contactWebService.getContactByUUID(uuid);

        assertNotNull(fromWebservice);
        assertEquals(fromWebservice.getUuid(), contact.getUuid());
        assertEquals(fromWebservice.getName(), contact.getName());
        assertEquals(fromWebservice.getPhone(), contact.getPhone());
    }

    @Test
    public void shouldReturnNull() throws Exception {
        Map<String, String> params = new HashMap<>();
        UUID uuid = UUID.randomUUID();
        params.put("uuid", uuid.toString());
        List<Contact> results = new ArrayList<>();
        PaginatedResponse<Contact> paginatedResponse = new PaginatedResponse<>();
        paginatedResponse.setCount("0");
        paginatedResponse.setResults(results);

        String json = new ObjectMapper().writeValueAsString(paginatedResponse);
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        when(rapidProHttpClient.executeGet("/contacts", MediaFormat.JSON, params)).thenReturn(inputStream);

        Contact fromWebservice = contactWebService.getContactByUUID(uuid);
        assertNull(fromWebservice);
    }

    @Test(expected = WebServiceException.class)
    public void shouldThrowExceptionMoreThanOneContact() throws Exception {
        Map<String, String> params = new HashMap<>();
        UUID uuid = UUID.randomUUID();
        params.put("uuid", uuid.toString());
        Contact contact = new Contact();
        contact.setName("Name");
        contact.setUuid(uuid);
        contact.setPhone("1234567890");
        ArrayList<String> urns = new ArrayList<>();
        urns.add("tel:+1234567890");
        contact.setUrns(urns);
        List<Contact> results = new ArrayList<>();
        results.add(contact);
        results.add(contact);
        PaginatedResponse<Contact> paginatedResponse = new PaginatedResponse<>();
        paginatedResponse.setCount("2");
        paginatedResponse.setResults(results);

        String json = new ObjectMapper().writeValueAsString(paginatedResponse);
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        when(rapidProHttpClient.executeGet("/contacts", MediaFormat.JSON, params)).thenReturn(inputStream);

        Contact fromWebservice = contactWebService.getContactByUUID(uuid);
    }

    @Test(expected = WebServiceException.class)
    public void shouldThrowExceptionHttpClient() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(rapidProHttpClient.executeGet("/contacts", MediaFormat.JSON, null)).thenThrow(new RapidProClientException("Exception"));
        Contact fromWebservice = contactWebService.getContactByUUID(uuid);
    }

    @Test(expected = WebServiceException.class)
    public void shouldThrowExceptionJsonUtils() throws Exception {
        Map<String, String> params = new HashMap<>();
        UUID uuid = UUID.randomUUID();
        params.put("uuid", uuid.toString());

        String json = "not json";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        when(rapidProHttpClient.executeGet("/contacts", MediaFormat.JSON, params)).thenReturn(inputStream);
        Contact fromWebservice = contactWebService.getContactByUUID(uuid);
    }
}
