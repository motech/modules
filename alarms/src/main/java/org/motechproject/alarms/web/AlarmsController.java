package org.motechproject.alarms.web;

import org.motechproject.alarms.domain.Alarm;
import org.motechproject.alarms.domain.Recipient;
import org.motechproject.alarms.repository.RecipientDataService;
import org.motechproject.alarms.service.AlarmsService;
import org.motechproject.scheduler.exception.MotechSchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import static org.motechproject.alarms.constants.AlarmsConstants.HAS_MANAGE_ALARMS_PERMISSION;

@Controller
@PreAuthorize(HAS_MANAGE_ALARMS_PERMISSION)
public class AlarmsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmsController.class);

    @Autowired
    private AlarmsService alarmsService;

    @Autowired
    private RecipientDataService recipientDataService;

    @RequestMapping(value = "/getAlarms", method = RequestMethod.GET)
    @ResponseBody
    public List<Alarm> getAlarms() {
        return alarmsService.getAlarms();
    }

    @RequestMapping(value = "/saveAlarm", method = RequestMethod.POST)
    @ResponseBody
    public Alarm saveAlarm(@RequestBody Alarm alarm) {
        return alarmsService.saveAlarm(alarm);
    }

    @RequestMapping(value = "/deleteAlarm", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> deleteAlarm(@RequestBody String alarmId) {
        try {
            alarmsService.deleteAlarm(Long.valueOf(alarmId));
        } catch (IllegalArgumentException e) {
            LOGGER.error("Alarm id is not a number", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.error("Fatal error raised during deleting alarm", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/getEmailRecipients", method = RequestMethod.GET)
    @ResponseBody
    public List<Recipient> getEmailRecipients() {
        return recipientDataService.retrieveAll();
    }

    @RequestMapping(value = "/addRecipient", method = RequestMethod.POST)
    @ResponseBody
    public Recipient addRecipient(@RequestBody Recipient recipient) {
        return recipientDataService.create(recipient);
    }

    @RequestMapping(value = "/enableAlarm", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> enableAlarm(@RequestBody String alarmId) {
        try {
            alarmsService.enableAlarm(Long.valueOf(alarmId));
        } catch (IllegalArgumentException e) {
            LOGGER.error("Alarm id is not a number", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/disableAlarm", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> disableAlarm(@RequestBody String alarmId) {
        try {
            alarmsService.disableAlarm(Long.valueOf(alarmId));
        } catch (IllegalArgumentException e) {
            LOGGER.error("Alarm id is not a number", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler(MotechSchedulerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleException(MotechSchedulerException e) {
        LOGGER.error(e.getMessage(), e);
        return "Cannot schedule job for alarm, check the log for more details";
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleException(Exception e) {
        LOGGER.error(e.getMessage(), e);
        return e.getMessage();
    }
}
