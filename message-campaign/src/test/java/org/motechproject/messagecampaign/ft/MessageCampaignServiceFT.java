package org.motechproject.messagecampaign.ft;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.event.listener.EventListenerRegistryService;
import org.motechproject.messagecampaign.EventKeys;
import org.motechproject.messagecampaign.contract.CampaignRequest;
import org.motechproject.messagecampaign.dao.CampaignEnrollmentDataService;
import org.motechproject.messagecampaign.service.MessageCampaignService;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.motechproject.testing.utils.faketime.JvmFakeTime;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.osgi.framework.BundleContext;
import org.quartz.Scheduler;

import javax.inject.Inject;

import static java.util.Arrays.asList;
import static org.motechproject.commons.date.util.DateUtil.newDateTime;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class MessageCampaignServiceFT extends BasePaxIT {
    @Inject
    MessageCampaignService messageCampaignService;

    @Inject
    BundleContext bundleContext;

    Scheduler scheduler;

    @Inject
    EventListenerRegistryService eventListenerRegistry;

    @Inject
    MotechSchedulerService schedulerService;

    @Inject
    CampaignEnrollmentDataService campaignEnrollmentDataService;

    @Before
    public void setup() {
        JvmFakeTime.load();
        //System.startFakingTime();
        scheduler = (Scheduler) getQuartzScheduler(bundleContext);
    }

    @After
    public void teardown() {
        schedulerService.unscheduleAllJobs("org.motechproject.messagecampaign");
        campaignEnrollmentDataService.deleteAll();
    }

    @Test
    public void shouldReceiveMessageEveryWeek() {
        EventCaptor listener = new EventCaptor("listener1", scheduler);
        eventListenerRegistry.registerListener(listener, EventKeys.SEND_MESSAGE);

        CampaignRequest campaignRequest = new CampaignRequest("entity_1", "weekly_campaign", new LocalDate(2012, 10, 1), null);
        messageCampaignService.enroll(campaignRequest);

        listener.assertEventRaisedAt(asList(
                newDateTime(2012, 10, 1, 10, 30, 0),
                newDateTime(2012, 10, 8, 10, 30, 0),
                newDateTime(2012, 10, 15, 10, 30, 0)
        ));
    }
}
