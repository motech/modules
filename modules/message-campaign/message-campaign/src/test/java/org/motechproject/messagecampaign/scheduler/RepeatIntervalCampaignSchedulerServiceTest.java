package org.motechproject.messagecampaign.scheduler;

import org.joda.time.Period;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.messagecampaign.dao.AllMessageCampaigns;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.domain.campaign.RepeatIntervalCampaign;
import org.motechproject.messagecampaign.domain.message.RepeatIntervalCampaignMessage;
import org.motechproject.messagecampaign.scheduler.exception.CampaignEnrollmentException;
import org.motechproject.scheduler.MotechSchedulerService;

import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RepeatIntervalCampaignSchedulerServiceTest {
    @Mock
    MotechSchedulerService motechSchedulerService;
    @Mock
    AllMessageCampaigns allMessageCampaigns;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    RepeatIntervalCampaignSchedulerService repeatIntervalCampaignSchedulerService;

    @Before
    public void setUp() {
        repeatIntervalCampaignSchedulerService = new RepeatIntervalCampaignSchedulerService(motechSchedulerService, allMessageCampaigns);
    }

    @Test
    public void shouldCheckIfReferenceTimeIsProvidedWhenTimeOffsetIsLessThanADayForSchedulingACampaign() {
        int repeatInterval = 60;
        String externalId = "externalId";
        String campaignName = "campaignName";
        CampaignEnrollment enrollment = new CampaignEnrollment(externalId, campaignName).setReferenceTime(null);
        RepeatIntervalCampaignMessage campaignMessage = new RepeatIntervalCampaignMessage(new Period(repeatInterval * 1000));
        campaignMessage.name(campaignName);
        when(allMessageCampaigns.getCampaign(campaignName)).thenReturn(new RepeatIntervalCampaign());


        expectedException.expect(CampaignEnrollmentException.class);
        expectedException.expectMessage(String.format("Cannot enroll %s for message campaign %s - Reference time is not provided.", externalId, campaignName));

        repeatIntervalCampaignSchedulerService.scheduleMessageJob(enrollment, campaignMessage);

        verifyZeroInteractions(motechSchedulerService);
    }
}
