package org.motechproject.csd.scheduler;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.csd.constants.CSDEventKeys;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.scheduler.contract.RepeatingPeriodSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class CSDSchedulerTest {

    @Mock
    private MotechSchedulerService motechSchedulerService;

    @Mock
    private EventRelay eventRelay;

    private CSDScheduler csdScheduler;

    @Before
    public void setup() throws Exception {
        initMocks(this);
        csdScheduler = new CSDScheduler(motechSchedulerService, eventRelay);
    }

    @Test
    public void shouldScheduleJob() {
        String url = "someURL";
        Period period = new Period(0, 0, 0, 1, 0, 0, 0, 0);
        DateTime startDate = new DateTime();

        Map<String, Object> eventParameters = new HashMap<>();
        eventParameters.put(CSDEventKeys.XML_URL, url);

        csdScheduler.scheduleXmlConsumerRepeatingJob(eventParameters, startDate, null, period, "123");

        MotechEvent event = new MotechEvent(CSDEventKeys.CONSUME_XML_EVENT_BASE + "123", eventParameters);
        RepeatingPeriodSchedulableJob job = new RepeatingPeriodSchedulableJob(event, startDate.toDate(), null, period, true);

        verify(motechSchedulerService).safeScheduleRepeatingPeriodJob(job);
    }

    @Test
    public void shouldUnscheduleJob() {
        csdScheduler.unscheduleXmlConsumerRepeatingJobs();

        verify(motechSchedulerService).safeUnscheduleAllJobs(CSDEventKeys.CONSUME_XML_EVENT_BASE);
    }
}
