package org.motechproject.messagecampaign.it.scheduler;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.commons.date.model.Time;
import org.motechproject.commons.date.util.DateTimeSourceUtil;
import org.motechproject.commons.date.util.datetime.DateTimeSource;
import org.motechproject.commons.date.util.datetime.DefaultDateTimeSource;
import org.motechproject.messagecampaign.contract.CampaignRequest;
import org.motechproject.messagecampaign.dao.CampaignMessageRecordService;
import org.motechproject.messagecampaign.dao.CampaignRecordService;
import org.motechproject.messagecampaign.service.MessageCampaignService;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.osgi.framework.BundleContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.motechproject.commons.date.util.DateUtil.newDateTime;
import static org.quartz.TriggerKey.triggerKey;


@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class DayOfWeekCampaignSchedulingBundleIT extends BasePaxIT {
    private static final DateTimeSource DATE_TIME_SOURCE = new DefaultDateTimeSource();

    @Inject
    MessageCampaignService messageCampaignService;

    @Inject
    CampaignMessageRecordService campaignMessageRecordService;

    @Inject
    CampaignRecordService campaignRecordService;

    Scheduler scheduler;

    @Inject
    private BundleContext bundleContext;

    @Inject
    private MotechSchedulerService schedulerService;

    @Before
    public void setup() {
        scheduler = (Scheduler) getQuartzScheduler(bundleContext);
    }

    @After
    public void teardown() throws SchedulerException {
        schedulerService.unscheduleAllJobs("org.motechproject.messagecampaign");
    }

    @Test
    public void shouldScheduleMessageAtItsStartTime() throws SchedulerException {
        CampaignRequest campaignRequest = new CampaignRequest("entity_1", "DayOfWeekCampaign", new LocalDate(2020, 7, 10), null); // Friday
        messageCampaignService.enroll(campaignRequest);
        List<DateTime> fireTimes = getFireTimes("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.DayOfWeekCampaign.entity_1.message_key_1");
        assertEquals(asList(
                newDateTime(2020, 7, 10, 10, 30, 0),
                newDateTime(2020, 7, 13, 10, 30, 0),
                newDateTime(2020, 7, 17, 10, 30, 0),
                newDateTime(2020, 7, 20, 10, 30, 0)),
                fireTimes);
    }

    @Test
    public void shouldScheduleMessageAtUserPreferredTime() throws SchedulerException {
        CampaignRequest campaignRequest = new CampaignRequest("entity_1", "DayOfWeekCampaign", new LocalDate(2020, 7, 10), new Time(8, 20)); // Friday
        messageCampaignService.enroll(campaignRequest);
        List<DateTime> fireTimes = getFireTimes("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.DayOfWeekCampaign.entity_1.message_key_1");
        assertEquals(asList(
                newDateTime(2020, 7, 10, 8, 20, 0),
                newDateTime(2020, 7, 13, 8, 20, 0),
                newDateTime(2020, 7, 17, 8, 20, 0),
                newDateTime(2020, 7, 20, 8, 20, 0)),
                fireTimes);
    }

    @Test
    public void shouldNotScheduleMessagesInThePastForDelayedEnrollment() throws SchedulerException {
        try {
            DateTimeSourceUtil.setSourceInstance(new DateTimeSource() {
                private DateTime dateTime = new DateTime(2020, 7, 15, 0, 0);

                @Override
                public DateTimeZone timeZone() {
                    return dateTime.getZone();
                }

                @Override
                public DateTime now() {
                    return dateTime;
                }

                @Override
                public LocalDate today() {
                    return dateTime.toLocalDate();
                }
            });

            CampaignRequest campaignRequest = new CampaignRequest("entity_1", "DayOfWeekCampaign", new LocalDate(2020, 7, 10), null); // Friday
            messageCampaignService.enroll(campaignRequest);
            List<DateTime> fireTimes = getFireTimes("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.DayOfWeekCampaign.entity_1.message_key_1");
            assertEquals(asList(
                    newDateTime(2020, 7, 17, 10, 30, 0),
                    newDateTime(2020, 7, 20, 10, 30, 0)),
                    fireTimes);
        } finally {
            DateTimeSourceUtil.setSourceInstance(DATE_TIME_SOURCE);
        }
    }

    private List<DateTime> getFireTimes(String triggerKey) throws SchedulerException {
        Trigger trigger = scheduler.getTrigger(triggerKey(triggerKey, "default"));
        List<DateTime> fireTimes = new ArrayList<>();
        Date nextFireTime = trigger.getNextFireTime();
        while (nextFireTime != null) {
            fireTimes.add(newDateTime(nextFireTime));
            nextFireTime = trigger.getFireTimeAfter(nextFireTime);
        }
        return fireTimes;
    }
}
