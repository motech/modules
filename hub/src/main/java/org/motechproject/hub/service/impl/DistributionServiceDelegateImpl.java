package org.motechproject.hub.service.impl;

import org.motechproject.http.agent.service.HttpAgent;
import org.motechproject.http.agent.service.Method;
import org.motechproject.hub.service.DistributionServiceDelegate;
import org.motechproject.hub.util.HubConstants;
import org.motechproject.server.config.SettingsFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of {@link org.motechproject.hub.service.DistributionServiceDelegate}
 */
@Service(value = "distributionServiceDelegate")
public class DistributionServiceDelegateImpl implements
        DistributionServiceDelegate {

    private HttpAgent httpAgentImpl;

    @Value("${retry.count}")
    private String retryCount;

    /**
     * Sets the number of allowed request retries in case of request error.
     *
     * @param retryCount the retry count to be set
     */
    public void setRetryCount(String retryCount) {
        this.retryCount = retryCount;
    }

    @Value("${retry.interval}")
    private String retryInterval;

    /**
     * Sets the interval between request retries in case of request error.
     *
     * @param retryInterval the retry interval to be set
     */
    public void setRetryInterval(String retryInterval) {
        this.retryInterval = retryInterval;
    }

    private SettingsFacade settingsFacade;

    /**
     * Gets the <code>HttpAgent</code> which is an OSGI service
     * used for sending http requests.
     *
     * @return the http agent
     */
    public HttpAgent getHttpAgentImpl() {
        return httpAgentImpl;
    }

    /**
     * Sets the <code>HttpAgent</code> which is an OSGI service
     * used for sending http requests.
     *
     * @param httpAgentImpl the http agent to be set
     */
    public void setHttpAgentImpl(HttpAgent httpAgentImpl) {
        this.httpAgentImpl = httpAgentImpl;
    }

    /**
     * Creates a new instance of <code>DistributionServiceDelegateImpl</code>, with
     * all fields set to the autowired parameter values.
     *
     * @param httpAgentImpl autowired {@link org.motechproject.http.agent.service.HttpAgent}
     * @param settingsFacade autowired {@link org.motechproject.server.config.SettingsFacade}
     */
    @Autowired
    public DistributionServiceDelegateImpl(HttpAgent httpAgentImpl,
            @Qualifier("hubSettings") final SettingsFacade settingsFacade) {
        this.httpAgentImpl = httpAgentImpl;
        this.settingsFacade = settingsFacade;
    }

    @Override
    public ResponseEntity<String> getContent(String topicUrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> entity = new HttpEntity<String>("parameters",
                headers);
        return (ResponseEntity<String>) httpAgentImpl
                .executeWithReturnTypeSync(topicUrl, entity, Method.POST,
                        Integer.valueOf(retryCount),
                        Long.valueOf(retryInterval));
    }

    @Override
    public void distribute(String callbackUrl, String content,
            MediaType contentType, String topicUrl) {

        String hubBaseUrl = settingsFacade.getProperty("hubBaseUrl");

        Map<String, String> headersMap = new HashMap<String, String>();
        headersMap.put(HubConstants.HEADER_CONTENT_TYPE, contentType.toString());
        headersMap.put(HubConstants.HEADER_LINK, "<" + hubBaseUrl + ">; rel=\"hub\", <"
                + topicUrl + ">; rel=\"self\"");
        httpAgentImpl.execute(callbackUrl, content, Method.POST, headersMap);
    }
}
