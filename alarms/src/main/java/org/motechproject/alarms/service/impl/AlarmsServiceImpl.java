package org.motechproject.alarms.service.impl;

import org.motechproject.alarms.client.AlarmsEmailClient;
import org.motechproject.alarms.domain.Alarm;
import org.motechproject.alarms.domain.AlarmStatus;
import org.motechproject.alarms.domain.Recipient;
import org.motechproject.alarms.repository.AlarmDataService;
import org.motechproject.alarms.repository.RecipientDataService;
import org.motechproject.alarms.scheduler.AlarmsScheduler;
import org.motechproject.alarms.service.AlarmsService;
import org.motechproject.scheduler.exception.MotechSchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("alarmsService")
public class AlarmsServiceImpl implements AlarmsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmsServiceImpl.class);

    @Autowired
    private AlarmDataService alarmDataService;

    @Autowired
    private AlarmsScheduler alarmsScheduler;

    @Autowired
    private RecipientDataService recipientDataService;

    @Autowired
    private AlarmsEmailClient alarmsEmailClient;

    @Override
    public List<Alarm> getAlarms() {
        return alarmDataService.retrieveAll();
    }

    @Override
    public Alarm saveAlarm(Alarm alarm) {
        List<Recipient> recipients = new ArrayList<>();

        for (Recipient r : alarm.getRecipients()) {
            Recipient recipient = recipientDataService.findById(r.getId());
            if (recipient != null) {
                recipients.add(recipient);
            }
        }

        alarm.setRecipients(recipients);

        if (alarm.getId() == null) {
            alarmDataService.create(alarm);

            if (AlarmStatus.ACTIVE.equals(alarm.getStatus())) {
                try {
                    alarmsScheduler.scheduleAlarmJob(alarm.getSchedulePeriod(), alarm.getId());
                } catch (RuntimeException e) {
                    alarmDataService.delete(alarm);
                    throw new MotechSchedulerException(e);
                }
            }
        } else {
            if (AlarmStatus.ACTIVE.equals(alarm.getStatus())) {
                alarmsScheduler.rescheduleAlarmJob(alarm.getSchedulePeriod(), alarm.getId());
            }
            alarmDataService.update(alarm);
        }

        return alarm;
    }

    @Override
    public void deleteAlarm(Long alarmId) {
        Alarm alarm = alarmDataService.findById(alarmId);

        if (alarm == null) {
            throw new IllegalArgumentException("Cannot find alarm with id: " + alarmId);
        } else {
            if (AlarmStatus.ACTIVE.equals(alarm.getStatus())) {
                alarmsScheduler.unscheduleAlarmJob(alarmId);
            }

            alarmDataService.delete(alarm);
        }
    }

    @Override
    public void checkAlarm(Long alarmId) {
        Alarm alarm = alarmDataService.findById(alarmId);

        if (alarm == null) {
            LOGGER.error("Could not send email for alarm with id: {}, because no alarm with this id found", alarmId);
        } else {
            try {
                List<String> recipients = new ArrayList<>();

                for (Recipient r : alarm.getRecipients()) {
                    recipients.add(r.getEmailAddress());
                }

                alarmsEmailClient.sendNewMessage(alarm.getSubject(), recipients, alarm.getMessageContent());
            } catch (IllegalArgumentException e) {
                LOGGER.error("Could not send email for alarm with id: {}, because of wrong alarm data: {}", alarmId, e.getMessage(), e);
            } catch (Exception e) {
                LOGGER.error("Could not send email for alarm with id: {}, because: {}", alarmId, e.getMessage(), e);
            }
        }
    }

    @Override
    public AlarmStatus enableAlarm(Long alarmId) {
        Alarm alarm = alarmDataService.findById(alarmId);

        if (alarm == null) {
            throw new IllegalArgumentException("Cannot find report with id: " + alarmId);
        } else if (alarm.getStatus() == null || AlarmStatus.INACTIVE.equals(alarm.getStatus())) {
            alarmsScheduler.scheduleAlarmJob(alarm.getSchedulePeriod(), alarm.getId());

            alarm.setStatus(AlarmStatus.ACTIVE);
            alarmDataService.update(alarm);
        }

        return alarm.getStatus();
    }

    @Override
    public AlarmStatus disableAlarm(Long alarmId) {
        Alarm alarm = alarmDataService.findById(alarmId);

        if (alarm == null) {
            throw new IllegalArgumentException("Cannot find report with id: " + alarmId);
        } else if (AlarmStatus.ACTIVE.equals(alarm.getStatus())) {
            alarmsScheduler.unscheduleAlarmJob(alarmId);

            alarm.setStatus(AlarmStatus.INACTIVE);
            alarmDataService.update(alarm);
        }

        return alarm.getStatus();
    }
}
