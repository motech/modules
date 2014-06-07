package org.motechproject.commcare.client;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.io.IOUtils;
import org.motechproject.commcare.exception.CaseParserException;
import org.motechproject.commcare.parser.OpenRosaResponseParser;
import org.motechproject.commcare.request.json.CaseRequest;
import org.motechproject.commcare.response.OpenRosaResponse;
import org.motechproject.server.config.SettingsFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

@Component
public class CommCareAPIHttpClient {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private HttpClient commonsHttpClient;
    private SettingsFacade settingsFacade;

    @Autowired
    public CommCareAPIHttpClient(final HttpClient commonsHttpClient, @Qualifier("commcareAPISettings") final SettingsFacade settingsFacade) {
        this.commonsHttpClient = commonsHttpClient;
        this.settingsFacade = settingsFacade;
    }

    public OpenRosaResponse caseUploadRequest(String caseXml)
            throws CaseParserException {
        return this.postRequest(commcareCaseUploadUrl(), caseXml);
    }

    public String usersRequest() {
        return this.getRequest(commcareUserUrl(), null);
    }

    public String appStructureRequest() {
        return this.getRequest(commcareAppStructureUrl(), null);
    }

    public String formRequest(String formId) {
        return this.getRequest(commcareFormUrl(formId), null);
    }

    public String casesRequest(CaseRequest caseRequest) {
        return this.getRequest(commcareCasesUrl(), caseRequest);
    }
    
    public String singleCaseRequest(String caseId) {
        return this.getRequest(commcareCaseUrl(caseId), null);
    }

    public String fixturesRequest() {
        return this.getRequest(commcareFixturesUrl(), null);
    }

    public String fixtureRequest(String fixtureId) {
        return this.getRequest(commcareFixtureUrl(fixtureId), null);
    }

    public int dataForwardingEndpointUploadRequest(String dataForwardingEndpointJson) {
        return this.dataForwardingEndpointPostRequest(commcareDataForwardingEndpointUrl(), dataForwardingEndpointJson);
    }

    public int dataForwardingEndpointUpdateRequest(String resourceUri, String dataForwardingEndpointJson) {
        String combinedUri = commcareDataForwardingEndpointUrl() + resourceUri + '/';
        return this.dataForwardingEndpointPutRequest(combinedUri, dataForwardingEndpointJson);
    }

    public String dataForwardingEndpointsRequest() {
        return this.getRequest(commcareDataForwardingEndpointUrl(), null);
    }

    public boolean verifyConnection() {
        HttpMethod getMethod = new GetMethod(commcareDataForwardingEndpointUrl());

        authenticate();

        int status = executeMethod(getMethod);

        return status == HttpStatus.SC_OK;
    }

