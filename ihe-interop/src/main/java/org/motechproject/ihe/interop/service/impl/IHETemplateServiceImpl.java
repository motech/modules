package org.motechproject.ihe.interop.service.impl;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.motechproject.ihe.interop.service.IHETemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ConnectException;

/**
 * Implementation of {@link IHETemplateService}.
 */
@Service("iheTemplateService")
public class IHETemplateServiceImpl implements IHETemplateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IHETemplateServiceImpl.class);

    @Override
    public void sendTemplateToRecipientUrl(String url, String template) {
        PostMethod post = new PostMethod(url);
        try {
            post.setParameter("content-type", "application/xml");
            HttpClient client = new HttpClient();
            LOGGER.info("Sending template to URL: {}", url);
            int responseCode = client.executeMethod(post);
            LOGGER.info("Response code: {}", responseCode);
            LOGGER.info("Response body: {}", post.getResponseBodyAsString());
        } catch (ConnectException | HttpException e) {
            LOGGER.error("Cannot connect with URL: {}", url);
        } catch (IOException e) {
            LOGGER.error("Cannot get body from response message. URL: {}", url);
        }
    }
}
