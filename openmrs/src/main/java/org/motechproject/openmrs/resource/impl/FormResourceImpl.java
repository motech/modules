package org.motechproject.openmrs.resource.impl;

import org.apache.commons.httpclient.HttpClient;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.Form;
import org.motechproject.openmrs.resource.FormResource;
import org.motechproject.openmrs.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

@Component
public class FormResourceImpl extends BaseResource implements FormResource {

    @Autowired
    public FormResourceImpl(RestOperations restOperations, HttpClient httpClient) {
        super(restOperations, httpClient);
    }

    @Override
    public Form getFormByUuid(Config config, String uuid) {
        String responseJson = getJson(config, "/form/{uuid}", uuid);
        return (Form) JsonUtils.readJson(responseJson, Form.class);
    }

}
