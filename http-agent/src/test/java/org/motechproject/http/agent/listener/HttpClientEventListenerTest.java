package org.motechproject.http.agent.listener;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.event.MotechEvent;
import org.motechproject.http.agent.domain.EventDataKeys;
import org.motechproject.http.agent.domain.EventSubjects;
import org.motechproject.http.agent.domain.HTTPActionAudit;
import org.motechproject.http.agent.service.HTTPActionService;
import org.motechproject.http.agent.service.Method;
import org.motechproject.config.SettingsFacade;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HttpClientEventListenerTest {

    @Mock
    RestTemplate restTempate;
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


    @Before
    public void setup() {
        when(settings.getProperty(HttpClientEventListener.HTTP_CONNECT_TIMEOUT)).thenReturn("0");
        when(settings.getProperty(HttpClientEventListener.HTTP_READ_TIMEOUT)).thenReturn("0");
        when(restTempate.getRequestFactory()).thenReturn(httpComponentsClientHttpRequestFactory);

        httpClientEventListener = new HttpClientEventListener(restTempate, settings, httpActionService);

        url = "http://commcare";
        data = "aragorn";
        headers = new HashMap<String, String>();
        headers.put("key1", "value1");
        headers.put("key2", "value2");
        body = "example";
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
        when(restTempate.exchange(eq(url),eq(HttpMethod.POST),eq(request), eq(String.class))).thenReturn((ResponseEntity<String>) responseEntity);;
        httpClientEventListener.handle(motechEvent);
        verify(restTempate).exchange(eq(url),eq(HttpMethod.POST),eq(request), eq(String.class));
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
        when(restTempate.exchange(eq(url),eq(HttpMethod.PUT),eq(request), eq(String.class))).thenReturn((ResponseEntity<String>) responseEntity);
        httpClientEventListener.handle(motechEvent);
        verify(restTempate).exchange(eq(url),eq(HttpMethod.PUT),eq(request), eq(String.class));
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
        when(restTempate.exchange(eq(url),eq(HttpMethod.DELETE),eq(request), eq(String.class))).thenReturn((ResponseEntity<String>) responseEntity);
        httpClientEventListener.handle(motechEvent);
        verify(restTempate).exchange(eq(url),eq(HttpMethod.DELETE),eq(request), eq(String.class));
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
        when(restTempate.exchange(eq(url),eq(HttpMethod.DELETE),eq(request), eq(String.class))).thenReturn((ResponseEntity<String>) responseEntity);

        httpClientEventListener.handle(motechEvent);

        HTTPActionAudit httpActionAudit = new HTTPActionAudit(url, request.toString(), responseEntity.getBody().toString(),
                responseEntity.getStatusCode().toString());
        verify(httpActionService,times(1)).create(eq(httpActionAudit));
    }
}
