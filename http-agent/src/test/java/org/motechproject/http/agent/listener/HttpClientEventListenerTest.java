package org.motechproject.http.agent.listener;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.motechproject.event.MotechEvent;
import org.motechproject.http.agent.constants.SendRequestConstants;
import org.motechproject.http.agent.domain.EventDataKeys;
import org.motechproject.http.agent.domain.EventSubjects;
import org.motechproject.http.agent.domain.HTTPActionAudit;
import org.motechproject.http.agent.factory.HttpComponentsClientHttpRequestFactoryWithAuth;
import org.motechproject.http.agent.service.HTTPActionService;
import org.motechproject.http.agent.service.Method;
import org.motechproject.config.SettingsFacade;

import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("org.apache.http.conn.ssl.*")
@PrepareForTest( {HttpClientEventListener.class})
public class HttpClientEventListenerTest {

    @Mock
    RestTemplate restTemplate;
    @Mock
    HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory;
    @Mock
    SettingsFacade settings;
    @Mock
    HTTPActionService httpActionService;
    @Mock
    ResponseEntity<?> responseEntity;
    @Mock
    HTTPActionAudit httpActionAudit;


    private HttpClientEventListener httpClientEventListener;
    private String url;
    private String data;
    private Map headers;
    private HttpEntity<?> request;
    private String body;
    private String admin;
    private String password;


    @Before
    public void setup() {
        when(settings.getProperty(HttpClientEventListener.HTTP_CONNECT_TIMEOUT)).thenReturn("0");
        when(settings.getProperty(HttpClientEventListener.HTTP_READ_TIMEOUT)).thenReturn("0");
        when(restTemplate.getRequestFactory()).thenReturn(httpComponentsClientHttpRequestFactory);

        httpClientEventListener = new HttpClientEventListener(restTemplate, settings, httpActionService);

        url = "http://commcare";
        data = "aragorn";
        headers = new HashMap<String, String>();
        headers.put("key1", "value1");
        headers.put("key2", "value2");
        body = "example";
        admin = "admin";
        password = "password";
    }

    @Test
    public void shouldReadFromQueueAndMakeAHttpCallForPost() throws IOException {
        MotechEvent motechEvent = new MotechEvent(EventSubjects.HTTP_REQUEST, new HashMap<String, Object>() {{
            put(EventDataKeys.URL, url);
            put(EventDataKeys.DATA, data);
            put(EventDataKeys.METHOD, Method.POST);
            put(EventDataKeys.HEADERS, headers);
        }});
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("key1", "value1");
        httpHeaders.add("key2", "value2");
        request = new HttpEntity<String>(data,httpHeaders);
        responseEntity = new ResponseEntity<String>(body, HttpStatus.OK);
        when(restTemplate.exchange(eq(url),eq(HttpMethod.POST),eq(request), eq(String.class))).thenReturn((ResponseEntity<String>) responseEntity);;
        httpClientEventListener.handle(motechEvent);
        verify(restTemplate).exchange(eq(url),eq(HttpMethod.POST),eq(request), eq(String.class));
    }

    @Test
    public void shouldReadFromQueueAndMakeAHttpCall() throws IOException {
        MotechEvent motechEvent = new MotechEvent(EventSubjects.HTTP_REQUEST, new HashMap<String, Object>() {{
            put(EventDataKeys.URL, url);
            put(EventDataKeys.DATA, data);
            put(EventDataKeys.METHOD, Method.PUT);
            put(EventDataKeys.HEADERS, headers);
        }});
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("key1", "value1");
        httpHeaders.add("key2", "value2");

        request = new HttpEntity<String>(data,httpHeaders);
        responseEntity = new ResponseEntity<String>(body,HttpStatus.OK);
        when(restTemplate.exchange(eq(url),eq(HttpMethod.PUT),eq(request), eq(String.class))).thenReturn((ResponseEntity<String>) responseEntity);
        httpClientEventListener.handle(motechEvent);
        verify(restTemplate).exchange(eq(url),eq(HttpMethod.PUT),eq(request), eq(String.class));
    }

    @Test
    public void shouldReadFromQueueAndMakeAHttpDeleteCall() throws IOException {
        MotechEvent motechEvent = new MotechEvent(EventSubjects.HTTP_REQUEST, new HashMap<String, Object>() {{
            put(EventDataKeys.URL, url);
            put(EventDataKeys.DATA, data);
            put(EventDataKeys.METHOD, Method.DELETE);
            put(EventDataKeys.HEADERS, headers);
        }});
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("key1", "value1");
        httpHeaders.add("key2", "value2");
        request = new HttpEntity<String>(data,httpHeaders);
        responseEntity = new ResponseEntity<String>(body,HttpStatus.OK);
        when(restTemplate.exchange(eq(url),eq(HttpMethod.DELETE),eq(request), eq(String.class))).thenReturn((ResponseEntity<String>) responseEntity);
        httpClientEventListener.handle(motechEvent);
        verify(restTemplate).exchange(eq(url),eq(HttpMethod.DELETE),eq(request), eq(String.class));
    }

