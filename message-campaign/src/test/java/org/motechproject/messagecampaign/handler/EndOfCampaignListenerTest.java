package org.motechproject.messagecampaign.handler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.event.MotechEvent;
import org.motechproject.messagecampaign.EventKeys;
import org.motechproject.messagecampaign.service.EnrollmentService;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EndOfCampaignListenerTest {

    @InjectMocks
    private EndOfCampaignListener endOfCampaignListener = new EndOfCampaignListener();

    @Mock
    private EnrollmentService enrollmentService;

    @Test
    public void shouldMarkEnrollmentAsComplete() throws SchedulerException {
        Trigger trigger = mock(Trigger.class);
        when(trigger.getNextFireTime()).thenReturn(null);

        Map<String, Object> params = new HashMap<>();
        params.put(EventKeys.EXTERNAL_ID_KEY, "123abc");
        params.put(EventKeys.CAMPAIGN_NAME_KEY, "campaign");
        MotechEvent event = new MotechEvent(EventKeys.CAMPAIGN_COMPLETED, params);

        endOfCampaignListener.handle(event);

        verify(enrollmentService).markEnrollmentAsCompleted("123abc", "campaign");
    }
}
