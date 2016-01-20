package org.motechproject.odk.service.impl;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.Verification;
import org.motechproject.odk.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationServiceImpl implements VerificationService {

    private static final String FORM_LIST = "/formList";
    private static final int STATUS_OK = 200;
    private static final int TIMEOUT = 2000;

    private HttpClient client;
    private RequestConfig config;

    @Autowired
    public VerificationServiceImpl(HttpClientBuilderFactory httpClientBuilderFactory) {
        this.config = RequestConfig.custom()
                .setConnectionRequestTimeout(TIMEOUT)
                .setConnectTimeout(TIMEOUT)
                .setSocketTimeout(TIMEOUT)
                .build();
        this.client = httpClientBuilderFactory.newBuilder().build();

    }

    @Override
    public Verification verifyKobo(Configuration configuration) {
        HttpGet request = new HttpGet(configuration.getUrl() + "/" + configuration.getUsername() + FORM_LIST);
        request.addHeader(generateBasicAuthHeader(request, configuration));
        return new Verification(executeRequest(request));
    }

    @Override
    public Verification verifyOna(Configuration configuration) {
        return verifyKobo(configuration);
    }

    @Override
    public Verification verifyOdk(Configuration configuration) {
        HttpGet request = new HttpGet(configuration.getUrl() + FORM_LIST);
        return new Verification(executeRequest(request));
    }

    private boolean executeRequest(HttpGet request) {
        request.setConfig(config);
        try {
            HttpResponse response = client.execute(request);
            return response.getStatusLine().getStatusCode() == STATUS_OK;
        } catch (Exception e) {
            return false;
        }
    }

    private Header generateBasicAuthHeader(HttpUriRequest request, Configuration configuration) {
        Header basicAuthHeader;
        BasicScheme basicScheme = new BasicScheme();
        try {
            basicAuthHeader = basicScheme.authenticate(
                    new UsernamePasswordCredentials(configuration.getUsername(), configuration.getPassword()),
                    request,
                    HttpClientContext.create());
        } catch (Exception e) {
            return null;
        }
        return basicAuthHeader;
    }
}
