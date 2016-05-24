package org.motechproject.openmrs19.web;

import org.motechproject.openmrs19.config.Config;
import org.motechproject.openmrs19.config.Configs;
import org.motechproject.openmrs19.exception.OpenMRSAuthenticationException;
import org.motechproject.openmrs19.service.OpenMRSConfigService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Controller responsible for managing OpenMRS configurations. It provides methods for getting and updating
 * configurations.
 */
@Controller
@RequestMapping(value = "/configs")
public class ConfigController extends OpenMRSController {

    private OpenMRSConfigService configService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Configs getConfigs() {
        return configService.getConfigs();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public void updateConfigs(@RequestBody Configs configs) {
        configService.deleteAllConfigs();
        configService.saveAllConfigs(configs);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/verify", produces = MediaType.TEXT_PLAIN_VALUE)
    public void verifyConfig(@RequestBody Config config) {
        if (!configService.verifyConfig(config)) {
            throw new OpenMRSAuthenticationException("Motech was unable to authenticate to OpenMRS. Please verify your account settings.");
        }
    }

    @Autowired
    public void setConfigService(OpenMRSConfigService configService) {
        this.configService = configService;
    }


}
