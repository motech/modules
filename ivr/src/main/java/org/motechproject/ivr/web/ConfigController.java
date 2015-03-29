package org.motechproject.ivr.web;

import org.motechproject.ivr.domain.Config;
import org.motechproject.ivr.service.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.util.List;

/**
 * Sends sends & received configs to/from the UI.
 */
@Controller
public class ConfigController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigController.class);

    private ConfigService configService;

    @Autowired
    public ConfigController(@Qualifier("configService") ConfigService configService) {
        this.configService = configService;
    }

    @RequestMapping(value = "/ivr-configs", method = RequestMethod.GET)
    @ResponseBody
    public List<Config> getConfigs() {
        return configService.allConfigs();
    }

    @RequestMapping(value = "/ivr-configs", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Config> updateConfigs(@RequestBody List<Config> configs) {
        configService.updateConfigs(configs);
        return configService.allConfigs();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleException(Exception e) throws IOException {
        LOGGER.error("Error while updating configs", e);
        return e.getMessage();
    }
}
