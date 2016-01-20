package org.motechproject.odk.web;


import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * Controller that maps to /configs. Provides an API enpoint for configuration CRUD operations.
 */
@Controller
@RequestMapping(value = "/configs")
public class ConfigurationController {

    @Autowired
    private ConfigurationService configurationService;

    /**
     * Returns a list of {@link Configuration}
     * @return List of {@link Configuration}
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Configuration> getConfigs() {
        return configurationService.getAllConfigs();
    }

    /**
     * Creates or updates a {@link Configuration}
     * @param configuration {@link Configuration}
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void addOrUpdateConfig(@RequestBody Configuration configuration) {
        configurationService.addOrUpdateConfiguration(configuration);
    }

    /**
     * Deletes a {@link Configuration} by name.
     * @param configName The name of the configuration.
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/{configName}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteConfig(@PathVariable("configName") String configName) {
        configurationService.removeConfiguration(configName);
    }
}
