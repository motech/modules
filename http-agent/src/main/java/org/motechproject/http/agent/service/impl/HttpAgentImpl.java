package org.motechproject.http.agent.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpException;
import org.motechproject.event.MotechEvent;
import org.motechproject.http.agent.components.AsynchronousCall;
import org.motechproject.http.agent.components.SynchronousCall;
import org.motechproject.http.agent.domain.Credentials;
import org.motechproject.http.agent.domain.EventDataKeys;
import org.motechproject.http.agent.domain.EventSubjects;
import org.motechproject.http.agent.service.HttpAgent;
import org.motechproject.http.agent.service.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link org.motechproject.http.agent.service.HttpAgent}.
 * This service allows making HTTP requests.
 */
@Service
public class HttpAgentImpl implements HttpAgent {

    private AsynchronousCall asynchronousCall;
    private SynchronousCall synchronousCall;

    @Autowired
    public HttpAgentImpl(AsynchronousCall asynchronousCall,
            SynchronousCall synchronousCall) {
        this.asynchronousCall = asynchronousCall;
        this.synchronousCall = synchronousCall;
    }

    @Override
    public void execute(String url, Object data, Method method) {
        execute(url, data, method, null, null);
    }

    @Override
    public void executeSync(String url, Object data, Method method) {
        executeSync(url, data, method, null, null);
    }

    @Override
    public void execute(String url, Object data, Method method, Map headers) {
        execute(url, data, method, headers, null);
    }

    @Override
    public void executeSync(String url, Object data, Method method, Map headers) {
        executeSync(url, data, method, headers, null);
    }

    @Override
    public void execute(String url, Object data, Method method,
            Credentials credentials) {
        execute(url, data, method, null, credentials);
    }

    @Override
    public void executeSync(String url, Object data, Method method,
            Credentials credentials) {
        executeSync(url, data, method, null, credentials);
    }

    @Override
    public void execute(String url, Object data, Method method, Map headers,
            Credentials credentials) {
        Map<String, Object> parameters = constructParametersFrom(url, data,
                method, headers, credentials);
        sendMessage(parameters);
    }

    @Override
    public void executeSync(String url, Object data, Method method,
            Map headers, Credentials credentials) {
        Map<String, Object> parameters = constructParametersFrom(url, data,
                method, headers, credentials);
        sendMessageSync(parameters);
    }

    @Override
    public ResponseEntity<?> executeWithReturnTypeSync(String url, Object data,
            Method method) throws HttpException {
        Map<String, Object> parameters = constructParametersForReturnType(url, data,
                method, null, null);
        return sendMessageWithReturnTypeSync(parameters);
    }

    @Override
    public ResponseEntity<?> executeWithReturnTypeSync(String url, Object data,
            Method method, Integer retryCount) throws HttpException {
        Map<String, Object> parameters = constructParametersForReturnType(url, data,
                method, retryCount, null);
        return sendMessageWithReturnTypeSync(parameters);
    }

    @Override
    public ResponseEntity<?> executeWithReturnTypeSync(String url, Object data,
            Method method, Integer retryCount, Long retryInterval) throws HttpException {
        Map<String, Object> parameters = constructParametersForReturnType(url, data,
                method, retryCount, retryInterval);
        return sendMessageWithReturnTypeSync(parameters);
    }

    private Map<String, Object> constructParametersFrom(String url,
            Object data, Method method, Map<String, String> headers,
            Credentials credentials) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(EventDataKeys.URL, url);
        parameters.put(EventDataKeys.METHOD, method);
        parameters.put(EventDataKeys.DATA, data);

        if (headers != null && !headers.isEmpty()) {
            parameters.put(EventDataKeys.HEADERS, headers);
        }
        if (credentials != null && !credentials.getUsername().isEmpty()) {
            parameters.put(EventDataKeys.USERNAME, credentials.getUsername());
        }
        if (credentials != null && !credentials.getPassword().isEmpty()) {
            parameters.put(EventDataKeys.PASSWORD, credentials.getPassword());
        }

        return parameters;
    }

    private Map<String, Object> constructParametersForReturnType(String url,
            Object data, Method method, Integer retryCount, Long retryInterval) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(EventDataKeys.URL, url);
        parameters.put(EventDataKeys.METHOD, method);
        parameters.put(EventDataKeys.DATA, data);
        parameters.put(EventDataKeys.RETRY_COUNT, retryCount);
        parameters.put(EventDataKeys.RETRY_INTERVAL, retryInterval);
        return parameters;
    }

    private void sendMessage(Map<String, Object> parameters) {
        MotechEvent motechEvent = new MotechEvent(EventSubjects.HTTP_REQUEST,
                parameters);
        asynchronousCall.send(motechEvent);
    }

    private void sendMessageSync(Map<String, Object> parameters) {
        MotechEvent motechEvent = new MotechEvent(EventSubjects.HTTP_REQUEST,
                parameters);
        synchronousCall.send(motechEvent);
    }

    private ResponseEntity<?> sendMessageWithReturnTypeSync (
            Map<String, Object> parameters) throws HttpException {
        MotechEvent motechEvent = new MotechEvent(
                EventSubjects.SYNC_HTTP_REQUEST_RET_TYPE, parameters);
        return synchronousCall.sendWithReturnType(motechEvent);
    }
}