    @Test
    public void shouldLogAuditIfRequestAreMade() throws IOException {
        MotechEvent motechEvent = new MotechEvent(EventSubjects.HTTP_REQUEST, new HashMap<String, Object>() {{
            put(EventDataKeys.URL, url);
            put(EventDataKeys.DATA, data);
            put(EventDataKeys.METHOD, Method.DELETE);
            put(EventDataKeys.HEADERS, headers);
        }});
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("key1", "value1");
        httpHeaders.add("key2", "value2");
        request = new HttpEntity<String>(data,httpHeaders);
        responseEntity = new ResponseEntity<String>(body,HttpStatus.OK);
        when(restTemplate.exchange(eq(url),eq(HttpMethod.DELETE),eq(request), eq(String.class))).thenReturn((ResponseEntity<String>) responseEntity);

        httpClientEventListener.handle(motechEvent);

        HTTPActionAudit httpActionAudit = new HTTPActionAudit(url, request.toString(), responseEntity.getBody().toString(),
                responseEntity.getStatusCode().toString());
        verify(httpActionService).create(eq(httpActionAudit));
    }

    @Test
    public void shouldReturnResponse() throws Throwable {
        MotechEvent motechEvent = new MotechEvent(SendRequestConstants.SEND_REQUEST_SUBJECT, new HashMap<String, Object>() {{
            put(SendRequestConstants.URL, url);
            put(SendRequestConstants.BODY_PARAMETERS, data);
            put(SendRequestConstants.USERNAME, admin);
            put(SendRequestConstants.PASSWORD, password);
            put(SendRequestConstants.FOLLOW_REDIRECTS, false);
        }});

        RestTemplate restTemplateMock = mock(RestTemplate.class);
        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = mock(HttpComponentsClientHttpRequestFactory.class);
        SettingsFacade settings = mock(SettingsFacade.class);
        HTTPActionService httpActionService = mock(HTTPActionService.class);

        ResponseEntity<?> exceptedResponseEntity = new ResponseEntity<String>(body, HttpStatus.OK);

        when(settings.getProperty(HttpClientEventListener.HTTP_CONNECT_TIMEOUT)).thenReturn("0");
        when(settings.getProperty(HttpClientEventListener.HTTP_READ_TIMEOUT)).thenReturn("0");
        when(restTemplateMock.getRequestFactory()).thenReturn(httpComponentsClientHttpRequestFactory);

        request = new HttpEntity<String>(data);

        when(restTemplateMock.exchange(eq(url),eq(HttpMethod.POST),eq(request), eq(String.class))).
                thenReturn((ResponseEntity<String>) exceptedResponseEntity);
        whenNew(RestTemplate.class).withParameterTypes(ClientHttpRequestFactory.class).
                withArguments(isA(HttpComponentsClientHttpRequestFactoryWithAuth.class)).thenReturn(restTemplateMock);

        httpClientEventListener = new HttpClientEventListener(restTemplateMock, settings, httpActionService);

        ResponseEntity<?> responseEntity = httpClientEventListener.handleWithUserPasswordAndReturnType(motechEvent);
        assertEquals(responseEntity, exceptedResponseEntity);
    }

    @Test
    public void shouldUseBasicRestTemplate() {

        MotechEvent motechEvent = new MotechEvent(SendRequestConstants.SEND_REQUEST_SUBJECT, new HashMap<String, Object>() {{
            put(SendRequestConstants.URL, url);
            put(SendRequestConstants.BODY_PARAMETERS, data);
            put(SendRequestConstants.USERNAME, null);
            put(SendRequestConstants.PASSWORD, null);
            put(SendRequestConstants.FOLLOW_REDIRECTS, false);
        }});

        RestTemplate restTemplateMock = mock(RestTemplate.class);
        SettingsFacade settings = mock(SettingsFacade.class);
        HTTPActionService httpActionService = mock(HTTPActionService.class);
        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = mock(HttpComponentsClientHttpRequestFactory.class);


        when(settings.getProperty(HttpClientEventListener.HTTP_CONNECT_TIMEOUT)).thenReturn("0");
        when(settings.getProperty(HttpClientEventListener.HTTP_READ_TIMEOUT)).thenReturn("0");
        when(restTemplateMock.getRequestFactory()).thenReturn(httpComponentsClientHttpRequestFactory);

        request = new HttpEntity<String>(data);

        ResponseEntity<?> expectedResponseEntity = restTemplateMock.exchange((String) motechEvent.getParameters().get(SendRequestConstants.URL),
                HttpMethod.POST, request, String.class);

        httpClientEventListener = new HttpClientEventListener(restTemplateMock, settings, httpActionService);

        ResponseEntity<?> responseEntity = httpClientEventListener.handleWithUserPasswordAndReturnType(motechEvent);

        assertEquals(responseEntity, expectedResponseEntity);
    }
}
