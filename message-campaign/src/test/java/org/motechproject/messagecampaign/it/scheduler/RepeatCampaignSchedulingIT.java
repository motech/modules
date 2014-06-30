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
import org.motechproject.messagecampaign.service.MessageCampaignService;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
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
@ExamReactorStrategy(PerClass.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class RepeatCampaignSchedulingIT extends BasePaxIT {
    private static final DateTimeSource DATE_TIME_SOURCE = new DefaultDateTimeSource();

    @Inject
    MessageCampaignService messageCampaignService;

    @Inject
    BundleContext bundleContext;

    Scheduler scheduler;

    @Inject
    private MotechSchedulerService schedulerService;

    @Before
    public void setup() {
        scheduler = (Scheduler) getQuartzScheduler(bundleContext);
    }

    @After
    public void teardown() {
        schedulerService.unscheduleAllJobs("org.motechproject.messagecampaign");
    }

    @Test
    public void shouldScheduleAllMessagesOfCampaignAtMessageStartTime() throws SchedulerException {
        CampaignRequest campaignRequest = new CampaignRequest("entity_1", "WeeklyCampaign", new LocalDate(2020, 7, 10), null);
        messageCampaignService.enroll(campaignRequest);
        List<DateTime> fireTimes = getFireTimes("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.WeeklyCampaign.entity_1.message_key_1-repeat");
        assertEquals(asList(
                newDateTime(2020, 7, 10, 10, 30, 0),
                newDateTime(2020, 7, 17, 10, 30, 0),
                newDateTime(2020, 7, 24, 10, 30, 0)),
                fireTimes);

        fireTimes = getFireTimes("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.WeeklyCampaign.entity_1.message_key_2-repeat");
        assertEquals(asList(
                newDateTime(2020, 7, 10, 8, 30, 0),
                newDateTime(2020, 7, 20, 8, 30, 0),
                newDateTime(2020, 7, 30, 8, 30, 0)),
                fireTimes);
    }

    @Test
    public void shouldScheduleWeeklyMessagesAtUserSpecifiedTime() throws SchedulerException {
        CampaignRequest campaignRequest = new CampaignRequest("entity_1", "WeeklyCampaign", new LocalDate(2020, 7, 10), new Time(15, 20));
        messageCampaignService.enroll(campaignRequest);
        List<DateTime> fireTimes = getFireTimes("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.WeeklyCampaign.entity_1.message_key_1-repeat");
        assertEquals(asList(
                newDateTime(2020, 7, 10, 15, 20, 0),
                newDateTime(2020, 7, 17, 15, 20, 0),
                newDateTime(2020, 7, 24, 15, 20, 0)),
                fireTimes);
    }

    @Test
    public void shouldNotScheduleMessagesInPastForDelayedEnrollment() throws SchedulerException {
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

            CampaignRequest campaignRequest = new CampaignRequest("entity_1", "WeeklyCampaign", new LocalDate(2020, 7, 10), null);
            messageCampaignService.enroll(campaignRequest);
            List<DateTime> fireTimes = getFireTimes("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.WeeklyCampaign.entity_1.message_key_1-repeat");
            assertEquals(asList(
                    newDateTime(2020, 7, 17, 10, 30, 0),
                    newDateTime(2020, 7, 24, 10, 30, 0)),
                    fireTimes);
        } finally {
            DateTimeSourceUtil.setSourceInstance(DATE_TIME_SOURCE);
        }
    }

    @Test
    public void shouldScheduleMessagesEvery12Hours() throws SchedulerException {
        CampaignRequest campaignRequest = new CampaignRequest("entity_1", "HourlyCampaign", new LocalDate(2020, 7, 10), new Time(4, 30));
        messageCampaignService.enroll(campaignRequest);
        List<DateTime> fireTimes = getFireTimes("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.HourlyCampaign.entity_1.message_key_1-repeat");
        assertEquals(asList(
                newDateTime(2020, 7, 10, 4, 30, 0),
                newDateTime(2020, 7, 10, 16, 30, 0),
                newDateTime(2020, 7, 11, 4, 30, 0),
                newDateTime(2020, 7, 11, 16, 30, 0)),
                fireTimes);
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
