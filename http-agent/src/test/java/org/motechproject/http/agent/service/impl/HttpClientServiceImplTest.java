package org.motechproject.http.agent.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.event.MotechEvent;
import org.motechproject.http.agent.components.AsynchronousCall;
import org.motechproject.http.agent.components.SynchronousCall;
import org.motechproject.http.agent.domain.Credentials;
import org.motechproject.http.agent.domain.EventDataKeys;
import org.motechproject.http.agent.service.HttpAgent;
import org.motechproject.http.agent.service.Method;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
public class HttpClientServiceImplTest {

    @Mock
    private AsynchronousCall asynchronousCall;
    @Mock
    private SynchronousCall synchronousCall;

    HttpAgent httpAgent;

    private String url;
    private String data;
    private Map headers;
    private Credentials credentials;

    @Before
    public void setup() {
        initMocks(this);
        httpAgent = new HttpAgentImpl(asynchronousCall, synchronousCall);

        url = "http://someurl.com";
        data = "{ somekey: somevalue }";
        headers = new HashMap<String, String>();
        headers.put("headerKey", "headerValue");
        credentials = new Credentials("Admin", "password");
    }

    @Test
    public void shouldExecutePostRequest() {
        httpAgent.execute(url, data, Method.POST, headers, credentials);

        ArgumentCaptor<MotechEvent> motechEventArgumentCaptor = ArgumentCaptor.forClass(MotechEvent.class);
        verify(asynchronousCall).send(motechEventArgumentCaptor.capture());
        MotechEvent eventMessageSent = motechEventArgumentCaptor.getValue();

        assertEquals(Method.POST, eventMessageSent.getParameters().get(EventDataKeys.METHOD));
        assertEquals(data, (String) eventMessageSent.getParameters().get(EventDataKeys.DATA));
        assertEquals(url, eventMessageSent.getParameters().get(EventDataKeys.URL));
        assertEquals(headers, eventMessageSent.getParameters().get(EventDataKeys.HEADERS));
        assertEquals(credentials.getUsername(), eventMessageSent.getParameters().get(EventDataKeys.USERNAME));
        assertEquals(credentials.getPassword(), eventMessageSent.getParameters().get(EventDataKeys.PASSWORD));
    }

    @Test
    public void shouldExecutePutRequest() {
        httpAgent.execute(url, data, Method.PUT, headers, credentials);

        ArgumentCaptor<MotechEvent> motechEventArgumentCaptor = ArgumentCaptor.forClass(MotechEvent.class);
        verify(asynchronousCall).send(motechEventArgumentCaptor.capture());
        MotechEvent eventMessageSent = motechEventArgumentCaptor.getValue();

        assertEquals(Method.PUT, eventMessageSent.getParameters().get(EventDataKeys.METHOD));
        assertEquals(data, (String) eventMessageSent.getParameters().get(EventDataKeys.DATA));
        assertEquals(url, eventMessageSent.getParameters().get(EventDataKeys.URL));
        assertEquals(headers, eventMessageSent.getParameters().get(EventDataKeys.HEADERS));
        assertEquals(credentials.getUsername(), eventMessageSent.getParameters().get(EventDataKeys.USERNAME));
        assertEquals(credentials.getPassword(), eventMessageSent.getParameters().get(EventDataKeys.PASSWORD));
    }

    @Test
    public void shouldExecuteDeleteRequest() {
        httpAgent.execute(url, data, Method.DELETE, headers, credentials);

        ArgumentCaptor<MotechEvent> motechEventArgumentCaptor = ArgumentCaptor.forClass(MotechEvent.class);
        verify(asynchronousCall).send(motechEventArgumentCaptor.capture());
        MotechEvent eventMessageSent = motechEventArgumentCaptor.getValue();

        assertEquals(Method.DELETE, eventMessageSent.getParameters().get(EventDataKeys.METHOD));
        assertEquals(data, (String) eventMessageSent.getParameters().get(EventDataKeys.DATA));
        assertEquals(url, eventMessageSent.getParameters().get(EventDataKeys.URL));
        assertEquals(headers, eventMessageSent.getParameters().get(EventDataKeys.HEADERS));
        assertEquals(credentials.getUsername(), eventMessageSent.getParameters().get(EventDataKeys.USERNAME));
        assertEquals(credentials.getPassword(), eventMessageSent.getParameters().get(EventDataKeys.PASSWORD));
    }

    @Test
    public void shouldExecuteSynchronousCalls(){
        httpAgent.executeSync(url, data, Method.POST, headers, credentials);

        ArgumentCaptor<MotechEvent> motechEventArgumentCaptor = ArgumentCaptor.forClass(MotechEvent.class);
        verify(synchronousCall).send(motechEventArgumentCaptor.capture());
        MotechEvent eventMessageSent = motechEventArgumentCaptor.getValue();

        assertEquals(Method.POST, eventMessageSent.getParameters().get(EventDataKeys.METHOD));
        assertEquals(data, (String) eventMessageSent.getParameters().get(EventDataKeys.DATA));
        assertEquals(url, eventMessageSent.getParameters().get(EventDataKeys.URL));
        assertEquals(headers, eventMessageSent.getParameters().get(EventDataKeys.HEADERS));
        assertEquals(credentials.getUsername(), eventMessageSent.getParameters().get(EventDataKeys.USERNAME));
        assertEquals(credentials.getPassword(), eventMessageSent.getParameters().get(EventDataKeys.PASSWORD));
    }
}
