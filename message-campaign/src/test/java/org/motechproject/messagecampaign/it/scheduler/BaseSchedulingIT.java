package org.motechproject.messagecampaign.it.scheduler;


import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.motechproject.commons.date.util.datetime.DateTimeSource;
import org.motechproject.commons.date.util.datetime.DefaultDateTimeSource;
import org.motechproject.messagecampaign.dao.CampaignMessageRecordService;
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

import static org.motechproject.commons.date.util.DateUtil.newDateTime;
import static org.quartz.TriggerKey.triggerKey;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public abstract class BaseSchedulingIT extends BasePaxIT {

    public static final DateTimeSource DATE_TIME_SOURCE = new DefaultDateTimeSource();

    @Inject
    private MessageCampaignService messageCampaignService;

    @Inject
    private BundleContext bundleContext;

    private Scheduler scheduler;

    @Inject
    private MotechSchedulerService schedulerService;

    @Inject
    private CampaignMessageRecordService campaignMessageRecordService;

    final Object lock = new Object();

    @Before
    public void setup() {
        scheduler = (Scheduler) getQuartzScheduler(bundleContext);
    }

    @After
    public void teardown() {
        schedulerService.unscheduleAllJobs("org.motechproject.messagecampaign");
    }

    protected MessageCampaignService getMessageCampaignService() {
        return messageCampaignService;
    }

    protected CampaignMessageRecordService getCampaignMessageRecordService() {
        return campaignMessageRecordService;
    }

    protected List<DateTime> getFireTimes(String triggerKey) throws SchedulerException {
        Trigger trigger = getTrigger(triggerKey);
        List<DateTime> fireTimes = new ArrayList<>();
        Date nextFireTime = trigger.getNextFireTime();
        while (nextFireTime != null) {
            fireTimes.add(newDateTime(nextFireTime));
            nextFireTime = trigger.getFireTimeAfter(nextFireTime);
        }
        return fireTimes;
    }

    protected Trigger getTrigger(String triggerKey) throws SchedulerException {
        return scheduler.getTrigger(triggerKey(triggerKey, "default"));
    }
}
