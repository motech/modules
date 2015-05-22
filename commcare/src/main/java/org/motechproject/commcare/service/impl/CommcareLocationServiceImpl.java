package org.motechproject.commcare.service.impl;

import com.google.gson.reflect.TypeToken;
import org.motechproject.commcare.client.CommCareAPIHttpClient;
import org.motechproject.commcare.config.AccountConfig;
import org.motechproject.commcare.domain.CommcareLocation;
import org.motechproject.commcare.domain.CommcareLocationsJson;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.service.CommcareLocationService;
import org.motechproject.commons.api.json.MotechJsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class CommcareLocationServiceImpl implements CommcareLocationService {

    private MotechJsonReader motechJsonReader;

    private CommCareAPIHttpClient commcareHttpClient;

    private CommcareConfigService configService;

    @Autowired
    public CommcareLocationServiceImpl(CommCareAPIHttpClient commcareHttpClient, CommcareConfigService configService) {
        this.commcareHttpClient = commcareHttpClient;
        this.configService = configService;
        this.motechJsonReader = new MotechJsonReader();
    }

    @Override
    public CommcareLocation getCommcareLocationById(String id, String configName) {
        String response = commcareHttpClient.locationRequest(getAccountConfig(configName), id);
        Type commcareLocationType = new TypeToken<CommcareLocation>() {
        } .getType();

        return (CommcareLocation) motechJsonReader.readFromString(response, commcareLocationType);
    }

    @Override
    public CommcareLocation getCommcareLocationById(String id) {
        return getCommcareLocationById(id, null);
    }

    @Override
    public List<CommcareLocation> getCommcareLocations(Integer pageSize, Integer pageNumber, String configName) {
        String response = commcareHttpClient.locationsRequest(getAccountConfig(configName), pageSize, pageNumber);
        Type commcareLocationsType = new TypeToken<CommcareLocationsJson>() {
        } .getType();
        CommcareLocationsJson allLocations = (CommcareLocationsJson) motechJsonReader.readFromString(response, commcareLocationsType);

        return allLocations.getObjects();
    }

    @Override
    public List<CommcareLocation> getCommcareLocations(Integer pageSize, Integer pageNumber) {
        return getCommcareLocations(pageSize, pageNumber, null);
    }

    private AccountConfig getAccountConfig(String configName) {
        return configService.getByName(configName).getAccountConfig();
    }

}
