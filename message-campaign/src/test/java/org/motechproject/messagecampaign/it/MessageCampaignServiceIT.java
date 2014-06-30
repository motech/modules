package org.motechproject.messagecampaign.it;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.messagecampaign.contract.CampaignRequest;
import org.motechproject.messagecampaign.service.MessageCampaignService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.osgi.framework.BundleContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;

import javax.inject.Inject;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.quartz.TriggerKey.triggerKey;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class MessageCampaignServiceIT extends BasePaxIT {

    @Inject
    MessageCampaignService messageCampaignService;

    @Inject
    private BundleContext bundleContext;

    Scheduler scheduler;

    @Before
    public void setup() {
        scheduler = (Scheduler) getQuartzScheduler(bundleContext);
    }

    @Test
    public void shouldUnscheduleMessageJobsWhenCampaignIsStopped() throws SchedulerException {
        CampaignRequest campaignRequest = new CampaignRequest("entity_1", "PREGNANCY", new LocalDate(2020, 7, 10), null);

        TriggerKey triggerKey = triggerKey("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.PREGNANCY.entity_1.PREGNANCY", "default");

        messageCampaignService.enroll(campaignRequest);
        assertTrue(scheduler.checkExists(triggerKey));

        messageCampaignService.unenroll(campaignRequest.externalId(), campaignRequest.campaignName());
        assertFalse(scheduler.checkExists(triggerKey));
    }
}
