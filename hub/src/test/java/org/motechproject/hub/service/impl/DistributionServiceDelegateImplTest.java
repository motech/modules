package org.motechproject.hub.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.http.HttpException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.http.agent.service.HttpAgent;
import org.motechproject.http.agent.service.Method;
import org.motechproject.config.SettingsFacade;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * This class tests the method inside
 * <code>DistributionServiceDelegateImpl</code> class
 *
 * @author Anuranjan
 *
 */
@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class DistributionServiceDelegateImplTest {

    @Mock
    private HttpAgent httpAgent;

    @Mock
    private SettingsFacade settingsFacade;

    private String hubBaseUrl;
    private String topicUrl;
    private String callbackUrl;
    private String content;
    private MediaType contentType;

    private String retryCount;
    private String retryInterval;

    ResponseEntity<String> response;

    @InjectMocks
    private DistributionServiceDelegateImpl distributionServiceDelegateImpl = new DistributionServiceDelegateImpl(
            httpAgent, settingsFacade);

    @Before
    public void setUp() {

        hubBaseUrl = "hub/base/url/";
        retryCount = "3";
        retryInterval = "1000";
        topicUrl = "topic_url";
        callbackUrl = "callback_url";
        content = "content";
        contentType = MediaType.APPLICATION_XML;

        response = new ResponseEntity<String>("response body", HttpStatus.OK);

        distributionServiceDelegateImpl.setRetryCount(retryCount);
        distributionServiceDelegateImpl.setRetryInterval(retryInterval);

        when(settingsFacade.getProperty("hubBaseUrl")).thenReturn(hubBaseUrl);
    }

    /**
     * Test the method to get the content of an updated topic
     */
    @Test
    public void testGetContent() throws HttpException {
        when(
                httpAgent.executeWithReturnTypeSync(anyString(),
                        (HttpEntity<String>) anyObject(), (Method) any(),
                        (Integer) any(), (Long) any())).thenReturn(
                (ResponseEntity) response);
        ResponseEntity<String> retVal = (ResponseEntity<String>) distributionServiceDelegateImpl
                .getContent(topicUrl);
        assertNotNull(retVal);
        assertEquals("response body", retVal.getBody());
        assertEquals(HttpStatus.OK, retVal.getStatusCode());
        assertNotNull(retVal.getHeaders());
        assertEquals(0, retVal.getHeaders().size());

        verify(httpAgent).executeWithReturnTypeSync(anyString(),
                (HttpEntity<String>) anyObject(), (Method) any(),
                (Integer) any(), (Long) any());

    }

    /**
     * Tests the method to distribute the updated content of the topic to all
     * its subscribers
     */
    @Test
    public void testDistribute() {
        distributionServiceDelegateImpl.distribute(callbackUrl, content,
                contentType, topicUrl);

        verify(httpAgent, Mockito.times(1)).execute(anyString(),
                anyString(), (Method) any(), anyMap());
    }
}
