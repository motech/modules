package org.motechproject.csd.web;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.motechproject.csd.constants.CSDEventKeys;
import org.motechproject.csd.domain.Config;
import org.motechproject.csd.scheduler.CSDScheduler;
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

    private CSDScheduler csdScheduler;

    @Autowired
    public ConfigController(@Qualifier("configService") ConfigService configService, CSDScheduler csdScheduler) {
        this.configService = configService;
        this.csdScheduler = csdScheduler;
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
        String xmlUrl = configService.getConfig().getXmlUrl();
        Period period = new Period(0, 0, 0, configService.getConfig().getPeriodDays(), configService.getConfig().getPeriodHours(),
                configService.getConfig().getPeriodMinutes(), 0, 0);
        String startDate = configService.getConfig().getStartDate();

        Map<String, Object> eventParameters = new HashMap<>();
        eventParameters.put(CSDEventKeys.XML_URL, xmlUrl);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put(CSDEventKeys.EVENT_PARAMETERS, eventParameters);
        parameters.put(CSDEventKeys.PERIOD, period);
        if (startDate != null && !startDate.isEmpty()) {
            parameters.put(CSDEventKeys.START_DATE, DateTime.parse(startDate, DateTimeFormat.forPattern(Config.DATE_TIME_PICKER_FORMAT)));
        }

        csdScheduler.scheduleXmlConsumerRepeatingJob(parameters);
    }

    private void unschedule() {
        csdScheduler.unscheduleXmlConsumerRepeatingJob();
    }
}
