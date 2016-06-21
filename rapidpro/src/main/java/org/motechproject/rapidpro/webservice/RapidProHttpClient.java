package org.motechproject.rapidpro.webservice;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.motechproject.rapidpro.domain.Settings;
import org.motechproject.rapidpro.exception.RapidProClientException;
import org.motechproject.rapidpro.service.SettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;


/**
 * Performs all HTTP operations for the Rapidpro Module
 */
@Component
public class RapidProHttpClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(RapidProHttpClient.class);

    private static final String PROTOCOL = "https://";
    private static final String DOMAIN_NAME = "app.rapidpro.io/api/";
    private static final String URL = PROTOCOL + DOMAIN_NAME;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN = "Token ";

    private static final String ERROR_URI = "Error building URI for request";
    private static final String UNSUPPORTED_MEDIA = "Unsupported Media Type: ";
    private static final String REDIRECTION = "Redirection: ";
    private static final String CLIENT_ERROR = "Client Error: ";
    private static final String SERVER_ERROR = "Server Error: ";
    private static final String EXECUTING_REQUEST = "Executing request: ";
    private static final String UNABLE_TO_EXECUTE = "Unable to execute request";
    private static final String ERROR_RESPONSE_ENTITY = "Error retreiving response entity.";
    private static final String NO_SETTINGS = "No settings";
    private static final String NO_API_KEY = "No API key";
    private static final String NO_VERSION = "No API version";

    private HttpClient client;
    private SettingsService settingsService;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    public RapidProHttpClient(HttpClientBuilderFactory httpClientBuilderFactory, SettingsService settingsService) {
        this.client = httpClientBuilderFactory.newBuilder().build();
        this.settingsService = settingsService;
    }

    /**
     * Executes an HTTP GET Request
     *
     * @param endpoint            The API endpoint
     * @param responseMediaFormat The desired format for the response
     * @param params              The query parameters
     * @return An {@link InputStream} containing the response
     * @throws RapidProClientException
     */
    public InputStream executeGet(String endpoint, MediaFormat responseMediaFormat, Map<String, String> params) throws RapidProClientException {
        URI uri = buildUri(endpoint, responseMediaFormat, params);
        HttpGet request = new HttpGet(uri);
        return executeRequestWithResponseContent(request);
    }

    /**
     * Executes an HTTP POST Request
     *
     * @param endpoint            The API endpoint
     * @param body                The body of the request
     * @param contentType         The content type of the request
     * @param responseMediaFormat The desered format for the response
     * @return An {@link InputStream} containing the response
     * @throws RapidProClientException
     */
    public InputStream executePost(String endpoint, byte[] body, MediaFormat contentType, MediaFormat responseMediaFormat) throws RapidProClientException {
        URI uri = buildUri(endpoint, responseMediaFormat, null);
        HttpPost request = new HttpPost(uri);
        request.setEntity(new ByteArrayEntity(body));
        setContentType(request, contentType);
        return executeRequestWithResponseContent(request);
    }

    /**
     * Executes an HTTP DELETE Request
     *
     * @param endpoint            The API endpoint
     * @param responseMediaFormat The desired format for the response.
     * @param params              The query parameters
     * @throws RapidProClientException
     */
    public void executeDelete(String endpoint, MediaFormat responseMediaFormat, Map<String, String> params) throws RapidProClientException {
        URI uri = buildUri(endpoint, responseMediaFormat, params);
        HttpDelete request = new HttpDelete(uri);
        executeRequest(request);
    }

    private URI buildUri(String endpoint, MediaFormat responseMediaFormat, Map<String, String> params) throws RapidProClientException {
        try {
            URIBuilder builder = new URIBuilder((URL + getVersion() + endpoint + responseMediaFormat.getExtension()));

            if (params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    builder.addParameter(entry.getKey(), entry.getValue());
                }
            }
            return builder.build();
        } catch (RapidProClientException | URISyntaxException e) {
            throw new RapidProClientException(ERROR_URI, e);
        }
    }

    private void setContentType(HttpUriRequest request, MediaFormat contentType) throws RapidProClientException {
        switch (contentType) {
            case JSON:
                request.setHeader(HTTP.CONTENT_TYPE, MediaFormat.JSON.getContentType());
                break;
            case XML:
                request.setHeader(HTTP.CONTENT_TYPE, MediaFormat.XML.getContentType());
                break;
            default:
                throw new RapidProClientException(UNSUPPORTED_MEDIA + contentType.getContentType());
        }
    }

    private void checkHttpStatusCode(HttpResponse response) throws RapidProClientException {
        int status = response.getStatusLine().getStatusCode();
        if (status < HttpStatus.SC_MULTIPLE_CHOICES) {
            return;
        } else if (status < HttpStatus.SC_BAD_REQUEST) {
            throw new RapidProClientException(REDIRECTION + status + getErrorResponseBody(response));
        } else if (status < HttpStatus.SC_INTERNAL_SERVER_ERROR) {
            throw new RapidProClientException(CLIENT_ERROR + status + getErrorResponseBody(response));
        } else {
            throw new RapidProClientException(SERVER_ERROR + status + getErrorResponseBody(response));
        }
    }

    private String getErrorResponseBody(HttpResponse response) {
        try {
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            return e.getMessage();
        }
    }


    private InputStream executeRequestWithResponseContent(HttpUriRequest request) throws RapidProClientException {
        try {
            return executeRequest(request).getEntity().getContent();
        } catch (IOException e) {
            throw new RapidProClientException(ERROR_RESPONSE_ENTITY, e);
        }
    }

    private HttpResponse executeRequest(HttpUriRequest request) throws RapidProClientException {
        try {
            generateTokenAuthHeader(request);
            LOGGER.debug(EXECUTING_REQUEST + request.toString());
            HttpResponse response = client.execute(request);
            checkHttpStatusCode(response);
            return response;
        } catch (IOException e) {
            throw new RapidProClientException(UNABLE_TO_EXECUTE, e);
        }
    }

    private void generateTokenAuthHeader(HttpUriRequest request) throws RapidProClientException {
        Header tokenAuthHeader = new BasicHeader(AUTHORIZATION_HEADER, TOKEN + getApiKey());
        request.setHeader(tokenAuthHeader);
    }

    private String getApiKey() throws RapidProClientException {
        Settings settings = getSettings();
        if (settings.getApiKey() == null) {
            throw new RapidProClientException(NO_API_KEY);
        } else {
            return settings.getApiKey();
        }
    }

    private String getVersion() throws RapidProClientException {
        Settings settings = getSettings();
        if (settings.getVersion() == null) {
            throw new RapidProClientException(NO_VERSION);
        } else {
            return settings.getVersion();
        }
    }

    private Settings getSettings() throws RapidProClientException {
        if (settingsService.getSettings() == null) {
            throw new RapidProClientException(NO_SETTINGS);
        } else {
            return settingsService.getSettings();
        }
    }
}
