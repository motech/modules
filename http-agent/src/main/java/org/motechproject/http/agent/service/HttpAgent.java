package org.motechproject.http.agent.service;

import org.motechproject.http.agent.domain.Credentials;

import java.util.Map;

public interface HttpAgent {

    void execute(String url, Object data, Method method);

    void executeSync(String url, Object data, Method method);

    void execute(String url, Object data, Method method, Map<String, String> headers);

    void executeSync(String url, Object data, Method method, Map<String, String> headers);

    void execute(String url, Object data, Method method, Credentials credentials);

    void executeSync(String url, Object data, Method method, Credentials credentials);

    void execute(String url, Object data, Method method, Map<String, String> headers, Credentials credentials);

    void executeSync(String url, Object data, Method method, Map<String, String> headers, Credentials credentials);

}
