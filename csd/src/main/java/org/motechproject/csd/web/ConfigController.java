package org.motechproject.csd.web;

import org.motechproject.csd.domain.Config;
import org.motechproject.csd.exception.ConfigNotFoundException;
import org.motechproject.csd.service.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.util.List;

import static org.motechproject.csd.constants.CSDConstants.HAS_MANAGE_CSD_PERMISSION;

/**
 * Sends & receives configs to/from the UI.
 */
@Controller
public class ConfigController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigController.class);

    private ConfigService configService;

    @Autowired
    public void setConfigService(@Qualifier("configService") ConfigService configService) {
        this.configService = configService;
    }

    @RequestMapping(value = "/csd-configs", method = RequestMethod.GET)
    @PreAuthorize(HAS_MANAGE_CSD_PERMISSION)
    @ResponseBody
    public List<Config> getConfigs() {
        return configService.getConfigs();
    }

    @RequestMapping(value = "/csd-configs", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(HAS_MANAGE_CSD_PERMISSION)
    @ResponseBody
    public List<Config> updateConfigs(@RequestBody List<Config> configs) {
        configService.updateConfigs(configs);
        return configService.getConfigs();
    }

    @ExceptionHandler(ConfigNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleConfigNotFoundException(ConfigNotFoundException e) throws IOException {
        LOGGER.error("Config not found", e);
        return e.getMessage();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleException(Exception e) throws IOException {
        LOGGER.error("Error while updating configs", e);
        return e.getMessage();
    }

}
