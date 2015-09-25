package org.motechproject.messagecampaign.scheduler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.event.MotechEvent;
import org.motechproject.messagecampaign.EventKeys;
import org.motechproject.messagecampaign.dao.CampaignEnrollmentDataService;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollmentStatus;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class EndOfCampaignListenerTest {

    private EndOfCampaignListener endOfCampaignListener;

    @Mock
    private CampaignEnrollmentDataService campaignEnrollmentDataService;

    @Before
    public void setup() {
        initMocks(this);
        endOfCampaignListener = new EndOfCampaignListener();
        endOfCampaignListener.setCampaignEnrollmentDataService(campaignEnrollmentDataService);
    }

    @Test
    public void shouldMarkEnrollmentAsComplete() throws SchedulerException {
        Trigger trigger = mock(Trigger.class);
        when(trigger.getNextFireTime()).thenReturn(null);

        when(campaignEnrollmentDataService.findByExternalIdAndCampaignName("123abc", "campaign")).thenReturn(new CampaignEnrollment("123abc", "campaign"));

        Map<String, Object> params = new HashMap<>();
        params.put(EventKeys.EXTERNAL_ID_KEY, "123abc");
        params.put(EventKeys.CAMPAIGN_NAME_KEY, "campaign");
        MotechEvent event = new MotechEvent(EventKeys.CAMPAIGN_COMPLETED, params);
        endOfCampaignListener.handle(event);

        ArgumentCaptor<CampaignEnrollment> enrollmentCaptor = ArgumentCaptor.forClass(CampaignEnrollment.class);
        verify(campaignEnrollmentDataService).update(enrollmentCaptor.capture());
        CampaignEnrollment enrollment = enrollmentCaptor.getValue();
        assertEquals("123abc", enrollment.getExternalId());
        assertEquals("campaign", enrollment.getCampaignName());
        assertEquals(CampaignEnrollmentStatus.COMPLETED, enrollment.getStatus());
    }
}
