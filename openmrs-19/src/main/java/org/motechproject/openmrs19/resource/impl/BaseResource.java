package org.motechproject.openmrs19.resource.impl;

import org.apache.commons.codec.binary.Base64;
import org.motechproject.openmrs19.config.Config;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestOperations;

import java.net.URI;
import java.util.Arrays;

/**
 * Serves as a base for all implementation of the resource interfaces. Provides method for basic REST operations with
 * the OpenMRS servers.
 */
public abstract class BaseResource {

    private RestOperations restOperations;

    protected BaseResource(RestOperations restOperations) {
        this.restOperations = restOperations;
    }

    /**
     * Sends a get request to the OpenMRS server using the given {@code config}.
     *
     * @param config  the configuration to be used
     * @param path  the path to the resource
     * @param params  the path parameters
     * @return the response json
     */
    protected String getJson(Config config, String path, Object... params) {
        return exchange(config, buildUrl(config, path, params), HttpMethod.GET).getBody();
    }

    /**
     * Sends a post request to the OpenMRS server using the given {@code config}.
     *
     * @param config  the configuration to be used
     * @param json  the json to be sent
     * @param path  the path to the resource
     * @param params  the path parameters
     * @return the response json
     */
    protected String postForJson(Config config, String json, String path, Object... params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.ALL));

        return exchange(config, buildUrl(config, path, params), HttpMethod.POST, json, headers).getBody();
    }

    /**
     * Sends a post request to the OpenMRS server using the given {@code config}. This request does not return response.
     *
     * @param config  the configuration to be used
     * @param json  the json to be sent
     * @param path  the path to the resource
     * @param params  the path parameters
     */
    protected void postWithEmptyResponseBody(Config config, String json, String path, Object... params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        exchange(config, buildUrl(config, path, params), HttpMethod.POST, json, headers);
    }

    /**
     * Sends a delete request to the OpenMRS server using the given {@code config}.
     *
     * @param config  the configuration to be used
     * @param path  the path to the resource
     * @param params  the path parameters
     */
    protected void delete(Config config, String path, Object... params) {
        exchange(config, buildUrl(config, path, params), HttpMethod.DELETE);
    }

    private ResponseEntity<String> exchange(Config config, URI url, HttpMethod method) {
        return exchange(config, url, method, null, new HttpHeaders());
    }

    private ResponseEntity<String> exchange(Config config, URI url, HttpMethod method, String body, HttpHeaders headers) {
        headers.add("Authorization", "Basic " + prepareCredentials(config));
        return restOperations.exchange(url, method, new HttpEntity<>(body, headers), String.class);
    }

    private String prepareCredentials(Config config) {
        return new String(Base64.encodeBase64(
                String.format("%s:%s", config.getUsername(), config.getPassword()).getBytes()
        ));
    }

    private URI buildUrl(Config config, String path, Object... params) {
        URI url;
        if (params.length > 0) {
            url = config.toInstancePathWithParams(path, params);
        } else {
            url = config.toInstancePath(path);
        }
        return url;
    }
}
