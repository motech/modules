package org.motechproject.ihe.interop.service.impl;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
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

    @Override
    public void sendTemplateToRecipientUrl(String url, String template) throws IOException {

        PostMethod post = new PostMethod(url);
        HttpClient client = new HttpClient();

        post.setRequestEntity(new StringRequestEntity(template, "application/xml", "utf-8"));

        LOGGER.info("Sending template to URL: {}", post.getURI().toString());
        int responseCode = client.executeMethod(post);
        LOGGER.info("Response code: {}", responseCode);
    }
}
