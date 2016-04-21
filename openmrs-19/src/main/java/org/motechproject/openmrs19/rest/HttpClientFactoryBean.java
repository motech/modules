package org.motechproject.openmrs19.rest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.springframework.beans.factory.FactoryBean;

/**
 * Factory bean for creating a HTTP client with BASIC authentication.
 */
public class HttpClientFactoryBean implements FactoryBean<HttpClient> {

    private HttpClient httpClient;

    @Override
    public HttpClient getObject() {
        if (httpClient == null) {
            initializeHttpClient();
        }

        return httpClient;
    }

    private void initializeHttpClient() {
        httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
    }

    @Override
    public Class<?> getObjectType() {
        return HttpClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
