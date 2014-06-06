package org.motechproject.commcare.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.httpclient.HttpStatus;
import org.motechproject.commcare.domain.CommcareDataForwardingEndpoint;
import org.motechproject.commcare.domain.CommcareDataForwardingEndpointsJson;
import org.motechproject.commcare.service.CommcareDataForwardingEndpointService;
import org.motechproject.commcare.client.CommCareAPIHttpClient;
import org.motechproject.commons.api.json.MotechJsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class CommcareDataForwardingEndpointServiceImpl implements CommcareDataForwardingEndpointService {

    private MotechJsonReader motechJsonReader;

    private CommCareAPIHttpClient commcareHttpClient;

    @Autowired
    public CommcareDataForwardingEndpointServiceImpl(CommCareAPIHttpClient commcareHttpClient) {
        this.commcareHttpClient = commcareHttpClient;
        this.motechJsonReader = new MotechJsonReader();
    }

    @Override
    public List<CommcareDataForwardingEndpoint> getAllDataForwardingEndpoints() {
        String response = commcareHttpClient.dataForwardingEndpointsRequest();

        Type commcareDataForwardingEndpointType = new TypeToken<CommcareDataForwardingEndpointsJson>() {
        } .getType();

        CommcareDataForwardingEndpointsJson allDataForwardingEndpoints =
                (CommcareDataForwardingEndpointsJson) motechJsonReader
                .readFromString(response, commcareDataForwardingEndpointType);

        return allDataForwardingEndpoints.getObjects();
    }

    @Override
    public boolean createNewDataForwardingRule(CommcareDataForwardingEndpoint newForwardingRule) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String outputData = gson.toJson(newForwardingRule);

        int response = commcareHttpClient.dataForwardingEndpointUploadRequest(outputData);

        return response == HttpStatus.SC_CREATED; // HTTP 201
    }

    @Override
    public boolean updateDataForwardingRule(CommcareDataForwardingEndpoint updatedForwardingRule) {
        String resourceId = updatedForwardingRule.getId();

        Gson gson = new Gson();
        String outputData = gson.toJson(updatedForwardingRule);

        int response = commcareHttpClient.dataForwardingEndpointUpdateRequest(resourceId, outputData);

        return response == HttpStatus.SC_NO_CONTENT; // HTTP 204
    }
}
