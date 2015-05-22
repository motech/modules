package org.motechproject.commcare.service.impl;


import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.motechproject.commcare.client.CommCareAPIHttpClient;
import org.motechproject.commcare.config.AccountConfig;
import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.config.Configs;
import org.motechproject.commcare.domain.CommcareDataForwardingEndpoint;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.exception.CommcareAuthenticationException;
import org.motechproject.commcare.exception.CommcareConnectionFailureException;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.service.CommcareDataForwardingEndpointService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.server.config.SettingsFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.motechproject.commcare.config.Config.CONFIG_NAME;
import static org.motechproject.commcare.config.Config.CONFIG_BASE_URL;
import static org.motechproject.commcare.config.Config.CONFIG_DOMAIN;
import static org.motechproject.commcare.config.Config.CONFIG_USERNAME;
import static org.motechproject.commcare.config.Config.CONFIG_PASSWORD;

@Service
public class CommcareConfigServiceImpl implements CommcareConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommcareConfigServiceImpl.class);

    private static final String COMMCARE_CONFIGS_FILE_NAME = "commcare-configs.json";

    private static final String CASE_EVENT_STRATEGY_KEY = "eventStrategy";

    private static final String FORWARD_CASES_KEY = "forwardCases";
    private static final String FORWARD_FORMS_KEY = "forwardForms";
    private static final String FORWARD_FORM_STUBS_KEY = "forwardFormStubs";
    private static final String FORWARD_SCHEMA_CHANGES_KEY = "forwardAppStructure";

    private static final String FORWARD_CASE_TYPE = "CaseRepeater";
    private static final String FORWARD_FORMS_TYPE = "FormRepeater";
    private static final String FORWARD_STUBS_TYPE = "ShortFormRepeater";
    private static final String FORWARD_SCHEMA_TYPE = "AppStructureRepeater";

    private final Config template;

    private CommcareDataForwardingEndpointService forwardingEndpointService;
    private CommCareAPIHttpClient commcareHttpClient;
    private SettingsFacade settingsFacade;
    private EventRelay eventRelay;
    private Configs configs;

    @Autowired
    public CommcareConfigServiceImpl(@Qualifier("commcareAPISettings") SettingsFacade settingsFacade,
                                     CommCareAPIHttpClient commCareAPIHttpClient, EventRelay eventRelay,
                                     CommcareDataForwardingEndpointService forwardingEndpointService) {
        this.settingsFacade = settingsFacade;
        this.commcareHttpClient = commCareAPIHttpClient;
        this.eventRelay = eventRelay;
        this.forwardingEndpointService = forwardingEndpointService;
        loadConfigs();
        template = createTemplate();
    }

    @Override
    public Config saveConfig(Config config) throws CommcareAuthenticationException, CommcareConnectionFailureException {

        if (configs.nameInUse(config.getName())) {
            if (!isSameServer(config.getAccountConfig(), configs.getByName(config.getName()).getAccountConfig())) {
                eventRelay.sendEventMessage(new MotechEvent(EventSubjects.CONFIG_UPDATED, prepareParams(config.getName())));
            }
            configs.updateConfig(config);
        } else {
            validateConfig(config);
            configs.saveConfig(config);
            eventRelay.sendEventMessage(new MotechEvent(EventSubjects.CONFIG_CREATED, prepareParams(config.getName())));
        }

        if (verifyConfig(config)) {
            updateForwardingRules(config);
        } else {
            LOGGER.info(String.format("Configuration \"%s\" couldn't be verified. Forwarding rules are not updated!", config.getName()));
        }

        if (!configs.hasDefault()) {
            setDefault(config.getName());
        }

        updateConfigs();

        return config;
    }

    private void validateConfig(Config config) {
        String baseUrl = config.getAccountConfig().getBaseUrl();
        String domain = config.getAccountConfig().getDomain();

        if (configs.validateUrlAndDomain(baseUrl, domain)) {
            throw new IllegalArgumentException("There already is configuration for url \"" + baseUrl + domain + "\"");
        }
    }

    @Override
    public Config getByName(String name) {
        return configs.getByName(name);
    }

    @Override
    public void deleteConfig(String configName) {

        Config config = configs.getByName(configName);

        configs.deleteConfig(configName);

        Map<String, Object> map = new HashMap<>();
        map.put(EventDataKeys.CONFIG_NAME, config.getName());

        eventRelay.sendEventMessage(new MotechEvent(EventSubjects.CONFIG_DELETED, map));

        updateConfigs();
    }

    @Override
    public void setDefault(String name) {

        if (configs.setDefaultConfigName(name)) {
            updateConfigs();
        }
    }

    @Override
    public Config getDefault() {
        return configs.getDefault();
    }

    @Override
    public Config create() {
        return template;
    }

    @Override
    public Configs getConfigs() {
        return configs;
    }

    @Override
    public boolean verifyConfig(Config config) {
        return commcareHttpClient.verifyConnection(config.getAccountConfig());
    }

    @Override
    public String getBaseUrl() {
        return settingsFacade.getPlatformSettings().getServerUrl() + "/module/commcare/";
    }

    @Override
    public boolean exists(String name) {
        return configs.nameInUse(name);
    }

    public void setConfigs(Configs configs) {
        this.configs = configs;
    }

    private synchronized void loadConfigs() {
        try (InputStream is = settingsFacade.getRawConfig(COMMCARE_CONFIGS_FILE_NAME)) {

            String jsonText = IOUtils.toString(is);
            Gson gson = new Gson();
            configs = gson.fromJson(jsonText, Configs.class);
        }
        catch (Exception e) {
            throw new JsonIOException("Malformed " + COMMCARE_CONFIGS_FILE_NAME + " file? " + e.toString(), e);
        }
    }

    private void updateConfigs() {
        Gson gson = new Gson();
        String jsonText = gson.toJson(configs, Configs.class);
        ByteArrayResource resource = new ByteArrayResource(jsonText.getBytes());
        settingsFacade.saveRawConfig(COMMCARE_CONFIGS_FILE_NAME, resource);
        loadConfigs();
    }

    private Map<String, Object> prepareParams(String name) {

        Map<String, Object> params = new HashMap<>();
        params.put(EventDataKeys.CONFIG_NAME, name);

        return params;
    }

    private Config createTemplate() {
        Config config = new Config();
        config.setName(getProperty(CONFIG_NAME));

        AccountConfig accountConfig = new AccountConfig();
        accountConfig.setBaseUrl(getProperty(CONFIG_BASE_URL));
        accountConfig.setDomain(getProperty(CONFIG_DOMAIN));
        accountConfig.setUsername(getProperty(CONFIG_USERNAME));
        accountConfig.setPassword(getProperty(CONFIG_PASSWORD));

        config.setEventStrategy(getProperty(CASE_EVENT_STRATEGY_KEY));

        config.setForwardCases(Boolean.getBoolean(getProperty(FORWARD_CASES_KEY)));
        config.setForwardForms(Boolean.getBoolean(getProperty(FORWARD_FORMS_KEY)));
        config.setForwardSchema(Boolean.getBoolean(getProperty(FORWARD_SCHEMA_CHANGES_KEY)));
        config.setForwardSchema(Boolean.getBoolean(getProperty(FORWARD_FORM_STUBS_KEY)));

        config.setAccountConfig(accountConfig);

        return config;
    }

    private String getProperty(String key) {
        String property = settingsFacade.getProperty(key);
        return StringUtils.isNotBlank(key) ? property : null;
    }

    private List<String> getEndpoints(Config config) {

        List<String> endpoints = new ArrayList<>();

        for (CommcareDataForwardingEndpoint endpoint : forwardingEndpointService.getAllDataForwardingEndpoints(config)) {
            endpoints.add(endpoint.getType());
        }

        return endpoints;
    }

    private void updateForwardingRules(Config config) {
        List<String> endpoints = getEndpoints(config);

        updateCasesForwarding(config, endpoints);
        updateFormsForwarding(config, endpoints);
        updateStubsForwading(config, endpoints);
        updateSchemaForwarding(config, endpoints);
    }

    private boolean isSameServer(AccountConfig one, AccountConfig two) {
        return one.getBaseUrl().equals(two.getBaseUrl()) && one.getDomain().equals(two.getDomain());
    }

    private void updateCasesForwarding(Config config, List<String> endpoints) {
        if (config.isForwardCases() && !endpoints.contains(FORWARD_CASE_TYPE)) {
            forward(config, FORWARD_CASE_TYPE, getCasesUrl(config.getName()));
        } else if (!config.isForwardCases() && endpoints.contains(FORWARD_CASE_TYPE)) {
            config.setForwardCases(true);
        }
    }

    private void updateFormsForwarding(Config config, List<String> endpoints) {
        if (config.isForwardForms() && !endpoints.contains(FORWARD_FORMS_TYPE)) {
            forward(config, FORWARD_FORMS_TYPE, getFormsUrl(config.getName()));
        } else if (!config.isForwardForms() && endpoints.contains(FORWARD_FORMS_TYPE)) {
            config.setForwardForms(true);
        }
    }

    private void updateStubsForwading(Config config, List<String> endpoints) {
        if (config.isForwardStubs() && !endpoints.contains(FORWARD_STUBS_TYPE)) {
            forward(config, FORWARD_STUBS_TYPE, getFormStubsUrl(config.getName()));
        } else if (!config.isForwardStubs() && endpoints.contains(FORWARD_STUBS_TYPE)) {
            config.setForwardStubs(true);
        }
    }

    private void updateSchemaForwarding(Config config, List<String> endpoints) {
        if (config.isForwardSchema() && !endpoints.contains(FORWARD_SCHEMA_TYPE)) {
            forward(config, FORWARD_SCHEMA_TYPE, getSchemaChangeUrl(config.getName()));
        } else if (!config.isForwardSchema() && endpoints.contains(FORWARD_SCHEMA_TYPE)) {
            config.setForwardSchema(true);
        }
    }

    private void forward(Config config, String type, String url) {
        CommcareDataForwardingEndpoint newForwardingEndpoint = new CommcareDataForwardingEndpoint(
                config.getAccountConfig().getDomain(), type,
                url, null);

        forwardingEndpointService.createNewDataForwardingRule(newForwardingEndpoint, config);
    }

    private String getCasesUrl(String name) {
        return getBaseUrl() + "cases/" + name;
    }

    private String getFormsUrl(String name) {
        return getBaseUrl() + "forms/" + name;
    }

    private String getFormStubsUrl(String name) {
        return getBaseUrl() + "stub/" + name;
    }

    private String getSchemaChangeUrl(String name) {
        return getBaseUrl() + "appSchemaChange/" + name;
    }
}
