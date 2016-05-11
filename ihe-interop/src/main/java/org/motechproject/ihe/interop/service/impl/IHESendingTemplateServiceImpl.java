package org.motechproject.ihe.interop.service.impl;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.motechproject.ihe.interop.service.HL7RecipientsService;
import org.motechproject.ihe.interop.service.IHESendingTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;

/**
 * Implementation of {@link HL7RecipientsService}.
 */
@Service("iheSendingTemplateService")
public class IHESendingTemplateServiceImpl implements IHESendingTemplateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IHESendingTemplateServiceImpl.class);

    @Override
    public void sendTemplateToRecipientUrl(String url, String template) {
        String encoding = "utf-8";
        PostMethod post = new PostMethod(url);
        try {
            post.setRequestEntity(new StringRequestEntity(template, "application/xml", encoding));
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error("Unsupported encoding : {}", encoding);
        }
        HttpClient client = new HttpClient();
        LOGGER.info("Sending template to URL: {}", url);
        try {
            int responseCode = client.executeMethod(post);
            LOGGER.info("Response code: {}", responseCode);
            LOGGER.info("Response body: {}", post.getResponseBodyAsString());
        } catch (ConnectException e) {
            LOGGER.error("Cannot connect with URL: {}", url);
        } catch (HttpException e) {
            LOGGER.error("Cannot connect with URL: {}", url);
        } catch (IOException e) {
            LOGGER.error("Cannot get body from response message. URL: {}", url);
        }
    }
}
