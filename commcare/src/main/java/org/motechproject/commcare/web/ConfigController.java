package org.motechproject.commcare.web;

import org.codehaus.jackson.map.ObjectMapper;
import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.config.Configs;
import org.motechproject.commcare.exception.CommcareAuthenticationException;
import org.motechproject.commcare.exception.CommcareConnectionFailureException;
import org.motechproject.commcare.service.CommcareConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

import static org.motechproject.commcare.util.Constants.HAS_MANAGE_COMMCARE_PERMISSION;

/**
 * Controller responsible for managing configurations. It provides methods for retrieving, creating, updating and
 * verifying configurations. It also provides method for making a configuration default.
 */
@Controller
@RequestMapping(value = "/configs")
@PreAuthorize(HAS_MANAGE_COMMCARE_PERMISSION)
public class ConfigController extends CommcareController {

    private CommcareConfigService configService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Configs getConfigs() {
        return configService.getConfigs();
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    @ResponseBody
    public Config createConfig() {
        return configService.create();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/default", method = RequestMethod.POST)
    public void makeDefault(@RequestBody String name) {
        configService.setDefault(name);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Config saveConfig(@RequestBody Config config, @RequestParam(required = false) String oldName) throws CommcareConnectionFailureException {
        if(oldName == null) {
            return configService.saveNewConfig(config);
        } else {
            return configService.saveUpdatedConfig(config, oldName);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{name}", method = RequestMethod.DELETE)
    public void deleteConfig(@PathVariable String name) {
        configService.deleteConfig(name);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/verify")
    public void verifyConfig(@RequestBody Config config) {
        if (!configService.verifyConfig(config)) {
            throw new CommcareAuthenticationException("Motech was unable to authenticate to CommCareHQ. Please verify your account settings.");
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping("/endpointBaseUrl")
    @ResponseBody
    public String getBaseEndpoint() throws IOException {
        return new ObjectMapper().writeValueAsString(new StringMessage(configService.getBaseUrl()));
    }

    @Autowired
    public void setConfigService(CommcareConfigService configService) {
        this.configService = configService;
    }
}

class StringMessage {

    private String message;

    StringMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
