package org.motechproject.commcare.service.impl;

import com.google.gson.reflect.TypeToken;
import org.motechproject.commcare.client.CommCareAPIHttpClient;
import org.motechproject.commcare.domain.AppStructureResponseJson;
import org.motechproject.commcare.domain.CommcareApplicationJson;
import org.motechproject.commcare.service.CommcareAppStructureService;
import org.motechproject.commons.api.json.MotechJsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommcareAppStructureServiceImpl implements CommcareAppStructureService {

    private MotechJsonReader motechJsonReader;
    private CommCareAPIHttpClient commcareHttpClient;

    @Autowired
    public CommcareAppStructureServiceImpl(CommCareAPIHttpClient commcareHttpClient) {
        this.commcareHttpClient = commcareHttpClient;
        this.motechJsonReader = new MotechJsonReader();
    }

    @Override
    public List<CommcareApplicationJson> getAllApplications() {
        String response = commcareHttpClient.appStructureRequest();
        AppStructureResponseJson appStructureResponseJson = parseApplicationsFromResponse(response);

        List<CommcareApplicationJson> appJsons = new ArrayList<>();
        appJsons.addAll(appStructureResponseJson.getApplications());

        return appJsons;
    }

    private AppStructureResponseJson parseApplicationsFromResponse(String response) {
        Type appStructureResponseType = new TypeToken<AppStructureResponseJson>() { } .getType();
        return (AppStructureResponseJson) motechJsonReader.readFromString(response, appStructureResponseType);
    }
}
