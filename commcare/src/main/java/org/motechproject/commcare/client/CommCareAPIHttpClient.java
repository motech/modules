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
import org.apache.http.client.utils.URIBuilder;
import org.motechproject.commcare.config.AccountConfig;
import org.motechproject.commcare.exception.CommcareAuthenticationException;
import org.motechproject.commcare.exception.OpenRosaParserException;
import org.motechproject.commcare.parser.OpenRosaResponseParser;
import org.motechproject.commcare.request.FormListRequest;
import org.motechproject.commcare.request.StockTransactionRequest;
import org.motechproject.commcare.request.json.CaseRequest;
import org.motechproject.commcare.request.json.Request;
import org.motechproject.commcare.response.OpenRosaResponse;
import org.motechproject.commcare.util.CommcareParamHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

/**
 * A Commcare REST API client. Responsible for sending requests to the Commcare server and fetching data from it.
 */
@Component
public class CommCareAPIHttpClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommCareAPIHttpClient.class);

    private static final String API_VERSION = "0.5";

    private HttpClient commonsHttpClient;

    @Autowired
    public CommCareAPIHttpClient(final HttpClient commonsHttpClient) {
        this.commonsHttpClient = commonsHttpClient;
    }

    /**
     * Sends a POST request to the CommCare server. It will result in creating new entry on the CommCare server based on
     * the information given in the {@code xml}, which should be a valid case or form represented by an XML string.
     *
     * @param accountConfig  the CommCare account information
     * @param xml  the submission represented as an XML string
     * @return the CommCare server response as an instance of the {@link OpenRosaResponse} class
     * @throws OpenRosaParserException if there were problems while parsing server response
     */
    public OpenRosaResponse submissionRequest(AccountConfig accountConfig, String xml)
            throws OpenRosaParserException {
        return this.postRequest(accountConfig, commcareSubmissionUrl(accountConfig), xml);
    }

    /**
     * Retrieves user with the given {@code userId} from the CommCare server. The retrieved user is returned as a JSON
     * string.
     *
     * @param accountConfig  the Commcare account information
     * @param userId  the ID of the user
     * @return the JSON string representation of the user, null if the user with the given ID does not exist
     */
    public String userRequest(AccountConfig accountConfig, String userId) {
        return this.getRequest(accountConfig, commcareUserUrl(accountConfig, userId), null);
    }

    /**
     * Retrieves a list of users from the CommCare server. The data is fetched in accordance to the passed paging
     * parameters. The retrieved list is returned as a JSON string.
     *
     * @param accountConfig  the Commcare account information
     * @param pageSize  the size of the page
     * @param pageNumber  the number of the page
     * @return the JSON string representation of the users
     */
    public String usersRequest(AccountConfig accountConfig, Integer pageSize, Integer pageNumber) {
        return this.getRequest(accountConfig, commcareUsersUrl(accountConfig, pageSize, pageNumber), null);
    }

    /**
     * Retrieves a location with the given {@code locationId} from the Commcare server. The retrieved location is
     * returned as a JSON string.
     *
     * @param accountConfig  the Commcare account information
     * @param locationId  the id of the location
     * @return the JSON string representation of the location, null if the location with the given ID does not exist
     */
    public String locationRequest(AccountConfig accountConfig, String locationId) {
        return this.getRequest(accountConfig, commcareLocationUrl(accountConfig, locationId), null);
    }

    /**
     * Retrieves a list of locations. The data is fetched in accordance to the passed paging parameters. The retrieved
     * list is returned as a JSON string.
     *
     * @param accountConfig  the Commcare account information
     * @param pageSize  the size of the page
     * @param pageNumber  the number of the page
     * @return the JSON string representation of the locations
     */
    public String locationsRequest(AccountConfig accountConfig, Integer pageSize, Integer pageNumber) {
        return this.getRequest(accountConfig, commcareLocationsUrl(accountConfig, pageSize, pageNumber), null);
    }

    /**
     * Retrieves a list of the applications from the CommCare server. The data is fetched in accordance to the passed
     * paging parameters. The retrieved list is returned as a JSON string.
     *
     * @param accountConfig  the Commcare account information
     * @param pageSize  the size of the page
     * @param pageNumber  the number of the page
     * @return the JSON string representation of the applications
     */
    public String appStructureRequest(AccountConfig accountConfig, Integer pageSize, Integer pageNumber) {
        return this.getRequest(accountConfig, commcareAppStructureUrl(accountConfig, pageSize, pageNumber), null);
    }

    /**
     * Retrieves the form with the given {@code formId} from the CommCare server. The retrieved form is returned as a
     * JSON string.
     *
     * @param accountConfig  the CommCare account information
     * @param formId  the ID of the form
     * @return the JSON string representation of the form, null if the form with the given ID does not exist
     */
    public String formRequest(AccountConfig accountConfig, String formId) {
        return this.getRequest(accountConfig, commcareFormUrl(accountConfig, formId), null);
    }

    /**
     * Executes a HTTP get request to the form list API endpoint.
     *
     * @param accountConfig  the account configuration to use
     * @param formListRequest  the request that will be used for creating the HTTP request
     * @return the response as a String (JSON expected)
     */
    public String formListRequest(AccountConfig accountConfig, FormListRequest formListRequest) {
        return this.getRequest(accountConfig, commcareFormListUrl(accountConfig, formListRequest), null);
    }

    /**
     * Executes a HTTP get request to the report data API endpoint.
     *
     * @param accountConfig  the account configuration to use
     * @param reportId  the ID of the report
     * @return the response as a String (JSON expected)
     */
    public String singleReportDataRequest(AccountConfig accountConfig, String reportId){
        return this.getRequest(accountConfig, commcareReportDataUrl(accountConfig, reportId), null);
    }

    public String singleReportDataRequestWithFilters(AccountConfig accountConfig, String reportId, String filters) {
        return this.getRequest(accountConfig, commcareReportDataUrlWithFilters(accountConfig, reportId, filters), null);
    }

    /**
      * Executes a HTTP get request to the reports list API endpoint.
      *
      * @param accountConfig  the account configuration to use
      * @return the response as a String (JSON expected)
      */
    public String reportsListMetadataRequest(AccountConfig accountConfig) {
        return this.getRequest(accountConfig, commcareReportsMetadataUrl(accountConfig), null);
    }

    /**
     * Retrieves a list of the cases from the CommCare server. The given {@code caseRequest} will be used for fetching
     * data from the server.
     *
     * @param accountConfig  the CommCare account information
     * @param caseRequest  the request parameters
     * @return the JSON string representation of the cases
     */
    public String casesRequest(AccountConfig accountConfig, CaseRequest caseRequest) {
        return this.getRequest(accountConfig, commcareCasesUrl(accountConfig, caseRequest), caseRequest);
    }

    /**
     * Retrieves case with the given {@code caseId} from the CommCare server. The retrieved case is returned as a JSON
     * string.
     *
     * @param accountConfig  the CommCare account information
     * @param caseId  the ID of the case
     * @return the JSON string representation of the case, null if the case with the given ID does not exist
     */
    public String singleCaseRequest(AccountConfig accountConfig, String caseId) {
        return this.getRequest(accountConfig, commcareCaseUrl(accountConfig, caseId), null);
    }

    /**
     * Retrieves a list of fixtures from the CommCare server. The data is fetched in accordance to the passed paging
     * parameters. The retrieved list is returned as a JSON string.
     *
     * @param accountConfig  the Commcare account information
     * @param pageSize  the size of the page
     * @param pageNumber  the number of the page
     * @return the JSON string representation of the fixtures
     */
    public String fixturesRequest(AccountConfig accountConfig, Integer pageSize, Integer pageNumber) {
        return this.getRequest(accountConfig, commcareFixturesUrl(accountConfig, pageSize, pageNumber), null);
    }

    /**
     * Retrieves fixture with the given {@code fixtureId} from the CommCare server. The retrieved fixture is returned as
     * a JSON string.
     *
     * @param accountConfig  the CommCare account information
     * @param fixtureId  the ID of the fixture
     * @return the JSON string representation of the fixture, null if the fixture with the given ID does not exist
     */
    public String fixtureRequest(AccountConfig accountConfig, String fixtureId) {
        return this.getRequest(accountConfig, commcareFixtureUrl(accountConfig, fixtureId), null);
    }

    /**
     * Sends a POST request to the CommCare server. It will result in creating a new forwarding endpoint based on the
     * information given in the {@code dataForwardingEndpointJson}, which should be a valid forwarding endpoint
     * represented by a JSON string.
     *
     * @param accountConfig  the CommCare account information
     * @param dataForwardingEndpointJson  the JSON string representation of the endpoint
     * @return the server response status
     */
    public int dataForwardingEndpointUploadRequest(AccountConfig accountConfig, String dataForwardingEndpointJson) {
        return this.dataForwardingEndpointPostRequest(accountConfig, commcareDataForwardingEndpointUrl(accountConfig), dataForwardingEndpointJson);
    }

    /**
     * Sends a PUT request to the CommCare server. It will result in updating the forwarding endpoint pointed by the
     * {@code resourceUri}. The forwarding endpoint will be updated with the data passed in the
     * {@code dataForwardingEndpointJson}, which should be a valid forwarding endpoint represented by a JSON string.
     *
     * @param accountConfig  the CommCare account information
     * @param resourceUri  the URI pointing to the forwarding endpoint
     * @param dataForwardingEndpointJson  the updated forwarding endpoint represented by a JSON string
     * @return the server response status
     */
    public int dataForwardingEndpointUpdateRequest(AccountConfig accountConfig, String resourceUri, String dataForwardingEndpointJson) {
        String combinedUri = commcareDataForwardingEndpointUrl(accountConfig) + resourceUri + '/';
        return this.dataForwardingEndpointPutRequest(accountConfig, combinedUri, dataForwardingEndpointJson);
    }

    /**
     * Retrieves a list of the forwarding endpoints from the CommCare server. The data is fetched in accordance to the
     * passed paging parameters. The retrieved list is returned as a JSON string.
     *
     * @param accountConfig  the CommCare account information
     * @param pageSize  the size of the page
     * @param pageNumber  the number of the page
     * @return the JSON string representation of the forwarding endpoints
     */
    public String dataForwardingEndpointsRequest(AccountConfig accountConfig, Integer pageSize, Integer pageNumber) {
        return this.getRequest(accountConfig, commcareDataForwardingEndpointsUrl(accountConfig, pageSize, pageNumber), null);
    }

    /**
     * Retrieves a list of the stock transactions from the CommCareHQ server. The data is fetched in accordance to the
     * passed paging parameters. The retrieved list is returned as a JSON string.
     *
     * @param accountConfig  the CommCare account information
     * @param pageSize  the size of the page
     * @param pageNumber  the number of the page
     * @return the JSON string representation of the stock transactions
     */
    public String stockTransactionsRequest(AccountConfig accountConfig, Integer pageSize, Integer pageNumber) {
        return this.getRequest(accountConfig, commcareStockTransactionsUrl(accountConfig, pageSize, pageNumber), null);
    }

    /**
     * Retrieves a list of the stock transactions from the CommCare server. The given {@code request} will be used for
     * fetching data from the server. The retrieved list is returned as a JSON string.
     *
     * @param accountConfig  the CommCare account information
     * @param request  the request parameters
     * @return the JSON string representation of the stock transactions
     */
    public String stockTransactionsRequest(AccountConfig accountConfig, StockTransactionRequest request) {
        return this.getRequest(accountConfig, commcareStockTransactionsUrl(accountConfig), request);
    }

    /**
     * Verifies whether the connection with the CommCare server is possible using the given credentials. If this method
     * returns false, it might indicate that the given credentials are incorrect or a server error occurred.
     *
     * @param accountConfig  the CommCare account information
     * @return true if connection was possible, false otherwise
     */
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

    private HttpMethod buildRequest(AccountConfig accountConfig, String url, Request request) {
        HttpMethod requestMethod = new GetMethod(url);

        authenticate(accountConfig);
        if (request != null && requestMethod.getQueryString() == null) {
            requestMethod.setQueryString(request.toQueryString());
        }

        return requestMethod;
    }

    private String getRequest(AccountConfig accountConfig, String requestUrl, Request request) {

        HttpMethod getMethod = buildRequest(accountConfig, requestUrl, request);

        try {
            LOGGER.debug("Sending GET request {}", requestUrl);
            commonsHttpClient.executeMethod(getMethod);

            LOGGER.debug("{} request response status: {}", requestUrl, getMethod.getStatusCode());
            switch (getMethod.getStatusCode()) {
                case HttpStatus.SC_UNAUTHORIZED:
                    throw new CommcareAuthenticationException();
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
            throws OpenRosaParserException {

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

        OpenRosaResponse openRosaResponse = responseParser.parseResponse(response);

        if (openRosaResponse == null) {
            openRosaResponse = new OpenRosaResponse();
        }

        openRosaResponse.setStatus(status);

        return openRosaResponse;

    }

    String commcareUsersUrl(AccountConfig accountConfig, Integer pageSize, Integer pageNumber) {
        return String.format("%s/%s/api/v%s/user/?format=json%s", getCommcareBaseUrl(accountConfig.getBaseUrl()),
                accountConfig.getDomain(), API_VERSION, buildPaginationParams(pageSize, pageNumber));
    }

    String commcareUserUrl(AccountConfig accountConfig, String id) {
        return String.format("%s/%s/api/v%s/user/%s/?format=json", getCommcareBaseUrl(accountConfig.getBaseUrl()),
                accountConfig.getDomain(), API_VERSION, id);
    }

    String commcareLocationsUrl(AccountConfig accountConfig, Integer pageSize, Integer pageNumber) {
        return String.format("%s/%s/api/v%s/location/?format=json%s", getCommcareBaseUrl(accountConfig.getBaseUrl()),
                accountConfig.getDomain(), API_VERSION, buildPaginationParams(pageSize, pageNumber));
    }

    String commcareLocationUrl(AccountConfig accountConfig, String id) {
        return String.format("%s/%s/api/v%s/location/%s/?format=json", getCommcareBaseUrl(accountConfig.getBaseUrl()),
                accountConfig.getDomain(), API_VERSION, id);
    }

    String commcareAppStructureUrl(AccountConfig accountConfig, Integer pageSize, Integer pageNumber) {
        return String.format("%s/%s/api/v%s/application/?format=json%s", getCommcareBaseUrl(accountConfig.getBaseUrl()),
                accountConfig.getDomain(), API_VERSION, buildPaginationParams(pageSize, pageNumber));
    }

    String commcareFormUrl(AccountConfig accountConfig, String formId) {
        return String.format("%s/%s/api/v%s/form/%s/?format=json", getCommcareBaseUrl(accountConfig.getBaseUrl()),
                accountConfig.getDomain(), API_VERSION, formId);
    }

    String commcareFormListUrl(AccountConfig accountConfig, FormListRequest formListRequest) {
        try {
            URIBuilder uriBuilder = new URIBuilder(String.format("%s/%s/api/v%s/form", getCommcareBaseUrl(accountConfig.getBaseUrl()),
                    accountConfig.getDomain(), API_VERSION));

            if (formListRequest != null) {
                formListRequest.addQueryParams(uriBuilder);
            }

            return uriBuilder.build().toString();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Unable to build form list url", e);
        }
    }

    String commcareFixturesUrl(AccountConfig accountConfig) {
        return String.format("%s/%s/api/v%s/fixture/", getCommcareBaseUrl(accountConfig.getBaseUrl()),
                accountConfig.getDomain(), API_VERSION);
    }

    String commcareFixtureUrl(AccountConfig accountConfig, String fixtureId) {
        return String.format("%s%s/", commcareFixturesUrl(accountConfig), fixtureId);
    }

    String commcareFixturesUrl(AccountConfig accountConfig, Integer pageSize, Integer pageNumber) {
        return String.format("%s?format=json%s", commcareFixturesUrl(accountConfig),
                buildPaginationParams(pageSize, pageNumber));
    }

    String commcareCasesUrl(AccountConfig accountConfig, CaseRequest caseRequest) {
        try {
            URIBuilder uriBuilder = new URIBuilder(String.format("%s/%s/api/v%s/case/", getCommcareBaseUrl(accountConfig.getBaseUrl()),
                    accountConfig.getDomain(), API_VERSION));

            if (caseRequest != null) {
                caseRequest.addQueryParams(uriBuilder);
            }

            return uriBuilder.build().toString();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Unable to build form list url", e);
        }
    }

    String commcareCasesUrl(AccountConfig accountConfig) {
        return String.format("%s/%s/api/v%s/case/", getCommcareBaseUrl(accountConfig.getBaseUrl()),
                accountConfig.getDomain(), API_VERSION);
    }

    String commcareCasesUrl(String baseUrl, String domain) {
        return String.format("%s/%s/api/v%s/case/", baseUrl, domain, API_VERSION);
    }

    String commcareCaseUrl(AccountConfig accountConfig, String caseId) {
        return String.format("%s/%s/api/v%s/case/%s/", getCommcareBaseUrl(accountConfig.getBaseUrl()),
                accountConfig.getDomain(), API_VERSION, caseId);
    }

    String commcareReportDataUrl(AccountConfig accountConfig, String reportId) {
        return String.format("%s/%s/api/v%s/configurablereportdata/%s/?format=json",
                getCommcareBaseUrl(accountConfig.getBaseUrl()), accountConfig.getDomain(), API_VERSION, reportId);
    }

    String commcareReportDataUrlWithFilters(AccountConfig accountConfig, String reportId, String filter) {
        return commcareReportDataUrl(accountConfig, reportId).concat(filter);
    }

    String commcareReportsMetadataUrl(AccountConfig accountConfig) {
        return String.format("%s/%s/api/v%s/simplereportconfiguration/?format=json", getCommcareBaseUrl(accountConfig.getBaseUrl()),
                        accountConfig.getDomain(), API_VERSION);
    }

    String commcareStockTransactionsUrl(AccountConfig accountConfig) {
        return String.format("%s/%s/api/v%s/stock_transaction/?format=json",
                getCommcareBaseUrl(accountConfig.getBaseUrl()), accountConfig.getDomain(), API_VERSION);
    }

    String commcareStockTransactionsUrl(AccountConfig accountConfig, Integer pageSize, Integer pageNumber) {
        return String.format("%s/%s/api/v%s/stock_transaction/?format=json%s",
                getCommcareBaseUrl(accountConfig.getBaseUrl()), accountConfig.getDomain(), API_VERSION,
                buildPaginationParams(pageSize, pageNumber));
    }

    private String commcareDataForwardingEndpointUrl(AccountConfig accountConfig) {
        return String.format("%s/%s/api/v%s/data-forwarding/?format=json",
                getCommcareBaseUrl(accountConfig.getBaseUrl()), accountConfig.getDomain(), API_VERSION);
    }

    private String commcareDataForwardingEndpointsUrl(AccountConfig accountConfig, Integer pageSize, Integer pageNumber) {
        return String.format("%s%s", commcareDataForwardingEndpointUrl(accountConfig), buildPaginationParams(pageSize, pageNumber));
    }

    String commcareSubmissionUrl(AccountConfig accountConfig) {
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
        return buildPaginationParams(pageSize, pageNumber, false);
    }

    private String buildPaginationParams(Integer pageSize, Integer pageNumber, boolean startWithQuestionMark) {
        StringBuilder sb = new StringBuilder();

        if (pageSize != null && pageSize > 0) {
            sb.append(startWithQuestionMark && sb.length() == 0 ? '?' : '&');
            sb.append("limit=").append(pageSize.toString());
        }
        if (pageNumber != null && pageNumber > 0) {
            sb.append(startWithQuestionMark && sb.length() == 0 ? '?' : '&');
            sb.append("offset=").append(CommcareParamHelper.toOffset(pageSize, pageNumber));
        }

        return sb.toString();
    }
}
