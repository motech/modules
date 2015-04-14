package org.motechproject.csd.web;

import org.motechproject.csd.domain.Config;
import org.motechproject.csd.service.ConfigService;
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

    private ConfigService configService;

    @Autowired
    public ConfigController(@Qualifier("configService") ConfigService configService) {
        this.configService = configService;
    }

    @RequestMapping(value = "/csd-configs", method = RequestMethod.GET)
    @ResponseBody
    public List<Config> getConfigs() {
        return configService.getConfigs();
    }

    @RequestMapping(value = "/csd-configs", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Config> updateConfigs(@RequestBody List<Config> configs) {
        configService.updateConfigs(configs);
        return configService.getConfigs();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleException(Exception e) throws IOException {
        return e.getMessage();
    }

}
