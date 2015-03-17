package org.motechproject.csd.scheduler;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.motechproject.csd.CSDEventKeys;
import org.motechproject.csd.service.ConfigService;
import org.motechproject.event.MotechEvent;
import org.motechproject.scheduler.contract.RepeatingPeriodSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class CSDSchedulerService {

    private MotechSchedulerService motechSchedulerService;

    private ConfigService configService;

    @Autowired
    public CSDSchedulerService(@Qualifier("configService") ConfigService configService, MotechSchedulerService motechSchedulerService) {
        this.motechSchedulerService = motechSchedulerService;
        this.configService = configService;
    }

    public void scheduleXmlConsumerRepeatingJob(Map<String, Object> parameters) {
        Map<String, Object> eventParameters = (Map<String, Object>) parameters.get(CSDEventKeys.EVENT_PARAMETERS);

        MotechEvent event = new MotechEvent(CSDEventKeys.CONSUME_XML_EVENT, eventParameters);

        DateTime startDateTime = (DateTime) parameters.get(CSDEventKeys.START_DATE);
        Date startDate;
        if (startDateTime == null) {
            startDateTime = new DateTime();
        }
        startDate = startDateTime.plusSeconds(1).toDate();
        DateTime endDateTime = (DateTime) parameters.get(CSDEventKeys.END_DATE);
        Date endDate = null;
        if (endDateTime != null) {
            endDate = endDateTime.toDate();
        }
        Period period = (Period) parameters.get(CSDEventKeys.PERIOD);
        if (period == null) {
            period = new Period(0, 0, 0, configService.getConfig().getPeriodDays(), configService.getConfig().getPeriodHours(), configService.getConfig().getPeriodMinutes(), 0, 0);
        }

        RepeatingPeriodSchedulableJob job = new RepeatingPeriodSchedulableJob(event, startDate, endDate, period, true);
        motechSchedulerService.safeScheduleRepeatingPeriodJob(job);
    }

    public void unscheduleXmlConsumerRepeatingJob() {
        motechSchedulerService.safeUnscheduleAllJobs(CSDEventKeys.CONSUME_XML_EVENT);
    }
}
