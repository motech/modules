package org.motechproject.http.agent.listener;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.event.MotechEvent;
import org.motechproject.http.agent.domain.Credentials;
import org.motechproject.http.agent.domain.EventDataKeys;
import org.motechproject.http.agent.domain.EventSubjects;
import org.motechproject.http.agent.service.Method;
import org.motechproject.server.config.SettingsFacade;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HttpClientEventListenerTest {

    @Mock
    private RestTemplate restTempate;

    @Mock
    HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory;
    @Mock
    SettingsFacade settings;

    private HttpClientEventListener httpClientEventListener;

    private String url;
    private String data;
    private Map headers;
    private Credentials credentials;

    @Before
    public void setup() {
        when(settings.getProperty(HttpClientEventListener.HTTP_CONNECT_TIMEOUT)).thenReturn("0");
        when(settings.getProperty(HttpClientEventListener.HTTP_READ_TIMEOUT)).thenReturn("0");
        when(restTempate.getRequestFactory()).thenReturn(httpComponentsClientHttpRequestFactory);
        httpClientEventListener = new HttpClientEventListener(restTempate, settings);

        url = "http://commcare";
        data = "aragorn";
        headers = new HashMap<String, String>();
        headers.put("key1", "value1");
        headers.put("key2", "value2");
        credentials = new Credentials("Admin", "password");
    }

    @Test
    public void shouldReadFromQueueAndMakeAHttpCallForPost() throws IOException {
        MotechEvent motechEvent = new MotechEvent(EventSubjects.HTTP_REQUEST, new HashMap<String, Object>() {{
            put(EventDataKeys.URL, url);
            put(EventDataKeys.DATA, data);
            put(EventDataKeys.METHOD, Method.POST);
            put(EventDataKeys.HEADERS, headers);
        }});

        httpClientEventListener.handle(motechEvent);

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(restTempate).postForLocation(eq(url), captor.capture());
        assertRequestObject(data, headers, captor.getValue());
    }

    @Test
    public void shouldReadFromQueueAndMakeAHttpCall() throws IOException {
        MotechEvent motechEvent = new MotechEvent(EventSubjects.HTTP_REQUEST, new HashMap<String, Object>() {{
            put(EventDataKeys.URL, url);
            put(EventDataKeys.DATA, data);
            put(EventDataKeys.METHOD, Method.PUT);
            put(EventDataKeys.HEADERS, headers);
        }});

        httpClientEventListener.handle(motechEvent);

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(restTempate).put(eq(url), captor.capture());
        assertRequestObject(data, headers, captor.getValue());
    }

    @Test
    public void shouldReadFromQueueAndMakeAHttpDeleteCall() throws IOException {
        MotechEvent motechEvent = new MotechEvent(EventSubjects.HTTP_REQUEST, new HashMap<String, Object>() {{
            put(EventDataKeys.URL, url);
            put(EventDataKeys.DATA, data);
            put(EventDataKeys.METHOD, Method.DELETE);
            put(EventDataKeys.HEADERS, headers);
        }});

        httpClientEventListener.handle(motechEvent);
        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(restTempate).delete(eq(url), captor.capture());
        assertRequestObject(data, headers, captor.getValue());
    }

    private void assertRequestObject(String expectedData, Map expectedHeaders, Object requestObject) {
        assertTrue(requestObject instanceof HttpEntity);
        assertEquals(expectedData, ((HttpEntity) requestObject).getBody());
        assertEquals(expectedHeaders, ((HttpEntity) requestObject).getHeaders().toSingleValueMap());
    }
}
