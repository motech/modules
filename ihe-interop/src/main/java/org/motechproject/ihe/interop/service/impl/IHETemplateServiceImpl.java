package org.motechproject.ihe.interop.service.impl;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.motechproject.ihe.interop.domain.HL7Recipient;
import org.motechproject.ihe.interop.service.IHETemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Implementation of {@link IHETemplateService}.
 */
@Service("iheTemplateService")
public class IHETemplateServiceImpl implements IHETemplateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IHETemplateServiceImpl.class);

    private PostMethod post;

    private HttpClient client;

    @Override
    public void sendTemplateToRecipientUrl(String url, String template) throws IOException {

        setConnectionParameters(url, template);
        sendTemplate();
    }

    @Override
    public void sendTemplateToRecipientUrlWithBasicAuthentication(HL7Recipient recipient, String template) throws IOException {

        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(recipient.getRecipientUsername(),
                recipient.getRecipientPassword());

        setConnectionParameters(recipient.getRecipientUrl(), template);
        client.getParams().setAuthenticationPreemptive(true);
        client.getState().setCredentials(AuthScope.ANY, credentials);

        sendTemplate();
    }

    private void setConnectionParameters(String url, String template) throws IOException {

        post = new PostMethod(url);
        post.setRequestEntity(new StringRequestEntity(template, "application/xml", "utf-8"));

        client = new HttpClient();
    }

    private void sendTemplate() throws IOException {

        LOGGER.info("Sending template to URL: {}", post.getURI().toString());
        int responseCode = client.executeMethod(post);
        LOGGER.info("Response code: {}", responseCode);
    }
}
