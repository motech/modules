package org.motechproject.csd.scheduler;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.csd.constants.CSDEventKeys;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.scheduler.contract.RepeatingPeriodSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class CSDScheduler {

    private static final DateTimeFormatter DT_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSSS");

    private MotechSchedulerService motechSchedulerService;

    private EventRelay eventRelay;

    @Autowired
    public CSDScheduler(MotechSchedulerService motechSchedulerService, EventRelay eventRelay) {
        this.motechSchedulerService = motechSchedulerService;
        this.eventRelay = eventRelay;
    }

    public void scheduleXmlConsumerRepeatingJob(Map<String, Object> eventParameters, DateTime startDateTime, DateTime endDateTime, Period period, String key) {
        MotechEvent event = new MotechEvent(CSDEventKeys.CONSUME_XML_EVENT_BASE + key, eventParameters);

        Date startDate;

        if (startDateTime != null) {
            startDate = startDateTime.toDate();
        } else {
            startDate = DateTime.now().plusSeconds(1).toDate();
        }

        Date endDate = null;
        if (endDateTime != null) {
            endDate = endDateTime.toDate();
        }

        if (period == null) {
            throw new IllegalArgumentException("Period cannot be null");
        }

        RepeatingPeriodSchedulableJob job = new RepeatingPeriodSchedulableJob(event, startDate, endDate, period, true);
        motechSchedulerService.safeScheduleRepeatingPeriodJob(job);
    }

    public void unscheduleXmlConsumerRepeatingJobs() {
        motechSchedulerService.safeUnscheduleAllJobs(CSDEventKeys.CONSUME_XML_EVENT_BASE);
    }

    public void sendCustomUpdateEventMessage(String xmlUrl) {
        sendUpdateEventMessage(CSDEventKeys.CSD_UPDATE_CUSTOM, xmlUrl);
    }

    public void sendScheduledUpdateEventMessage(String xmlUrl) {
        sendUpdateEventMessage(CSDEventKeys.CSD_UPDATE_SCHEDULED, xmlUrl);
    }

    public void sendTaskUpdateEventMessage(String xmlUrl) {
        sendUpdateEventMessage(CSDEventKeys.CSD_UPDATE_TASK, xmlUrl);
    }

    private void sendUpdateEventMessage(String messageType, String xmlUrl) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(CSDEventKeys.UPDATE_DATE, DT_FORMATTER.print(DateTime.now()));
        parameters.put(CSDEventKeys.XML_URL, xmlUrl);
        MotechEvent motechEvent = new MotechEvent(messageType, parameters);
        eventRelay.sendEventMessage(motechEvent);
    }
}
