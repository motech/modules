package org.motechproject.commcare.web;

import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.commcare.exception.EndpointNotSupported;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

import static org.motechproject.commcare.events.constants.EventSubjects.SCHEMA_CHANGE_EVENT;

/**
 * Controller that serves as an endpoint for schema change notifications coming from the CommCareHQ server. It is
 * capable of handling multiple configurations by parameterizing the endpoint URL.
 */
@Controller
@RequestMapping("/appSchemaChange")
public class AppSchemaChangeController extends CommcareController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppSchemaChangeController.class);

    private CommcareConfigService configService;
    private EventRelay eventRelay;

    @Autowired
    public AppSchemaChangeController(EventRelay eventRelay, CommcareConfigService configService) {
        this.eventRelay = eventRelay;
        this.configService = configService;
    }

    @RequestMapping
    @ResponseStatus(HttpStatus.OK)
    public void receiveSchemaChange() throws EndpointNotSupported {
        doReceiveSchemaChange(configService.getDefault());
    }

    @RequestMapping(value = "/{configName}")
    @ResponseStatus(HttpStatus.OK)
    public void receiveSchemaChange(@PathVariable String configName) throws EndpointNotSupported {
        doReceiveSchemaChange(configService.getByName(configName));
    }

    private void doReceiveSchemaChange(Config config) throws EndpointNotSupported {

        LOGGER.trace("Received schema change request.");

        if (!config.isForwardSchema()) {
            throw new EndpointNotSupported(String.format("Configuration \"%s\" doesn't support endpoint for schema!", config.getName()));
        }

        Map<String, Object> params = new HashMap<>();
        params.put(EventDataKeys.CONFIG_DOMAIN, config.getAccountConfig().getDomain());
        params.put(EventDataKeys.CONFIG_BASE_URL, config.getAccountConfig().getBaseUrl());
        params.put(EventDataKeys.CONFIG_NAME, config.getName());

        eventRelay.sendEventMessage(new MotechEvent(SCHEMA_CHANGE_EVENT, params));
    }
}
