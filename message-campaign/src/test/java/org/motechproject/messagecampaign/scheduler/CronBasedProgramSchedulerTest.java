package org.motechproject.messagecampaign.scheduler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.commons.date.model.Time;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.messagecampaign.builder.CampaignBuilder;
import org.motechproject.messagecampaign.builder.EnrollRequestBuilder;
import org.motechproject.messagecampaign.contract.CampaignRequest;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.domain.campaign.CronBasedCampaign;
import org.motechproject.messagecampaign.dao.CampaignEnrollmentDataService;
import org.motechproject.messagecampaign.dao.CampaignRecordService;
import org.motechproject.messagecampaign.domain.campaign.CampaignRecord;
import org.motechproject.scheduler.contract.CronSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.commons.date.util.DateUtil.today;

public class CronBasedProgramSchedulerTest {

    private MotechSchedulerService schedulerService;
    @Mock
    private CampaignEnrollmentDataService mockCampaignEnrollmentDataService;
    @Mock
    private CampaignRecordService campaignRecordService;
    @Mock
    private CampaignRecord campaignRecord;

    @Before
    public void setUp() {
        schedulerService = mock(MotechSchedulerService.class);
        initMocks(this);
    }

    @Test
    public void shouldScheduleJobs() {
        CampaignRequest request = new EnrollRequestBuilder().withDefaults().withReferenceDate(today()).build();
        CronBasedCampaign campaign = CampaignBuilder.defaultCronBasedCampaign();

        CronBasedCampaignSchedulerService cronBasedCampaignScheduler = new CronBasedCampaignSchedulerService(schedulerService, campaignRecordService);

        when(campaignRecordService.findByName("testCampaign")).thenReturn(campaignRecord);
        when(campaignRecord.toCampaign()).thenReturn(campaign);

        CampaignEnrollment enrollment = new CampaignEnrollment("12345", "testCampaign");
        enrollment.setReferenceDate(today());
        enrollment.setDeliverTime(new Time(9, 30));
        cronBasedCampaignScheduler.start(enrollment);
        ArgumentCaptor<CronSchedulableJob> capture = ArgumentCaptor.forClass(CronSchedulableJob.class);
        verify(schedulerService, times(2)).scheduleJob(capture.capture());

        List<CronSchedulableJob> allJobs = capture.getAllValues();
        assertEquals(campaign.getMessages().get(0).getCron(), allJobs.get(0).getCronExpression());
        assertEquals(DateUtil.today(), DateUtil.newDate(allJobs.get(0).getStartDate()));
        assertEquals("org.motechproject.messagecampaign.fired-campaign-message", allJobs.get(0).getMotechEvent().getSubject());
        assertMotechEvent(allJobs.get(0), "MessageJob.testCampaign.12345.cron-message1", "cron-message1");

        assertEquals(campaign.getMessages().get(1).getCron(), allJobs.get(1).getCronExpression());
        assertEquals(DateUtil.today(), DateUtil.newDate(allJobs.get(1).getStartDate()));
        assertEquals("org.motechproject.messagecampaign.fired-campaign-message", allJobs.get(1).getMotechEvent().getSubject());
        assertMotechEvent(allJobs.get(1), "MessageJob.testCampaign.12345.cron-message2", "cron-message2");
    }

    private void assertMotechEvent(CronSchedulableJob cronSchedulableJob, String expectedJobId, String messageKey) {
        assertEquals(expectedJobId, cronSchedulableJob.getMotechEvent().getParameters().get("JobID"));
        assertEquals("testCampaign", cronSchedulableJob.getMotechEvent().getParameters().get("CampaignName"));
        assertEquals("12345", cronSchedulableJob.getMotechEvent().getParameters().get("ExternalID"));
        assertEquals(messageKey, cronSchedulableJob.getMotechEvent().getParameters().get("MessageKey"));
    }
}
