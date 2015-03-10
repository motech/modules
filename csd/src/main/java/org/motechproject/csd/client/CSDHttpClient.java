package org.motechproject.csd.client;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class CSDHttpClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSDHttpClient.class);

    public String getXml(String url) {

        HttpClient client = new HttpClient();

        GetMethod method = new GetMethod(url);

        try {
            client.executeMethod(method);

            InputStream responseBodyAsStream = method.getResponseBodyAsStream();

            return IOUtils.toString(responseBodyAsStream);

        } catch (HttpException e) {
            LOGGER.warn("HttpException while sending request: " + e.getMessage());
        } catch (IOException e) {
            LOGGER.warn("IOException while sending request: " + e.getMessage());
        } finally {
            method.releaseConnection();
        }

        return null;
    }
}
