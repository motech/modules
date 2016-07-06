package org.motechproject.alarms.scheduler;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.motechproject.alarms.constants.AlarmsConstants;
import org.motechproject.event.MotechEvent;
import org.motechproject.scheduler.contract.JobId;
import org.motechproject.scheduler.contract.RepeatingPeriodJobId;
import org.motechproject.scheduler.contract.RepeatingPeriodSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AlarmsScheduler {

    private static final int ALARM_JOB_DELAY = 5;

    @Autowired
    private MotechSchedulerService motechSchedulerService;

    public void scheduleAlarmJob(int periodInMinutes, Long alarmId) {
        if (alarmId == null) {
            throw new IllegalArgumentException("Cannot schedule job for alarm, because alarm id is empty");
        }

        Period period = Period.minutes(periodInMinutes);
        Map<String, Object> eventParameters = new HashMap<>();
        eventParameters.put(AlarmsConstants.ALARM_EVENT_ID, alarmId);
        eventParameters.put(MotechSchedulerService.JOB_ID_KEY, alarmId.toString());

        MotechEvent event = new MotechEvent(AlarmsConstants.ALARM_EVENT, eventParameters);

        RepeatingPeriodSchedulableJob job = new RepeatingPeriodSchedulableJob(event, DateTime.now().plusMinutes(ALARM_JOB_DELAY).toDate(), null, period, true);
        motechSchedulerService.scheduleRepeatingPeriodJob(job);
    }

    public void unscheduleAlarmJob(Long reportId) {
        JobId jobId = new RepeatingPeriodJobId(AlarmsConstants.ALARM_EVENT, reportId.toString());
        motechSchedulerService.unscheduleJob(jobId);
    }

    public void rescheduleAlarmJob(int period, Long reportId) {
        unscheduleAlarmJob(reportId);
        scheduleAlarmJob(period, reportId);
    }
}
