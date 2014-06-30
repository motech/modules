package org.motechproject.messagecampaign.scheduler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.messagecampaign.builder.CampaignBuilder;
import org.motechproject.messagecampaign.builder.EnrollRequestBuilder;
import org.motechproject.messagecampaign.contract.CampaignRequest;
import org.motechproject.messagecampaign.domain.campaign.AbsoluteCampaign;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.dao.CampaignEnrollmentDataService;
import org.motechproject.messagecampaign.dao.CampaignRecordService;
import org.motechproject.messagecampaign.userspecified.CampaignRecord;
import org.motechproject.scheduler.contract.RunOnceSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;

import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AbsoluteProgramSchedulerTest {

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
        CampaignRequest request = new EnrollRequestBuilder().withDefaults().build();
        AbsoluteCampaign campaign = new CampaignBuilder().defaultAbsoluteCampaign();

        AbsoluteCampaignSchedulerService absoluteCampaignScheduler = new AbsoluteCampaignSchedulerService(schedulerService, campaignRecordService);

        when(campaignRecordService.findByName("testCampaign")).thenReturn(campaignRecord);
        when(campaignRecord.build()).thenReturn(campaign);

        CampaignEnrollment enrollment = new CampaignEnrollment("12345", "testCampaign");
        absoluteCampaignScheduler.start(enrollment);

        ArgumentCaptor<RunOnceSchedulableJob> capture = ArgumentCaptor.forClass(RunOnceSchedulableJob.class);
        verify(schedulerService, times(2)).scheduleRunOnceJob(capture.capture());

        List<RunOnceSchedulableJob> allJobs = capture.getAllValues();

        Date startDate1 = DateUtil.newDateTime(campaign.getMessages().get(0).getDate(), request.deliverTime().getHour(), request.deliverTime().getMinute(), 0).toDate();
        assertEquals(startDate1.toString(), allJobs.get(0).getStartDate().toString());
        assertEquals("org.motechproject.messagecampaign.fired-campaign-message", allJobs.get(0).getMotechEvent().getSubject());
        assertMotechEvent(allJobs.get(0), "MessageJob.testCampaign.12345.random-1", "random-1");

        Date startDate2 = DateUtil.newDateTime(campaign.getMessages().get(1).getDate(), request.deliverTime().getHour(), request.deliverTime().getMinute(), 0).toDate();
        assertEquals(startDate2.toString(), allJobs.get(1).getStartDate().toString());
        assertEquals("org.motechproject.messagecampaign.fired-campaign-message", allJobs.get(1).getMotechEvent().getSubject());
        assertMotechEvent(allJobs.get(1), "MessageJob.testCampaign.12345.random-2", "random-2");
    }

    private void assertMotechEvent(RunOnceSchedulableJob runOnceSchedulableJob, String expectedJobId, Object messageKey) {
        assertEquals(expectedJobId, runOnceSchedulableJob.getMotechEvent().getParameters().get("JobID"));
        assertEquals("testCampaign", runOnceSchedulableJob.getMotechEvent().getParameters().get("CampaignName"));
        assertEquals("12345", runOnceSchedulableJob.getMotechEvent().getParameters().get("ExternalID"));
        assertEquals(messageKey, runOnceSchedulableJob.getMotechEvent().getParameters().get("MessageKey"));
    }
}