    private int dataForwardingEndpointPostRequest(String requestUrl, String body) {

        PostMethod postMethod = new PostMethod(requestUrl);

        StringRequestEntity stringEntity = null;

        try {
            stringEntity = new StringRequestEntity(body, "application/json",
                    "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            logger.warn("UnsupportedEncodingException, this should not occur: " + e.getMessage()); //This exception cannot happen here
        }

        postMethod.setRequestEntity(stringEntity);

        authenticate();

        return executeMethod(postMethod);
    }

    private int executeMethod(HttpMethod httpMethod) {
        int status = 0;

        try {
            status = commonsHttpClient.executeMethod(httpMethod);
        } catch (HttpException e) {
            logger.warn("HttpException while sending request to CommCareHQ: " + e.getMessage());
        } catch (IOException e) {
            logger.warn("IOException while sending request to CommcareHQ: " + e.getMessage());
        } finally {
            httpMethod.releaseConnection();
        }

        return status;
    }

    private int dataForwardingEndpointPutRequest(String requestUrl, String body) {

        PutMethod putMethod = new PutMethod(requestUrl);

        StringRequestEntity stringEntity = null;

        try {
            stringEntity = new StringRequestEntity(body, "application/json",
                    "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            logger.warn("UnsupportedEncodingException, this should not occur: " + e.getMessage()); //This exception cannot happen here
        }

        putMethod.setRequestEntity(stringEntity);

        authenticate();

        return executeMethod(putMethod);
    }

    private HttpMethod buildRequest(String url, CaseRequest caseRequest) {
        HttpMethod requestMethod = new GetMethod(url);

        authenticate();
        if (caseRequest != null) {
            requestMethod.setQueryString(caseRequest.toQueryString());
        }

        return requestMethod;
    }

    private String getRequest(String requestUrl, CaseRequest caseRequest) {

        HttpMethod getMethod = buildRequest(requestUrl, caseRequest);

        try {
            commonsHttpClient.executeMethod(getMethod);
            InputStream responseBodyAsStream = getMethod.getResponseBodyAsStream();
            return IOUtils.toString(responseBodyAsStream);
        } catch (HttpException e) {
            logger.warn("HttpException while sending request to CommCare: " + e.getMessage());
        } catch (IOException e) {
            logger.warn("IOException while sending request to CommCare: " + e.getMessage());
        } finally {
            getMethod.releaseConnection();
        }

        return null;
    }

    private void authenticate() {
        commonsHttpClient.getParams().setAuthenticationPreemptive(true);

        commonsHttpClient.getState().setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials(getUsername(), getPassword()));
    }

    private OpenRosaResponse postRequest(String requestUrl, String body)
            throws CaseParserException {

        PostMethod postMethod = new PostMethod(requestUrl);

        StringRequestEntity stringEntity = null;

        try {
            stringEntity = new StringRequestEntity(body, "text/xml",
                    "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            logger.warn("UnsupportedEncodingException, this should not occur: " + e.getMessage()); //This exception cannot happen here
        }

        postMethod.setRequestEntity(stringEntity);

        authenticate();

        String response = "";

        int status = 0;

        try {
            status = commonsHttpClient.executeMethod(postMethod);
            response = postMethod.getResponseBodyAsString();
        } catch (HttpException e) {
            logger.warn("HttpException while posting case xml to CommCareHQ: " + e.getMessage());
        } catch (IOException e) {
            logger.warn("IOException while posting case xml to CommcareHQ: " + e.getMessage());
        }

        OpenRosaResponseParser responseParser = new OpenRosaResponseParser();

        OpenRosaResponse openRosaResponse = responseParser
                .parseResponse(response);

        if (openRosaResponse == null) {
            openRosaResponse = new OpenRosaResponse();
        }

        openRosaResponse.setStatus(status);

        return openRosaResponse;

    }

    String commcareUserUrl() {
        return String.format("%s/%s/api/v%s/user/?format=json", getCommcareBaseUrl(), getCommcareDomain(), getCommcareApiVersion());
    }

    String commcareAppStructureUrl() {
        return String.format("%s/%s/api/v%s/application/", getCommcareBaseUrl(), getCommcareDomain(), getCommcareApiVersion());
    }

    String commcareFormUrl(String formId) {
        return String.format("%s/%s/api/v%s/form/%s/?format=json", getCommcareBaseUrl(), getCommcareDomain(), getCommcareApiVersion(), formId);
    }

    String commcareFixturesUrl() {
        return String.format("%s/%s/api/v%s/fixture/", getCommcareBaseUrl(), getCommcareDomain(), getCommcareApiVersion());
    }

    String commcareFixtureUrl(String fixtureId) {
        return String.format("%s%s/", commcareFixturesUrl(), fixtureId);
    }

    String commcareCasesUrl() {
        return String.format("%s/%s/api/v%s/case/", getCommcareBaseUrl(), getCommcareDomain(), getCommcareApiVersion());
    }

    String commcareCaseUrl(String caseId) {
        return String.format("%s/%s/api/v%s/case/%s/", getCommcareBaseUrl(), getCommcareDomain(), getCommcareApiVersion(), caseId);
    }

    private String commcareDataForwardingEndpointUrl() {
        return String.format("%s/%s/api/v0.4/data-forwarding/", getCommcareBaseUrl(), getCommcareDomain());
    }

    String commcareCaseUploadUrl() {
        return String.format("%s/%s/receiver/", getCommcareBaseUrl(), getCommcareDomain());
    }

    private String getCommcareBaseUrl() {
        String commcareBaseUrl = settingsFacade.getProperty("commcareBaseUrl");

        if (commcareBaseUrl.endsWith("/")) {
            commcareBaseUrl = commcareBaseUrl.substring(0, commcareBaseUrl.length() - 1);
        }

        return commcareBaseUrl;
    }

    private String getCommcareDomain() {
        return settingsFacade.getProperty("commcareDomain");
    }

    private String getCommcareApiVersion() {
        return settingsFacade.getProperty("apiVersion");
    }

    private String getUsername() {
        return settingsFacade.getProperty("username");
    }

    private String getPassword() {
        return settingsFacade.getProperty("password");
    }
}
