package org.motechproject.openmrs.resource.impl;

import org.apache.commons.httpclient.HttpClient;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.GeneratedIdentifier;
import org.motechproject.openmrs.resource.GeneratedIdentifierResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

@Component
public class GeneratedIdentifierResourceImpl extends BaseResource implements GeneratedIdentifierResource {

    @Autowired
    public GeneratedIdentifierResourceImpl(RestOperations restOperations, HttpClient httpClient) {
        super(restOperations, httpClient);
    }

    @Override
    public GeneratedIdentifier getGeneratedIdentifier(Config config, String sourceName) {
        String value = getJson(config, "/idgen/latestidentifier?sourceName={name}", sourceName);

        GeneratedIdentifier identifier = new GeneratedIdentifier();
        identifier.setValue(value);

        return identifier;
    }
}
