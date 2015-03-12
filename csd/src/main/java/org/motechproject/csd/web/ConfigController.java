package org.motechproject.csd.web;

import org.motechproject.csd.CSDEventKeys;
import org.motechproject.csd.domain.Config;
import org.motechproject.csd.scheduler.CSDSchedulerService;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Sends sends & received configs to/from the UI.
 */
@Controller
public class ConfigController {

    private ConfigService configService;

    private CSDSchedulerService schedulerService;

    @Autowired
    public ConfigController(@Qualifier("configService") ConfigService configService, CSDSchedulerService schedulerService) {
        this.configService = configService;
        this.schedulerService = schedulerService;
    }

    @RequestMapping(value = "/csd-config", method = RequestMethod.GET)
    @ResponseBody
    public Config getConfigs() {
        return configService.getConfig();
    }

    @RequestMapping(value = "/csd-config", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Config updateConfigs(@RequestBody Config config) {
        configService.updateConfig(config);

        unschedule();
        if (configService.getConfig().isSchedulerEnabled()) {
            schedule();
        }

        return configService.getConfig();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleException(Exception e) throws IOException {
        return e.getMessage();
    }

    private void schedule() {
        String xml = configService.getConfig().getXmlUrl();

        Map<String, Object> eventParameters = new HashMap<>();
        eventParameters.put(CSDEventKeys.XML_URL, xml);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put(CSDEventKeys.EVENT_PARAMETERS, eventParameters);

        schedulerService.scheduleXmlConsumerRepeatingJob(parameters);
    }

    private void unschedule() {
        schedulerService.unscheduleXmlConsumerRepeatingJob();
    }
}
