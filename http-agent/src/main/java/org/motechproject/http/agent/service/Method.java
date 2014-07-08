package org.motechproject.http.agent.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public enum Method {
    POST {
        @Override
        public void execute(RestTemplate restTemplate, String url,
                Object request) {
            restTemplate.postForLocation(url, request);
        }

        @Override
        public ResponseEntity<?> executeWithReturnType(
                RestTemplate restTemplate, String url, Object request) {
            return restTemplate.exchange(url, HttpMethod.POST,
                    (HttpEntity<?>) request, String.class);
        }
    },

    PUT {
        @Override
        public void execute(RestTemplate restTemplate, String url,
                Object request) {
            restTemplate.put(url, request);
        }

        @Override
        public ResponseEntity<?> executeWithReturnType(
                RestTemplate restTemplate, String url, Object request) {
            return restTemplate.exchange(url, HttpMethod.PUT,
                    (HttpEntity<?>) request, String.class);
        }
    },

    DELETE {
        @Override
        public void execute(RestTemplate restTemplate, String url,
                Object request) {
            restTemplate.delete(url, request);
        }

        @Override
        public ResponseEntity<?> executeWithReturnType(
                RestTemplate restTemplate, String url, Object request) {
            return restTemplate.exchange(url, HttpMethod.DELETE,
                    (HttpEntity<?>) request, String.class);
        }
    },

    GET {

        @Override
        public void execute(RestTemplate restTemplate, String url,
                Object request) {
            restTemplate.getForObject(url, String.class,
                    (HttpEntity<?>) request);

        }

        @Override
        public ResponseEntity<?> executeWithReturnType(
                RestTemplate restTemplate, String url, Object request) {
            return restTemplate.exchange(url, HttpMethod.GET,
                    (HttpEntity<?>) request, String.class);
        }

    };

    public abstract void execute(RestTemplate restTemplate, String url,
            Object request);

    public static Method fromString(String str) {
        for (Method method : values()) {
            if (StringUtils.equalsIgnoreCase(method.name(), str)) {
                return method;
            }
        }
        return POST;
    }

    public abstract ResponseEntity<?> executeWithReturnType(
            RestTemplate restTemplate, String url, Object request);

}
