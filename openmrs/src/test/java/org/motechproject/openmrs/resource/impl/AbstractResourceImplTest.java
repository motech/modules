package org.motechproject.openmrs.resource.impl;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.util.JsonUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;

public abstract class AbstractResourceImplTest {

    protected String readJsonFromFile(String filename) throws Exception {
        Resource resource = new ClassPathResource(filename);
        String json;
        try(InputStream is = resource.getInputStream()) {
            json = IOUtils.toString(is);
        }

        return json;
    }

    protected ResponseEntity<String> getResponse(String response) {
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    protected ResponseEntity<String> getResponseFromFile(String file) throws Exception {
        return new ResponseEntity<>(readJsonFromFile(file), HttpStatus.OK);
    }

    protected Object readFromFile(String filename, Class type) throws Exception {
        return JsonUtils.readJson(readJsonFromFile(filename), type);
    }

    protected Object readFromFile(String filename, Class type, Map<Type, Object> adapters) throws Exception {
        return JsonUtils.readJsonWithAdapters(readJsonFromFile(filename), type, adapters);
    }

    protected HttpHeaders getHeadersForPost(Config config) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.ALL));
        headers.add("Authorization", "Basic " + prepareCredentials(config));
        return headers;
    }

    protected HttpHeaders getHeadersForPostWithoutResponse(Config config) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic " + prepareCredentials(config));
        return headers;
    }

    protected HttpHeaders getHeadersForGet(Config config) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + prepareCredentials(config));
        return headers;
    }

    private String prepareCredentials(Config config) {
        return new String(Base64.encodeBase64(
                String.format("%s:%s", config.getUsername(), config.getPassword()).getBytes()
        ));
    }
}
