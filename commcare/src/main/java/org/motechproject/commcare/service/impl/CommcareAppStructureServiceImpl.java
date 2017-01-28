package org.motechproject.commcare.service.impl;

import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.motechproject.commcare.client.CommCareAPIHttpClient;
import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.domain.AppStructureResponseJson;
import org.motechproject.commcare.domain.CommcareApplicationJson;
import org.motechproject.commcare.service.CommcareAppStructureService;
import org.motechproject.commcare.service.CommcareConfigService;
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

    private CommcareConfigService configService;

    @Autowired
    public CommcareAppStructureServiceImpl(CommCareAPIHttpClient commcareHttpClient, CommcareConfigService configService) {
        this.commcareHttpClient = commcareHttpClient;
        this.configService = configService;
        this.motechJsonReader = new MotechJsonReader();
    }

    @Override
    public List<CommcareApplicationJson> getAllApplications(String configName) {
        AppStructureResponseJson appStructureResponseJson;
        Integer pageNumber = 1;
        List<CommcareApplicationJson> commcareApps = new ArrayList<>();
        Config config = configService.getByName(configName);

        do {
            String response = commcareHttpClient.appStructureRequest(config.getAccountConfig(), DEFAULT_PAGE_SIZE, pageNumber);
            appStructureResponseJson = parseApplicationsFromResponse(response, config.getName());
            commcareApps.addAll(appStructureResponseJson.getApplications());
            pageNumber++;
        } while (StringUtils.isNotBlank(appStructureResponseJson.getMetadata().getNextPageQueryString()));

        // Make sure the modules get serialized
        for (CommcareApplicationJson applicationJson : commcareApps) {
            applicationJson.serializeModules();
        }

        return commcareApps;
    }

    public List<CommcareApplicationJson> getApplications(Integer pageSize, Integer pageNumber, String configName) {
        Config config = configService.getByName(configName);
        String response = commcareHttpClient.appStructureRequest(config.getAccountConfig(), pageSize, pageNumber);
        AppStructureResponseJson appStructureResponseJson = parseApplicationsFromResponse(response, config.getName());
        return appStructureResponseJson.getApplications();
    }

    @Override
    public List<CommcareApplicationJson> getAllApplications() {
        return getAllApplications(null);
    }

    public List<CommcareApplicationJson> getApplications(Integer pageSize, Integer pageNumber) {
        return getApplications(pageSize, pageNumber, null);
    }

    private AppStructureResponseJson parseApplicationsFromResponse(String response, String configName) {
        Type appStructureResponseType = new TypeToken<AppStructureResponseJson>() { } .getType();
        AppStructureResponseJson structure = (AppStructureResponseJson) motechJsonReader
                .readFromStringOnlyExposeAnnotations(response, appStructureResponseType);

        setDomain(structure, configName);

        return structure;
    }

    private void setDomain(AppStructureResponseJson responseJson, String configName) {
        if (responseJson != null) {
            for (CommcareApplicationJson application : responseJson.getApplications()) {
                application.setConfigName(configName);
            }
        }
    }
}
