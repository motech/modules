package org.motechproject.commcare.client;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.motechproject.commcare.config.AccountConfig;
import org.motechproject.commcare.exception.CaseParserException;
import org.motechproject.commcare.exception.CommcareAuthenticationException;
import org.motechproject.commcare.parser.OpenRosaResponseParser;
import org.motechproject.commcare.request.json.CaseRequest;
import org.motechproject.commcare.response.OpenRosaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

@Component
public class CommCareAPIHttpClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommCareAPIHttpClient.class);

    private HttpClient commonsHttpClient;

    @Autowired
    public CommCareAPIHttpClient(final HttpClient commonsHttpClient) {
        this.commonsHttpClient = commonsHttpClient;
    }

    public OpenRosaResponse caseUploadRequest(AccountConfig accountConfig, String caseXml)
            throws CaseParserException {
        return this.postRequest(accountConfig, commcareCaseUploadUrl(accountConfig), caseXml);
    }

    public String userRequest(AccountConfig accountConfig, String userId) {
        return this.getRequest(accountConfig, commcareUserUrl(accountConfig, userId), null);
    }

    public String usersRequest(AccountConfig accountConfig, Integer pageSize, Integer pageNumber) {
        return this.getRequest(accountConfig, commcareUsersUrl(accountConfig, pageSize, pageNumber), null);
    }

    public String appStructureRequest(AccountConfig accountConfig, Integer pageSize, Integer pageNumber) {
        return this.getRequest(accountConfig, commcareAppStructureUrl(accountConfig, pageSize, pageNumber), null);
    }

    public String formRequest(AccountConfig accountConfig, String formId) {
        return this.getRequest(accountConfig, commcareFormUrl(accountConfig, formId), null);
    }

    public String casesRequest(AccountConfig accountConfig, CaseRequest caseRequest) {
        return this.getRequest(accountConfig, commcareCasesUrl(accountConfig), caseRequest);
    }

    public String singleCaseRequest(AccountConfig accountConfig, String caseId) {
        return this.getRequest(accountConfig, commcareCaseUrl(accountConfig, caseId), null);
    }

    public String fixturesRequest(AccountConfig accountConfig, Integer pageSize, Integer pageNumber) {
        return this.getRequest(accountConfig, commcareFixturesUrl(accountConfig, pageSize, pageNumber), null);
    }

    public String fixtureRequest(AccountConfig accountConfig, String fixtureId) {
        return this.getRequest(accountConfig, commcareFixtureUrl(accountConfig, fixtureId), null);
    }

    public int dataForwardingEndpointUploadRequest(AccountConfig accountConfig, String dataForwardingEndpointJson) {
        return this.dataForwardingEndpointPostRequest(accountConfig, commcareDataForwardingEndpointUrl(accountConfig), dataForwardingEndpointJson);
    }

    public int dataForwardingEndpointUpdateRequest(AccountConfig accountConfig, String resourceUri, String dataForwardingEndpointJson) {
        String combinedUri = commcareDataForwardingEndpointUrl(accountConfig) + resourceUri + '/';
        return this.dataForwardingEndpointPutRequest(accountConfig, combinedUri, dataForwardingEndpointJson);
    }

    public String dataForwardingEndpointsRequest(AccountConfig accountConfig, Integer pageSize, Integer pageNumber) {
        return this.getRequest(accountConfig, commcareDataForwardingEndpointsUrl(accountConfig, pageSize, pageNumber), null);
    }

    public boolean verifyConnection(AccountConfig accountConfig) {
        HttpMethod getMethod = new GetMethod(commcareCasesUrl(accountConfig.getBaseUrl(), accountConfig.getDomain()));

        authenticate(accountConfig);

        int status = executeMethod(getMethod);

        if (status == HttpStatus.SC_OK) {
            LOGGER.info("Connection to Commcare verified");
        } else {
            LOGGER.error("Unable to connect to Commcare, response status: {}", status);
        }

        return status == HttpStatus.SC_OK;
    }

    private int dataForwardingEndpointPostRequest(AccountConfig accountConfig, String requestUrl, String body) {

        PostMethod postMethod = new PostMethod(requestUrl);

        StringRequestEntity stringEntity = null;

        try {
            stringEntity = new StringRequestEntity(body, "application/json",
                    "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            LOGGER.warn("UnsupportedEncodingException, this should not occur: " + e.getMessage()); //This exception cannot happen here
        }

        postMethod.setRequestEntity(stringEntity);

        authenticate(accountConfig);

        return executeMethod(postMethod);
    }

    private int executeMethod(HttpMethod httpMethod) {
        int status = 0;

        try {
            status = commonsHttpClient.executeMethod(httpMethod);
        } catch (HttpException e) {
            LOGGER.warn("HttpException while sending request to CommCareHQ: " + e.getMessage());
        } catch (IOException e) {
            LOGGER.warn("IOException while sending request to CommcareHQ: " + e.getMessage());
        } finally {
            httpMethod.releaseConnection();
        }

        return status;
    }

    private int dataForwardingEndpointPutRequest(AccountConfig accountConfig, String requestUrl, String body) {

        PutMethod putMethod = new PutMethod(requestUrl);

        StringRequestEntity stringEntity = null;

        try {
            stringEntity = new StringRequestEntity(body, "application/json",
                    "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            LOGGER.warn("UnsupportedEncodingException, this should not occur: " + e.getMessage()); //This exception cannot happen here
        }

        putMethod.setRequestEntity(stringEntity);

        authenticate(accountConfig);

        return executeMethod(putMethod);
    }

    private HttpMethod buildRequest(AccountConfig accountConfig, String url, CaseRequest caseRequest) {
        HttpMethod requestMethod = new GetMethod(url);

        authenticate(accountConfig);
        if (caseRequest != null) {
            requestMethod.setQueryString(caseRequest.toQueryString());
        }

        return requestMethod;
    }

    private String getRequest(AccountConfig accountConfig, String requestUrl, CaseRequest caseRequest) {

        HttpMethod getMethod = buildRequest(accountConfig, requestUrl, caseRequest);

        try {
            LOGGER.debug("Sending GET request {}", requestUrl);
            commonsHttpClient.executeMethod(getMethod);

            LOGGER.debug("{} request response status: {}", requestUrl, getMethod.getStatusCode());
            switch (getMethod.getStatusCode()) {
                case HttpStatus.SC_UNAUTHORIZED:
                    throw new CommcareAuthenticationException("Couldn't authenticate to CommcareHQ server! Are given credentials correct?");
                default:
                    InputStream responseBodyAsStream = getMethod.getResponseBodyAsStream();
                    String responseBody =  IOUtils.toString(responseBodyAsStream);
                    LOGGER.trace("{} request response body: {}", requestUrl, responseBody);
                    return responseBody;
            }
        } catch (HttpException e) {
            LOGGER.warn("HttpException while sending request to CommCare: " + e.getMessage());
        } catch (IOException e) {
            LOGGER.warn("IOException while sending request to CommCare: " + e.getMessage());
        } finally {
            getMethod.releaseConnection();
        }

        return null;
    }

    private void authenticate(AccountConfig accountConfig) {

        UsernamePasswordCredentials oldCredentials = (UsernamePasswordCredentials) commonsHttpClient.getState().getCredentials(AuthScope.ANY);

        if (credentialsChanged(oldCredentials, accountConfig)) {
            commonsHttpClient.getState().clear();

            commonsHttpClient.getParams().setAuthenticationPreemptive(true);

            commonsHttpClient.getState().setCredentials(
                    AuthScope.ANY,
                    new UsernamePasswordCredentials(accountConfig.getUsername(), accountConfig.getPassword()));
        }
    }

    private boolean credentialsChanged(UsernamePasswordCredentials oldCredentials, AccountConfig newCredentials) {

        return oldCredentials == null
                || !StringUtils.equals(oldCredentials.getUserName(), newCredentials.getUsername())
                || !StringUtils.equals(oldCredentials.getPassword(), newCredentials.getPassword());
    }

    private OpenRosaResponse postRequest(AccountConfig accountConfig, String requestUrl, String body)
            throws CaseParserException {

        PostMethod postMethod = new PostMethod(requestUrl);

        StringRequestEntity stringEntity = null;

        try {
            stringEntity = new StringRequestEntity(body, "text/xml",
                    "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            LOGGER.warn("UnsupportedEncodingException, this should not occur: " + e.getMessage()); //This exception cannot happen here
        }

        postMethod.setRequestEntity(stringEntity);

        authenticate(accountConfig);

        String response = "";

        int status = 0;

        try {
            status = commonsHttpClient.executeMethod(postMethod);
            response = postMethod.getResponseBodyAsString();
        } catch (HttpException e) {
            LOGGER.warn("HttpException while posting case xml to CommCareHQ: " + e.getMessage());
        } catch (IOException e) {
            LOGGER.warn("IOException while posting case xml to CommcareHQ: " + e.getMessage());
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

    String commcareUsersUrl(AccountConfig accountConfig, Integer pageSize, Integer pageNumber) {
        return String.format("%s/%s/api/v%s/user/?format=json%s", getCommcareBaseUrl(accountConfig.getBaseUrl()),
                accountConfig.getDomain(), getCommcareApiVersion(), buildPaginationParams(pageSize, pageNumber));
    }

    String commcareUserUrl(AccountConfig accountConfig, String id) {
        return String.format("%s/%s/api/v%s/user/%s/?format=json", getCommcareBaseUrl(accountConfig.getBaseUrl()),
                accountConfig.getDomain(), getCommcareApiVersion(), id);
    }

    String commcareAppStructureUrl(AccountConfig accountConfig, Integer pageSize, Integer pageNumber) {
        return String.format("%s/%s/api/v%s/application/?format=json%s", getCommcareBaseUrl(accountConfig.getBaseUrl()),
                accountConfig.getDomain(), getCommcareApiVersion(), buildPaginationParams(pageSize, pageNumber));
    }

    String commcareFormUrl(AccountConfig accountConfig, String formId) {
        return String.format("%s/%s/api/v%s/form/%s/?format=json", getCommcareBaseUrl(accountConfig.getBaseUrl()),
                accountConfig.getDomain(), getCommcareApiVersion(), formId);
    }

    String commcareFixturesUrl(AccountConfig accountConfig) {
        return String.format("%s/%s/api/v%s/fixture/", getCommcareBaseUrl(accountConfig.getBaseUrl()),
                accountConfig.getDomain(), getCommcareApiVersion());
    }

    String commcareFixtureUrl(AccountConfig accountConfig, String fixtureId) {
        return String.format("%s%s/", commcareFixturesUrl(accountConfig), fixtureId);
    }

    String commcareFixturesUrl(AccountConfig accountConfig, Integer pageSize, Integer pageNumber) {
        return String.format("%s?format=json%s", commcareFixturesUrl(accountConfig),
                buildPaginationParams(pageSize, pageNumber));
    }

    String commcareCasesUrl(AccountConfig accountConfig) {
        return String.format("%s/%s/api/v%s/case/", getCommcareBaseUrl(accountConfig.getBaseUrl()),
                accountConfig.getDomain(), getCommcareApiVersion());
    }

    String commcareCasesUrl(String baseUrl, String domain) {
        return String.format("%s/%s/api/v%s/case/", baseUrl, domain, getCommcareApiVersion());
    }

    String commcareCaseUrl(AccountConfig accountConfig, String caseId) {
        return String.format("%s/%s/api/v%s/case/%s/", getCommcareBaseUrl(accountConfig.getBaseUrl()),
                accountConfig.getDomain(), getCommcareApiVersion(), caseId);
    }

    private String commcareDataForwardingEndpointUrl(AccountConfig accountConfig) {
        return String.format("%s/%s/api/v0.4/data-forwarding/?format=json",
                getCommcareBaseUrl(accountConfig.getBaseUrl()), accountConfig.getDomain());
    }

    private String commcareDataForwardingEndpointsUrl(AccountConfig accountConfig, Integer pageSize, Integer pageNumber) {
        return String.format("%s%s", commcareDataForwardingEndpointUrl(accountConfig), buildPaginationParams(pageSize, pageNumber));
    }

    String commcareCaseUploadUrl(AccountConfig accountConfig) {
        return String.format("%s/%s/receiver/", getCommcareBaseUrl(accountConfig.getBaseUrl()),
                accountConfig.getDomain());
    }

    private String getCommcareBaseUrl(String baseUrl) {
        String commcareBaseUrl = baseUrl;

        if (commcareBaseUrl.endsWith("/")) {
            commcareBaseUrl = commcareBaseUrl.substring(0, commcareBaseUrl.length() - 1);
        }

        return commcareBaseUrl;
    }

    private String buildPaginationParams(Integer pageSize, Integer pageNumber) {
        StringBuilder sb = new StringBuilder();

        if (pageSize != null && pageSize > 0) {
            sb.append("&limit=" + pageSize.toString());
        }
        if (pageNumber != null && pageNumber > 0) {
            sb.append("&offset=" + ((pageNumber - 1) * (pageSize != null && pageSize >= 0 ? pageSize : 0)));
        }

        return sb.toString();
    }


    private String getCommcareApiVersion() {
        return "0.4";
    }
}
