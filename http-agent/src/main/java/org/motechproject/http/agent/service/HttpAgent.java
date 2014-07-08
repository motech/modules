package org.motechproject.http.agent.service;

import java.util.Map;

import org.motechproject.http.agent.domain.Credentials;
import org.springframework.http.ResponseEntity;

public interface HttpAgent {

    void execute(String url, Object data, Method method);

    void executeSync(String url, Object data, Method method);

    void execute(String url, Object data, Method method,
            Map<String, String> headers);

    void executeSync(String url, Object data, Method method,
            Map<String, String> headers);

    void execute(String url, Object data, Method method, Credentials credentials);

    void executeSync(String url, Object data, Method method,
            Credentials credentials);

    void execute(String url, Object data, Method method,
            Map<String, String> headers, Credentials credentials);

    void executeSync(String url, Object data, Method method,
            Map<String, String> headers, Credentials credentials);

    /**
     * Executes the Http Request and returns the response
     */
    ResponseEntity<?> executeWithReturnTypeSync(String url, Object data,
            Method method);

    /**
     * Executes the Http Request and returns the response, takes additional
     * parameter for number of retries
     */
    ResponseEntity<?> executeWithReturnTypeSync(String url, Object data,
            Method method, Integer retryCount);

    /**
     * Executes the Http Request and returns the response, takes additional
     * parameters for number of retries and interval between two retries
     */
    ResponseEntity<?> executeWithReturnTypeSync(String url, Object data,
            Method method, Integer retryCount, Long retryInterval);

}
