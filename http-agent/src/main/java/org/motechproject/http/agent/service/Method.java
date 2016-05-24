package org.motechproject.http.agent.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Enum which represents http methods.
 */
public enum Method {

    /**
     * Represents http POST.
     */
    POST {
        @Override
        public ResponseEntity<?> execute(
                RestTemplate restTemplate, String url, Object request) {
            return restTemplate.exchange(url, HttpMethod.POST,
                    (HttpEntity<?>) request, String.class);
        }
    },

    /**
     * Represents http PUT.
     */
    PUT {
        @Override
        public ResponseEntity<?> execute(
                RestTemplate restTemplate, String url, Object request) {
            return restTemplate.exchange(url, HttpMethod.PUT,
                    (HttpEntity<?>) request, String.class);
        }
    },

    /**
     * Represents http DELETE.
     */
    DELETE {
        @Override
        public ResponseEntity<?> execute(
                RestTemplate restTemplate, String url, Object request) {
            return restTemplate.exchange(url, HttpMethod.DELETE,
                    (HttpEntity<?>) request, String.class);
        }
    },

    /**
     * Represents http GET.
     */
    GET {
        @Override
        public ResponseEntity<?> execute(
                RestTemplate restTemplate, String url, Object request) {
            return restTemplate.exchange(url, HttpMethod.GET,
                    (HttpEntity<?>) request, String.class);
        }

    };

    /**
     * Sends a request to the specified URL through the given restTemplate and returns the response for the request.
     * @param restTemplate the client which will be used to send the request
     * @param url the url for the request
     * @param request the request to send
     * @return response from the posted request
     */
    public abstract ResponseEntity<?> execute(RestTemplate restTemplate, String url, Object request);

    /**
     * Returns method type from the given string.
     * @param str the method name
     * @return method type
     */
    public static Method fromString(String str) {
        for (Method method : values()) {
            if (StringUtils.equalsIgnoreCase(method.name(), str)) {
                return method;
            }
        }
        return POST;
    }

}
