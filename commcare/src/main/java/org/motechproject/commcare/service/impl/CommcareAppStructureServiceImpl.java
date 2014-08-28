package org.motechproject.commcare.service.impl;

import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
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

    static final int DEFAULT_PAGE_SIZE = 20;

    private MotechJsonReader motechJsonReader;

    private CommCareAPIHttpClient commcareHttpClient;

    @Autowired
    public CommcareAppStructureServiceImpl(CommCareAPIHttpClient commcareHttpClient) {
        this.commcareHttpClient = commcareHttpClient;
        this.motechJsonReader = new MotechJsonReader();
    }

    @Override
    public List<CommcareApplicationJson> getAllApplications() {
        AppStructureResponseJson appStructureResponseJson = null;
        Integer pageNumber = 1;
        List<CommcareApplicationJson> commcareApp = new ArrayList<>();

        do {
            String response = commcareHttpClient.appStructureRequest(DEFAULT_PAGE_SIZE, pageNumber);
            appStructureResponseJson = parseApplicationsFromResponse(response);
            commcareApp.addAll(appStructureResponseJson.getApplications());
            pageNumber++;
        } while (appStructureResponseJson != null
                && StringUtils.isNotBlank(appStructureResponseJson.getMetadata().getNextPageQueryString()));

        return commcareApp;
    }

    public List<CommcareApplicationJson> getApplications(Integer pageSize, Integer pageNumber) {
        String response = commcareHttpClient.appStructureRequest(pageSize, pageNumber);
        AppStructureResponseJson appStructureResponseJson = parseApplicationsFromResponse(response);
        return appStructureResponseJson.getApplications();
    }

    private AppStructureResponseJson parseApplicationsFromResponse(String response) {
        Type appStructureResponseType = new TypeToken<AppStructureResponseJson>() { } .getType();
        return (AppStructureResponseJson) motechJsonReader.readFromStringOnlyExposeAnnotations(response, appStructureResponseType);
    }
}
