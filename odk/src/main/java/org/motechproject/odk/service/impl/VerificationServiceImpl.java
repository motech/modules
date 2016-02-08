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
import org.motechproject.odk.exception.BasicAuthException;
import org.motechproject.odk.service.VerificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("odkVerificationService")
public class VerificationServiceImpl implements VerificationService {

    private static final String FORM_LIST = "/formList";
    private static final int STATUS_OK = 200;
    private static final int TIMEOUT = 5000;
    private static final Logger LOGGER = LoggerFactory.getLogger(VerificationServiceImpl.class);

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
        return executeRequest(request);
    }

    @Override
    public Verification verifyOna(Configuration configuration) {
        return verifyKobo(configuration);
    }

    @Override
    public Verification verifyOdk(Configuration configuration) {
        HttpGet request = new HttpGet(configuration.getUrl() + FORM_LIST);
        return executeRequest(request);
    }

    private Verification executeRequest(HttpGet request) {
        request.setConfig(config);
        try {
            HttpResponse response = client.execute(request);
            int status = response.getStatusLine().getStatusCode();
            if (status == STATUS_OK) {
                return new Verification(true);
            } else {
                return new Verification(false, Integer.toString(status));
            }
        } catch (Exception e) {
            LOGGER.error("Error verifying connection", e);
            return new Verification(false, e.toString());
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
            throw new BasicAuthException(e);
        }
        return basicAuthHeader;
    }
}
