package org.motechproject.hub.service.impl;

import java.util.HashMap;
import java.util.Map;

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

/**
 * This class implements the methods in the interface
 * <code>DistributionServiceDelegate</code>
 *
 * @author Anuranjan
 *
 */
@Service(value = "distributionServiceDelegate")
public class DistributionServiceDelegateImpl implements
        DistributionServiceDelegate {

    private HttpAgent httpAgentImpl;

    @Value("${retry.count}")
    private String retryCount;

    public void setRetryCount(String retryCount) {
        this.retryCount = retryCount;
    }

    @Value("${retry.interval}")
    private String retryInterval;

    public void setRetryInterval(String retryInterval) {
        this.retryInterval = retryInterval;
    }

    private SettingsFacade settingsFacade;

    public HttpAgent getHttpAgentImpl() {
        return httpAgentImpl;
    }

    public void setHttpAgentImpl(HttpAgent httpAgentImpl) {
        this.httpAgentImpl = httpAgentImpl;
    }

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
