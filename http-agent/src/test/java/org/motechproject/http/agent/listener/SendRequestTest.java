package org.motechproject.http.agent.listener;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.config.SettingsFacade;
import org.motechproject.event.MotechEvent;
import org.motechproject.http.agent.constants.SendRequestConstants;
import org.motechproject.http.agent.domain.HTTPActionAudit;
import org.motechproject.http.agent.factory.HttpComponentsClientHttpRequestFactoryWithAuth;
import org.motechproject.http.agent.service.HTTPActionService;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.whenNew;


@RunWith(PowerMockRunner.class)
@PowerMockIgnore("org.apache.http.conn.ssl.*")
@PrepareForTest( {HttpClientEventListener.class})
public class SendRequestTest {

    private HttpClientEventListener httpClientEventListener;
    private HttpEntity<?> request;

    @Test
    public void shouldReturnResponseAndAddHttpActionAudit() throws Throwable {
        MotechEvent motechEvent = new MotechEvent(SendRequestConstants.SEND_REQUEST_SUBJECT, new HashMap<String, Object>() {{
            put(SendRequestConstants.URL, "http://commcare.com");
            put(SendRequestConstants.DATA, "aragorn");
            put(SendRequestConstants.USERNAME, "admin");
            put(SendRequestConstants.PASSWORD, "password");
            put(SendRequestConstants.REDIRECTION_ABILTY, true);
        }});
        RestTemplate restTemplateMock = mock(RestTemplate.class);
        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = mock(HttpComponentsClientHttpRequestFactory.class);
        SettingsFacade settings = mock(SettingsFacade.class);
        HTTPActionService httpActionService = mock(HTTPActionService.class);
        ResponseEntity<?> exceptedResponseEntity;
        ResponseEntity<?> responseEntity;
        when(settings.getProperty(HttpClientEventListener.HTTP_CONNECT_TIMEOUT)).thenReturn("0");
        when(settings.getProperty(HttpClientEventListener.HTTP_READ_TIMEOUT)).thenReturn("0");
        when(restTemplateMock.getRequestFactory()).thenReturn(httpComponentsClientHttpRequestFactory);

        request = new HttpEntity<String>("aragorn");
        exceptedResponseEntity = new ResponseEntity<String>("example", HttpStatus.OK);
        when(restTemplateMock.exchange(eq("http://commcare.com"),eq(HttpMethod.POST),eq(request), eq(String.class))).
                thenReturn((ResponseEntity<String>) exceptedResponseEntity);

        whenNew(RestTemplate.class).withParameterTypes(ClientHttpRequestFactory.class).
                withArguments(isA(HttpComponentsClientHttpRequestFactoryWithAuth.class)).thenReturn(restTemplateMock);

        httpClientEventListener = new HttpClientEventListener(restTemplateMock, settings, httpActionService);
        responseEntity = httpClientEventListener.handleWithUserPasswordAndReturnType(motechEvent);
        assertEquals(responseEntity, exceptedResponseEntity);
        HTTPActionAudit httpActionAudit = new HTTPActionAudit("http://commcare.com", request.toString(), exceptedResponseEntity.getBody().toString(),
                exceptedResponseEntity.getStatusCode().toString());
        verify(httpActionService).create(eq(httpActionAudit));
    }
}
