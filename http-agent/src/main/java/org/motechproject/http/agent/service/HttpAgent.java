package org.motechproject.http.agent.service;

import java.util.Map;

import org.motechproject.http.agent.domain.Credentials;
import org.springframework.http.ResponseEntity;

/**
 * An OSGI Service for sending http requests.
 */
public interface HttpAgent {

    /**
     * Executes the http request asynchronously.
     * @param url the url for request
     * @param data the request data
     * @param method the http method
     */
    void execute(String url, Object data, Method method);

    /**
     * Executes the http request synchronously.
     * @param url the url for request
     * @param data the request data
     * @param method the http method
     */
    void executeSync(String url, Object data, Method method);

    /**
     * Executes the http request asynchronously.
     * @param url the url for request
     * @param data the request data
     * @param method the http method
     * @param headers the request headers
     */
    void execute(String url, Object data, Method method,
            Map<String, String> headers);

    /**
     * Executes the http request synchronously.
     * @param url the url for request
     * @param data the request data
     * @param method the http method
     * @param headers the request headers
     */
    void executeSync(String url, Object data, Method method,
            Map<String, String> headers);

    /**
     * Executes the http request asynchronously.
     * @param url the url for request
     * @param data the request data
     * @param method the http method
     * @param credentials the user credentials
     * @see org.motechproject.http.agent.domain.Credentials
     */
    void execute(String url, Object data, Method method, Credentials credentials);

    /**
     * Executes the http request synchronously.
     * @param url the url for request
     * @param data the request data
     * @param method the http method
     * @param credentials the user credentials
     * @see org.motechproject.http.agent.domain.Credentials
     */
    void executeSync(String url, Object data, Method method,
            Credentials credentials);

    /**
     * Executes the http request asynchronously.
     * @param url the url for request
     * @param data the request data
     * @param method the http method
     * @param headers the request headers
     * @param credentials the user credentials
     * @see org.motechproject.http.agent.domain.Credentials
     */
    void execute(String url, Object data, Method method,
            Map<String, String> headers, Credentials credentials);

    /**
     * Executes the http request synchronously.
     * @param url the url for request
     * @param data the request data
     * @param method the http method
     * @param headers the request headers
     * @param credentials the user credentials
     * @see org.motechproject.http.agent.domain.Credentials
     */
    void executeSync(String url, Object data, Method method,
            Map<String, String> headers, Credentials credentials);

    /**
     * Executes the http request synchronously and returns the response.
     * @param url the url for request
     * @param data the request data
     * @param method the http method
     * @return response from posted request
     */
    ResponseEntity<?> executeWithReturnTypeSync(String url, Object data,
            Method method);

    /**
     * Executes the http request synchronously and returns the response, takes additional parameter for number of retries.
     * @param url the url for request
     * @param data the request data
     * @param method the http method
     * @param retryCount the number of retries
     * @return response from posted request
     */
    ResponseEntity<?> executeWithReturnTypeSync(String url, Object data,
            Method method, Integer retryCount);

    /**
     * Executes the http request synchronously and returns the response, takes additional parameters for number of retries
     * and interval between two retries.
     * @param url the url for request
     * @param data the request data
     * @param method the http method
     * @param retryCount the number of retries
     * @param retryInterval the interval between two retries in milliseconds
     * @return response from posted request
     */
    ResponseEntity<?> executeWithReturnTypeSync(String url, Object data,
            Method method, Integer retryCount, Long retryInterval);

}
