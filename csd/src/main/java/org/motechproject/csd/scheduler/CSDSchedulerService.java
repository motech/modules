package org.motechproject.csd.scheduler;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.motechproject.csd.CSDEventKeys;
import org.motechproject.event.MotechEvent;
import org.motechproject.scheduler.contract.RepeatingPeriodSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class CSDSchedulerService {

    private MotechSchedulerService motechSchedulerService;

    @Autowired
    public CSDSchedulerService(MotechSchedulerService motechSchedulerService) {
        this.motechSchedulerService = motechSchedulerService;
    }

    public void scheduleXmlConsumerRepeatingJob(Map<String, Object> parameters) {
        Map<String, Object> eventParameters = (Map<String, Object>) parameters.get(CSDEventKeys.EVENT_PARAMETERS);

        MotechEvent event = new MotechEvent(CSDEventKeys.CONSUME_XML_EVENT, eventParameters);

        DateTime startDateTime = (DateTime) parameters.get(CSDEventKeys.START_DATE);
        if (startDateTime == null) {
            startDateTime = DateTime.now().plusSeconds(1);
        }
        Date startDate = startDateTime.toDate();

        DateTime endDateTime = (DateTime) parameters.get(CSDEventKeys.END_DATE);
        Date endDate = null;
        if (endDateTime != null) {
            endDate = endDateTime.toDate();
        }

        Period period = (Period) parameters.get(CSDEventKeys.PERIOD);
        if (period == null) {
            throw new IllegalArgumentException("Period cannot be null");
        }

        RepeatingPeriodSchedulableJob job = new RepeatingPeriodSchedulableJob(event, startDate, endDate, period, true);
        motechSchedulerService.safeScheduleRepeatingPeriodJob(job);
    }

    public void unscheduleXmlConsumerRepeatingJob() {
        motechSchedulerService.safeUnscheduleAllJobs(CSDEventKeys.CONSUME_XML_EVENT);
    }
}
