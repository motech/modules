package org.motechproject.messagecampaign.scheduler;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.commons.date.model.Time;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.messagecampaign.builder.CampaignBuilder;
import org.motechproject.messagecampaign.builder.EnrollRequestBuilder;
import org.motechproject.messagecampaign.contract.CampaignRequest;
import org.motechproject.messagecampaign.dao.CampaignRecordService;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.domain.campaign.OffsetCampaign;
import org.motechproject.messagecampaign.domain.message.CampaignMessage;
import org.motechproject.messagecampaign.exception.CampaignEnrollmentException;
import org.motechproject.messagecampaign.domain.campaign.CampaignRecord;
import org.motechproject.scheduler.contract.RunOnceSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;

import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.joda.time.Period.days;
import static org.joda.time.Period.minutes;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.commons.date.util.DateUtil.newDateTime;
import static org.motechproject.commons.date.util.DateUtil.today;
import static org.motechproject.testing.utils.TimeFaker.fakeNow;
import static org.motechproject.testing.utils.TimeFaker.stopFakingTime;

public class OffsetCampaignSchedulerServiceTest {

    OffsetCampaignSchedulerService offsetCampaignSchedulerService;

    @Mock
    private MotechSchedulerService schedulerService;
    @Mock
    private CampaignRecordService campaignRecordService;
    @Mock
    private CampaignRecord campaignRecord;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        initMocks(this);
        offsetCampaignSchedulerService = new OffsetCampaignSchedulerService(schedulerService, campaignRecordService);
    }

    @Test
    public void shouldScheduleJobForOffsetCampaignMessage() {
        try {
            fakeNow(newDateTime(2010, 10, 1));

            CampaignMessage offsetCampaignMessage = new CampaignMessage(new Time(5,30), days(5));
            offsetCampaignMessage.setMessageKey("foo");

            OffsetCampaign campaign = new OffsetCampaign("camp", asList(offsetCampaignMessage));

            when(campaignRecordService.findByName("camp")).thenReturn(campaignRecord);
            when(campaignRecord.toCampaign()).thenReturn(campaign);

            CampaignEnrollment enrollment = new CampaignEnrollment("entity1", "camp");
            enrollment.setReferenceDate(new LocalDate(2010, 10, 3));

            offsetCampaignSchedulerService.scheduleMessageJob(enrollment, campaign, offsetCampaignMessage);

            ArgumentCaptor<RunOnceSchedulableJob> jobCaptor = ArgumentCaptor.forClass(RunOnceSchedulableJob.class);
            verify(schedulerService).scheduleRunOnceJob(jobCaptor.capture());
            RunOnceSchedulableJob job = jobCaptor.getValue();

            assertEquals(newDateTime(2010, 10, 8, 5, 30, 0).toDate(), job.getStartDate());
        } finally {
            stopFakingTime();
        }
    }

    @Test
    public void shouldHandleTheCaseWhenOffsetIsLessThanADayAndEnrollWithAppropriateTime() {
        try {
            fakeNow(newDateTime(2010, 10, 1));

            CampaignMessage offsetCampaignMessage = new CampaignMessage(new Time(5,30), minutes(5));
            offsetCampaignMessage.setMessageKey("foo");

            OffsetCampaign campaign = new OffsetCampaign("camp", asList(offsetCampaignMessage));

            when(campaignRecordService.findByName("camp")).thenReturn(campaignRecord);
            when(campaignRecord.toCampaign()).thenReturn(campaign);

            CampaignEnrollment enrollment = new CampaignEnrollment("entity1", "camp");
            enrollment.setReferenceDate(new LocalDate(2010, 10, 3));
            enrollment.setDeliverTime(new Time(8, 20));

            offsetCampaignSchedulerService.scheduleMessageJob(enrollment, campaign, offsetCampaignMessage);

            ArgumentCaptor<RunOnceSchedulableJob> jobCaptor = ArgumentCaptor.forClass(RunOnceSchedulableJob.class);
            verify(schedulerService).scheduleRunOnceJob(jobCaptor.capture());
            RunOnceSchedulableJob job = jobCaptor.getValue();

            assertEquals(newDateTime(2010, 10, 3, 8, 25, 0).toDate(), job.getStartDate());
        } finally {
            stopFakingTime();
        }
    }

    @Test
    public void userPreferredDeliverTimeShouldOverrideMessageDeliverTime() {
        try {
            fakeNow(newDateTime(2010, 10, 1));

            CampaignMessage offsetCampaignMessage = new CampaignMessage(new Time(5,30), days(3));
            offsetCampaignMessage.setMessageKey("foo");

            OffsetCampaign campaign = new OffsetCampaign("camp", asList(offsetCampaignMessage));

            when(campaignRecordService.findByName("camp")).thenReturn(campaignRecord);
            when(campaignRecord.toCampaign()).thenReturn(campaign);

            CampaignEnrollment enrollment = new CampaignEnrollment("entity1", "camp");
            enrollment.setReferenceDate(new LocalDate(2010, 10, 3));
            enrollment.setDeliverTime(new Time(8, 20));

            offsetCampaignSchedulerService.scheduleMessageJob(enrollment, campaign, offsetCampaignMessage);

            ArgumentCaptor<RunOnceSchedulableJob> jobCaptor = ArgumentCaptor.forClass(RunOnceSchedulableJob.class);
            verify(schedulerService).scheduleRunOnceJob(jobCaptor.capture());
            RunOnceSchedulableJob job = jobCaptor.getValue();

            assertEquals(newDateTime(2010, 10, 6, 8, 20, 0).toDate(), job.getStartDate());
        } finally {
            stopFakingTime();
        }
    }

    @Test
    public void shouldScheduleJobsAfterGivenTimeOffsetIntervalFromReferenceDate_WhenCampaignStartOffsetIsZero() {
        CampaignRequest request = new EnrollRequestBuilder().withDefaults().withReferenceDate(today()).build();
        OffsetCampaign campaign = CampaignBuilder.defaultOffsetCampaign();

        OffsetCampaignSchedulerService offsetCampaignScheduler = new OffsetCampaignSchedulerService(schedulerService, campaignRecordService);

        when(campaignRecordService.findByName("testCampaign")).thenReturn(campaignRecord);
        when(campaignRecord.toCampaign()).thenReturn(campaign);

        CampaignEnrollment enrollment = new CampaignEnrollment("12345", "testCampaign");
        enrollment.setReferenceDate(today());
        enrollment.setDeliverTime(new Time(9, 30));

        offsetCampaignScheduler.start(enrollment);
        ArgumentCaptor<RunOnceSchedulableJob> capture = ArgumentCaptor.forClass(RunOnceSchedulableJob.class);
        verify(schedulerService, times(4)).scheduleRunOnceJob(capture.capture());

        List<RunOnceSchedulableJob> allJobs = capture.getAllValues();

        Date startDate1 = DateUtil.newDateTime(DateUtil.today().plusDays(7), request.deliverTime().getHour(), request.deliverTime().getMinute(), 0).toDate();
        Assert.assertEquals(startDate1, allJobs.get(0).getStartDate());
        Assert.assertEquals("org.motechproject.messagecampaign.fired-campaign-message", allJobs.get(0).getMotechEvent().getSubject());
        assertMotechEvent(allJobs.get(0), "MessageJob.testCampaign.12345.child-info-week-1", "child-info-week-1");

        Date startDate2 = DateUtil.newDateTime(DateUtil.today().plusDays(14), request.deliverTime().getHour(), request.deliverTime().getMinute(), 0).toDate();
        Assert.assertEquals(startDate2, allJobs.get(1).getStartDate());
        Assert.assertEquals("org.motechproject.messagecampaign.fired-campaign-message", allJobs.get(1).getMotechEvent().getSubject());
        assertMotechEvent(allJobs.get(1), "MessageJob.testCampaign.12345.child-info-week-1a", "child-info-week-1a");

        Date startDate3 = DateUtil.newDateTime(DateUtil.today().plusMonths(1), request.deliverTime().getHour(), request.deliverTime().getMinute(), 0).toDate();
        Assert.assertEquals(startDate3, allJobs.get(2).getStartDate());
        Assert.assertEquals("org.motechproject.messagecampaign.fired-campaign-message", allJobs.get(2).getMotechEvent().getSubject());
        assertMotechEvent(allJobs.get(2), "MessageJob.testCampaign.12345.child-info-month-1", "child-info-month-1");

        RunOnceSchedulableJob endOfCampaignJob = allJobs.get(3);
        Assert.assertEquals(startDate3, endOfCampaignJob.getStartDate());
        Assert.assertEquals("org.motechproject.messagecampaign.campaign-completed", endOfCampaignJob.getMotechEvent().getSubject());
        Assert.assertEquals("testCampaign", endOfCampaignJob.getMotechEvent().getParameters().get("CampaignName"));
        Assert.assertEquals("12345", endOfCampaignJob.getMotechEvent().getParameters().get("ExternalID"));
    }

    @Test
    public void shouldCheckIfStartTimeIsProvidedWithCampaignMessageOrEnrollmentForSchedulingACampaign() {
        int timeOffsetGreaterThanADay = (24 * 60 * 60) + 1;
        String externalId = "externalId";
        String campaignName = "campaignName";
        CampaignEnrollment enrollment = new CampaignEnrollment(externalId, campaignName);
        enrollment.setDeliverTime(null);
        CampaignMessage campaignMessage = new CampaignMessage(null, new Period(timeOffsetGreaterThanADay * 1000));
        campaignMessage.setName(campaignName);
        OffsetCampaign campaign = CampaignBuilder.defaultOffsetCampaign();

        expectedException.expect(CampaignEnrollmentException.class);
        expectedException.expectMessage(String.format("Cannot enroll %s for message campaign %s - Start time not defined for campaign. Define it in campaign-message.json or at enrollment time", externalId, campaignName));

        offsetCampaignSchedulerService.scheduleMessageJob(enrollment, campaign, campaignMessage);

        verifyZeroInteractions(schedulerService);
    }

    private void assertMotechEvent(RunOnceSchedulableJob runOnceSchedulableJob, String expectedJobId, String messageKey) {
        Assert.assertEquals(expectedJobId, runOnceSchedulableJob.getMotechEvent().getParameters().get("JobID"));
        Assert.assertEquals("testCampaign", runOnceSchedulableJob.getMotechEvent().getParameters().get("CampaignName"));
        Assert.assertEquals("12345", runOnceSchedulableJob.getMotechEvent().getParameters().get("ExternalID"));
        Assert.assertEquals(messageKey, runOnceSchedulableJob.getMotechEvent().getParameters().get("MessageKey"));
    }
}
