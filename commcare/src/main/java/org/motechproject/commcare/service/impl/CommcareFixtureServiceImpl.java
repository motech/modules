package org.motechproject.commcare.service.impl;

import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import org.motechproject.commcare.client.CommCareAPIHttpClient;
import org.motechproject.commcare.domain.CommcareFixture;
import org.motechproject.commcare.domain.CommcareFixturesJson;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.service.CommcareFixtureService;
import org.motechproject.commons.api.json.MotechJsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class CommcareFixtureServiceImpl implements CommcareFixtureService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommcareFixtureServiceImpl.class);

    private CommCareAPIHttpClient commcareHttpClient;
    private CommcareConfigService configService;
    private MotechJsonReader motechJsonReader;

    @Autowired
    public CommcareFixtureServiceImpl(CommCareAPIHttpClient commcareHttpClient, CommcareConfigService configService) {
        this.commcareHttpClient = commcareHttpClient;
        this.configService = configService;
        this.motechJsonReader = new MotechJsonReader();
    }

    @Override
    public List<CommcareFixture> getFixtures(Integer pageSize, Integer pageNumber, String configName) {
        String response = commcareHttpClient.fixturesRequest(configService.getByName(configName).getAccountConfig(),
                pageSize, pageNumber);
        Type commcareFixtureType = new TypeToken<CommcareFixturesJson>() { } .getType();
        CommcareFixturesJson allFixtures = (CommcareFixturesJson) motechJsonReader.readFromString(response, commcareFixtureType);

        return allFixtures.getObjects();

    }

    @Override
    public CommcareFixture getCommcareFixtureById(String id, String configName) {
        String returnJson = commcareHttpClient.fixtureRequest(configService.getByName(configName).getAccountConfig(), id);

        Type commcareFixtureType = new TypeToken<CommcareFixture>() { } .getType();
        CommcareFixture fixture = null;

        try {
            fixture = (CommcareFixture) motechJsonReader.readFromString(returnJson, commcareFixtureType);
        } catch (JsonParseException e) {
            LOGGER.info("Unable to parse JSON from Commcare: " + returnJson);
        }

        return fixture;
    }

    @Override
    public List<CommcareFixture> getFixtures(Integer pageSize, Integer pageNumber) {
        return getFixtures(pageSize, pageNumber, null);
    }

    @Override
    public CommcareFixture getCommcareFixtureById(String id) {
        return getCommcareFixtureById(id, null);
    }
}
