package org.motechproject.openmrs19.resource.impl;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.motechproject.openmrs19.config.Config;
import org.motechproject.openmrs19.util.JsonUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.InputStream;
import java.util.Arrays;

public abstract class AbstractResourceImplTest {

    protected String readJsonFromFile(String filename) throws Exception {
        Resource resource = new ClassPathResource(filename);
        String json;
        try(InputStream is = resource.getInputStream()) {
            json = IOUtils.toString(is);
        }

        return json;
    }

    protected ResponseEntity<String> getResponse(String file) throws Exception {
        return new ResponseEntity<>(readJsonFromFile(file), HttpStatus.OK);
    }

    protected Object readFromFile(String filename, Class type) throws Exception {
        return JsonUtils.readJson(readJsonFromFile(filename), type);
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
