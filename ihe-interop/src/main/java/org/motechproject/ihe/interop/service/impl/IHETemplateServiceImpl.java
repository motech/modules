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
        post.setRequestEntity(new StringRequestEntity(template));
        post.setParameter("content-type", "application/xml");
        HttpClient client = new HttpClient();

        LOGGER.info("Sending template to URL: {}", url);
        int responseCode = client.executeMethod(post);
        LOGGER.info("Response code: {}", responseCode);
    }
}
