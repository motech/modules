package org.motechproject.http.agent.listener;

import org.apache.http.client.HttpClient;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpException;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.http.agent.domain.EventDataKeys;
import org.motechproject.http.agent.domain.EventSubjects;
import org.motechproject.http.agent.domain.HTTPActionAudit;
import org.motechproject.http.agent.factory.HttpComponentsClientHttpRequestFactoryWithAuth;
import org.motechproject.http.agent.service.HTTPActionService;
import org.motechproject.http.agent.service.Method;
import org.motechproject.config.SettingsFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.motechproject.http.agent.constants.SendRequestConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Component which listens for Motech events and sends http requests based on the data received.
 *
 * @see org.motechproject.http.agent.domain.EventSubjects
 */
@Component
public class HttpClientEventListener {

    public static final String HTTP_CONNECT_TIMEOUT = "http.connect.timeout";
    public static final String HTTP_READ_TIMEOUT = "http.read.timeout";
    public static final int HUNDRED = 100;

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientEventListener.class);

    private RestTemplate basicRestTemplate;
    private SettingsFacade settings;
    private HTTPActionService httpActionService;

    @Autowired
    public HttpClientEventListener(RestTemplate basicRestTemplate,
                                   @Qualifier("httpAgentSettings") SettingsFacade settings, HTTPActionService httpActionService) {
        HttpComponentsClientHttpRequestFactory requestFactory = (HttpComponentsClientHttpRequestFactory) basicRestTemplate
                .getRequestFactory();
        requestFactory.setConnectTimeout(Integer.parseInt(settings
                .getProperty(HTTP_CONNECT_TIMEOUT)));
        requestFactory.setReadTimeout(Integer.parseInt(settings
                .getProperty(HTTP_READ_TIMEOUT)));
        this.httpActionService = httpActionService;
        this.basicRestTemplate = basicRestTemplate;
        this.settings = settings;
    }

    /**
     * Handles an event and sends an http request. Request sections such as headers, url or method are built from event parameters.
     *
     * @param motechEvent the event which contains data for request
     */
    @MotechListener(subjects = EventSubjects.HTTP_REQUEST)
    public void handle(MotechEvent motechEvent) {
        Map<String, Object> parameters = motechEvent.getParameters();
        String url = String.valueOf(parameters.get(EventDataKeys.URL));
        Object requestData = parameters.get(EventDataKeys.DATA);
        String username = (String) parameters.get(EventDataKeys.USERNAME);
        String password = (String) parameters.get(EventDataKeys.PASSWORD);

        Object methodObj = parameters.get(EventDataKeys.METHOD);
        Method method = (methodObj instanceof Method) ? (Method) parameters
                .get(EventDataKeys.METHOD) : Method
                .fromString((String) methodObj);

        Map<String, String> headers = (Map<String, String>) parameters
                .get(EventDataKeys.HEADERS);
        if (headers == null) {
            headers = new HashMap<>();
        }
        HttpEntity<Object> entity = new HttpEntity<>(requestData,
                createHttpHeaders(headers));

        LOGGER.info(String.format("Posting Http request -- Url: %s, Data: %s",
                url, String.valueOf(requestData)));

        executeFor(url, entity, method, username, password);

    }

    /**
     * Handles an event and sends an http request. Request sections such as headers, url or method are built from event
     * parameters. Returns the response from the performed request. Retry count(default value is 1) and retry
     * interval(default value is 0, expressed in milliseconds) can be specified by event parameters(keys:
     * org.motechproject.http.agent.domain.EventDataKeys.RETRY_COUNT and org.motechproject.http.agent.domain.EventDataKeys.RETRY_INTERVAL).
     *
     * @param motechEvent the event which contains data for request
     * @return response from the posted request
     */
    @MotechListener(subjects = EventSubjects.SYNC_HTTP_REQUEST_RET_TYPE)
    public ResponseEntity<?> handleWithReturnType(MotechEvent motechEvent) {
        Map<String, Object> parameters = motechEvent.getParameters();
        final String url = String.valueOf(parameters.get(EventDataKeys.URL));
        final Object requestData = parameters.get(EventDataKeys.DATA);
        Object methodObj = parameters.get(EventDataKeys.METHOD);
        final Method method = (methodObj instanceof Method) ? (Method) parameters
                .get(EventDataKeys.METHOD) : Method
                .fromString((String) methodObj);
        int retryCount = 1; // default retry count = 1
        long retryInterval = 0; // by default, no waiting time between two
        // retries
        if (parameters.get(EventDataKeys.RETRY_COUNT) != null) {
            retryCount = (int) parameters.get(EventDataKeys.RETRY_COUNT);
        }
        if (parameters.get(EventDataKeys.RETRY_INTERVAL) != null) {
            retryInterval = (long) parameters.get(EventDataKeys.RETRY_INTERVAL);
        }

        return sendRequest(url, requestData, method, basicRestTemplate, retryCount, retryInterval);

    }

    /**
     * Handles an event and sends an http request with username and password parameters. Request sections such as headers, url or method are built from event
     * parameters. Returns the response from the performed request. Retry count(default value is 1) and retry
     * interval(default value is 0, expressed in milliseconds) can be specified by event parameters(keys:
     * org.motechproject.http.agent.domain.EventDataKeys.RETRY_COUNT and org.motechproject.http.agent.domain.EventDataKeys.RETRY_INTERVAL).
     *
     * @param motechEvent the event which contains data for request
     * @return response from the posted request
     */
    @MotechListener (subjects = { SendRequestConstants.SEND_REQUEST_SUBJECT })
    public ResponseEntity<?> handleWithUserPasswordAndReturnType(MotechEvent motechEvent) {
        Map<String, Object> parameters = motechEvent.getParameters();
        final String url = String.valueOf(parameters.get(SendRequestConstants.URL));
        Object requestData = parameters.get(SendRequestConstants.BODY_PARAMETERS);
        String username = String.valueOf(parameters.get(SendRequestConstants.USERNAME));
        String password = String.valueOf(parameters.get(SendRequestConstants.PASSWORD));
        boolean followRedirects = (boolean) parameters.get(SendRequestConstants.FOLLOW_REDIRECTS);
        LOGGER.info(String.format("Posting Http request -- Url: %s, Data: %s",
                url, String.valueOf(requestData)));

        HttpEntity<Object> entity = new HttpEntity<>(requestData);

        RestTemplate restTemplate;

        if (StringUtils.isNotBlank(username)
                || StringUtils.isNotBlank(password)) {
            restTemplate = new RestTemplate(usernamePasswordRequestFactory(
                    username, password));
        } else {
            restTemplate = basicRestTemplate;
        }
        if (followRedirects) {
            final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            HttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
            factory.setHttpClient(httpClient);
            restTemplate.setRequestFactory(factory);
        }

        int retryCount = 1; // default retry count = 1
        long retryInterval = 0; // by default, no waiting time between two
        // retries
        if (parameters.get(EventDataKeys.RETRY_COUNT) != null) {
            retryCount = (int) parameters.get(EventDataKeys.RETRY_COUNT);
        }
        if (parameters.get(EventDataKeys.RETRY_INTERVAL) != null) {
            retryInterval = (long) parameters.get(EventDataKeys.RETRY_INTERVAL);
        }

        return sendRequest(url, entity, Method.POST, restTemplate, retryCount, retryInterval);
    }

    private ResponseEntity<?> sendRequest(String url, Object requestData, Method method, RestTemplate restTemplate, int retryCount, long retryInterval)
    {
        ResponseEntity<?> retValue = null;
        Callable<ResponseEntity<?>> task = new Callable<ResponseEntity<?>>() {
            public ResponseEntity<?> call() throws HttpException {

                LOGGER.info("Posting Http request -- Url: {}, Data: {}",
                        url, String.valueOf(requestData));
                ResponseEntity<?> response = doExecuteForReturnType(url, requestData, method, restTemplate);
                if (response.getStatusCode().value() / HUNDRED == 2) {
                    return response;
                } else {
                    throw new HttpException();
                }
            }
        };
        // Creating new instance of RetriableTask to run the callable
        RetriableTask<ResponseEntity<?>> r = new RetriableTask<>(
                retryCount, retryInterval, task);
        try {
            retValue = r.call();
        } catch (Exception e) { // Http request failed for all retries
            LOGGER.error("Posting Http request -- Url: {}, Data: {} failed after {} retries at interval of {} ms.",
                    url, String.valueOf(requestData), String.valueOf(retryCount), String.valueOf(retryInterval),
                    e);
        }
        return retValue;
    }

    private ResponseEntity<?> executeFor(String url, Object requestData,
                            Method method, String username, String password) {
        RestTemplate restTemplate;

        if (StringUtils.isNotBlank(username)
                || StringUtils.isNotBlank(password)) {
            restTemplate = new RestTemplate(usernamePasswordRequestFactory(
                    username, password));
        } else {
            restTemplate = basicRestTemplate;
        }
        return doExecuteForReturnType(url, requestData, method, restTemplate);
    }

    private ResponseEntity<?> doExecuteForReturnType(String url, Object requestData, Method method,
                                                     RestTemplate restTemplate) {

        ResponseEntity<?> response = method.execute(restTemplate, url, requestData);
        HTTPActionAudit httpActionAudit = new HTTPActionAudit(url, requestData.toString(), response.getBody().toString(),
                response.getStatusCode().toString());
        httpActionService.create(httpActionAudit);

        return response;
    }

    private HttpComponentsClientHttpRequestFactoryWithAuth usernamePasswordRequestFactory(
            String username, String password) {
        HttpComponentsClientHttpRequestFactoryWithAuth requestFactory = new HttpComponentsClientHttpRequestFactoryWithAuth(
                username, password);

        requestFactory.setConnectTimeout(Integer.parseInt(settings
                .getProperty(HTTP_CONNECT_TIMEOUT)));
        requestFactory.setReadTimeout(Integer.parseInt(settings
                .getProperty(HTTP_READ_TIMEOUT)));

        return requestFactory;
    }

    private HttpHeaders createHttpHeaders(Map<String, String> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        for (String param : headers.keySet()) {
            httpHeaders.add(param, headers.get(param));
        }

        return httpHeaders;
    }
}
