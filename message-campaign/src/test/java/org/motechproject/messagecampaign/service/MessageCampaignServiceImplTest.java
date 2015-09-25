package org.motechproject.messagecampaign.service;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.motechproject.commons.date.model.Time;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.messagecampaign.EventKeys;
import org.motechproject.messagecampaign.builder.EnrollRequestBuilder;
import org.motechproject.messagecampaign.contract.CampaignRequest;
import org.motechproject.messagecampaign.dao.CampaignEnrollmentDataService;
import org.motechproject.messagecampaign.dao.CampaignMessageRecordService;
import org.motechproject.messagecampaign.dao.CampaignRecordService;
import org.motechproject.messagecampaign.domain.campaign.AbsoluteCampaign;
import org.motechproject.messagecampaign.domain.campaign.Campaign;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollmentStatus;
import org.motechproject.messagecampaign.domain.campaign.CampaignRecord;
import org.motechproject.messagecampaign.domain.campaign.CampaignType;
import org.motechproject.messagecampaign.domain.campaign.CronBasedCampaign;
import org.motechproject.messagecampaign.domain.campaign.CronBasedCampaignMessage;
import org.motechproject.messagecampaign.exception.CampaignNotFoundException;
import org.motechproject.messagecampaign.exception.EnrollmentAlreadyExists;
import org.motechproject.messagecampaign.exception.EnrollmentNotFoundException;
import org.motechproject.messagecampaign.scheduler.CampaignSchedulerFactory;
import org.motechproject.messagecampaign.scheduler.CampaignSchedulerService;
import org.motechproject.messagecampaign.scheduler.JobIdFactory;
import org.motechproject.messagecampaign.search.Criterion;
import org.motechproject.scheduler.contract.CronJobId;
import org.motechproject.scheduler.contract.JobId;
import org.motechproject.scheduler.service.MotechSchedulerService;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.commons.date.util.DateUtil.now;

public class MessageCampaignServiceImplTest {
    private MessageCampaignServiceImpl messageCampaignService;
    @Mock
    private CampaignRecordService campaignRecordService;
    @Mock
    private CampaignMessageRecordService campaignMessageRecordService;
    @Mock
    private EnrollmentService enrollmentService;
    @Mock
    private MotechSchedulerService schedulerService;
    @Mock
    private CampaignEnrollmentRecordMapper campaignEnrollmentRecordMapper;
    @Mock
    private MotechSchedulerService mockSchedulerService;
    @Mock
    private CampaignEnrollmentDataService campaignEnrollmentDataService;
    @Mock
    private CampaignSchedulerFactory campaignSchedulerFactory;
    @Mock
    private CampaignRecord campaignRecord;
    @Mock
    private EventRelay eventRelay;
    @Mock
    private CampaignSchedulerService campaignSchedulerService;

    @Before
    public void setUp() {
        initMocks(this);
        messageCampaignService = new MessageCampaignServiceImpl();
        messageCampaignService.setCampaignEnrollmentDataService(campaignEnrollmentDataService);
        messageCampaignService.setCampaignEnrollmentRecordMapper(campaignEnrollmentRecordMapper);
        messageCampaignService.setCampaignMessageRecordService(campaignMessageRecordService);
        messageCampaignService.setCampaignRecordService(campaignRecordService);
        messageCampaignService.setRelay(eventRelay);
        messageCampaignService.setSchedulerService(schedulerService);
        messageCampaignService.setCampaignSchedulerFactory(campaignSchedulerFactory);
        messageCampaignService.setEnrollmentService(enrollmentService);
    }

    @Test
    public void shouldCreateEnrollmentWhenScheduleIsStarted() {
        Campaign campaign = mock(Campaign.class);

        when(campaignRecordService.findByName("testCampaign")).thenReturn(campaignRecord);
        when(campaignRecord.toCampaign()).thenReturn(campaign);

        CampaignSchedulerService campaignScheduler = mock(CampaignSchedulerService.class);
        when(campaignSchedulerFactory.getCampaignScheduler("testCampaign")).thenReturn(campaignScheduler);

        CampaignRequest request = new EnrollRequestBuilder().withDefaults()
                .withReferenceDate(new LocalDate(2011, 11, 22))
                .withDeliverTime(new Time(8, 30))
                .build();
        messageCampaignService.enroll(request);

        ArgumentCaptor<CampaignEnrollment> campaignEnrollmentCaptor = ArgumentCaptor.forClass(CampaignEnrollment.class);
        verify(enrollmentService).register(campaignEnrollmentCaptor.capture());

        CampaignEnrollment campaignEnrollment = campaignEnrollmentCaptor.getValue();
        assertThat(campaignEnrollment.getReferenceDate(), is(new LocalDate(2011, 11, 22)));

        ArgumentCaptor<MotechEvent> motechEventArgumentCaptor = ArgumentCaptor.forClass(MotechEvent.class);
        verify(eventRelay).sendEventMessage(motechEventArgumentCaptor.capture());
        MotechEvent event = motechEventArgumentCaptor.getValue();
        assertEquals(event.getSubject(), EventKeys.ENROLLED_USER_SUBJECT);
        assertEquals(request.externalId(), event.getParameters().get(EventKeys.EXTERNAL_ID_KEY));
        assertEquals(request.campaignName(), event.getParameters().get(EventKeys.CAMPAIGN_NAME_KEY));
    }

    @Test
    public void shouldUnRegisterEnrollmentWhenScheduleIsStopped() {
        Campaign campaign = mock(Campaign.class);
        when(campaignRecordService.findByName("testCampaign")).thenReturn(campaignRecord);
        when(campaignRecord.toCampaign()).thenReturn(campaign);
        CampaignSchedulerService campaignScheduler = mock(CampaignSchedulerService.class);
        when(campaignSchedulerFactory.getCampaignScheduler("testCampaign")).thenReturn(campaignScheduler);

        CampaignRequest request = new EnrollRequestBuilder().withDefaults()
                .withReferenceDate(new LocalDate(2011, 11, 22))
                .withDeliverTime(new Time(8, 30))
                .build();

        when(campaignEnrollmentDataService.findByExternalIdAndCampaignName(request.externalId(), request.campaignName())).thenReturn(new CampaignEnrollment(request.externalId(), request.campaignName()));

        messageCampaignService.unenroll(request.externalId(), request.campaignName());

        verify(enrollmentService).unregister(request.externalId(), request.campaignName());

        ArgumentCaptor<MotechEvent> motechEventArgumentCaptor = ArgumentCaptor.forClass(MotechEvent.class);
        verify(eventRelay).sendEventMessage(motechEventArgumentCaptor.capture());
        MotechEvent event = motechEventArgumentCaptor.getValue();
        assertEquals(event.getSubject(), EventKeys.UNENROLLED_USER_SUBJECT);
        assertEquals(request.externalId(), event.getParameters().get(EventKeys.EXTERNAL_ID_KEY));
        assertEquals(request.campaignName(), event.getParameters().get(EventKeys.CAMPAIGN_NAME_KEY));
    }

    @Test
    public void shouldUnregisterAllCampaignsMatchingQuery() {
        Campaign campaign = mock(Campaign.class);
        when(campaignRecordService.findByName("testCampaign")).thenReturn(campaignRecord);
        when(campaignRecord.toCampaign()).thenReturn(campaign);

        CampaignEnrollment enrollment1 = new CampaignEnrollment("external_id_1", "testCampaign");
        CampaignEnrollment enrollment2 = new CampaignEnrollment("external_id_2", "testCampaign");
        when(enrollmentService.search(any(CampaignEnrollmentsQuery.class)))
                .thenReturn(asList(enrollment1, enrollment2));

        CampaignSchedulerService campaignScheduler = mock(CampaignSchedulerService.class);
        when(campaignSchedulerFactory.getCampaignScheduler("testCampaign")).thenReturn(campaignScheduler);

        CampaignEnrollmentsQuery query = new CampaignEnrollmentsQuery().withCampaignName("testCampaign");

        messageCampaignService.stopAll(query);

        ArgumentCaptor<CampaignEnrollmentsQuery> captor = ArgumentCaptor.forClass(CampaignEnrollmentsQuery.class);
        verify(enrollmentService).search(captor.capture());

        assertEquals(0, captor.getValue().getSecondaryCriteria().size());
        Criterion primaryCriterion = captor.getValue().getPrimaryCriterion();
        primaryCriterion.fetch(campaignEnrollmentDataService);
        verify(campaignEnrollmentDataService).findByCampaignName("testCampaign");

        verify(enrollmentService).unregister("external_id_1", "testCampaign");
        verify(enrollmentService).unregister("external_id_2", "testCampaign");
        verify(campaignSchedulerFactory, times(2)).getCampaignScheduler("testCampaign");
        verify(campaignScheduler).stop(enrollment1);
        verify(campaignScheduler).stop(enrollment2);
    }

    @Test
    public void shouldCallCampaignSchedulerToStart() {
        CampaignRequest campaignRequest = new CampaignRequest("entity_1", "campaign-name", null, null);

        AbsoluteCampaign absoluteCampaign = mock(AbsoluteCampaign.class);
        when(campaignRecordService.findByName("campaign-name")).thenReturn(campaignRecord);
        when(campaignRecord.toCampaign()).thenReturn(absoluteCampaign);

        CampaignSchedulerService campaignScheduler = mock(CampaignSchedulerService.class);
        when(campaignSchedulerFactory.getCampaignScheduler("campaign-name")).thenReturn(campaignScheduler);

        messageCampaignService.enroll(campaignRequest);

        ArgumentCaptor<CampaignEnrollment> enrollment = ArgumentCaptor.forClass(CampaignEnrollment.class);
        verify(campaignScheduler).start(enrollment.capture());
        assertEquals("entity_1", enrollment.getValue().getExternalId());
        assertEquals("campaign-name", enrollment.getValue().getCampaignName());

        ArgumentCaptor<MotechEvent> motechEventArgumentCaptor = ArgumentCaptor.forClass(MotechEvent.class);
        verify(eventRelay).sendEventMessage(motechEventArgumentCaptor.capture());
        MotechEvent event = motechEventArgumentCaptor.getValue();
        assertEquals(event.getSubject(), EventKeys.ENROLLED_USER_SUBJECT);
        assertEquals(enrollment.getValue().getExternalId(), event.getParameters().get(EventKeys.EXTERNAL_ID_KEY));
        assertEquals(enrollment.getValue().getCampaignName(), event.getParameters().get(EventKeys.CAMPAIGN_NAME_KEY));
    }

    @Test
    public void shouldReturnListOfCampaignEnrollmentsForTheGivenQuery() {
        CampaignEnrollmentsQuery enrollmentQuery = mock(CampaignEnrollmentsQuery.class);
        CampaignEnrollment enrollment1 = new CampaignEnrollment("external_id_1", null);
        CampaignEnrollment enrollment2 = new CampaignEnrollment("external_id_2", null);
        List<CampaignEnrollment> enrollments = asList(enrollment1, enrollment2);

        when(enrollmentService.search(enrollmentQuery)).thenReturn(enrollments);
        CampaignEnrollmentRecord record1 = new CampaignEnrollmentRecord(null, null, null, null);
        CampaignEnrollmentRecord record2 = new CampaignEnrollmentRecord(null, null, null, null);
        when(campaignEnrollmentRecordMapper.map(enrollment1)).thenReturn(record1);
        when(campaignEnrollmentRecordMapper.map(enrollment2)).thenReturn(record2);

        assertEquals(asList(new CampaignEnrollmentRecord[]{record1, record2}), messageCampaignService.search(enrollmentQuery));
    }

    @Test
    public void shouldGetCampaignTimings() {
        AbsoluteCampaign campaign = mock(AbsoluteCampaign.class);

        when(campaignRecordService.findByName("campaign")).thenReturn(campaignRecord);
        when(campaignRecord.toCampaign()).thenReturn(campaign);

        CampaignSchedulerService campaignScheduler = mock(CampaignSchedulerService.class);
        when(campaignSchedulerFactory.getCampaignScheduler("campaign")).thenReturn(campaignScheduler);

        CampaignEnrollment enrollment = new CampaignEnrollment("entity_1", "campaign");
        when(campaignEnrollmentDataService.findByExternalIdAndCampaignName("entity_1", "campaign")).thenReturn(enrollment);

        DateTime now = now();
        DateTime startDate = now.plusDays(1);
        DateTime endDate = now.plusDays(1).plusDays(5);
        messageCampaignService.getCampaignTimings("entity_1", "campaign", startDate, endDate);

        verify(campaignScheduler).getCampaignTimings(startDate, endDate, enrollment);
    }

    @Test
    public void shouldGetEmptyCampaignTimingsMapIfEnrollmentIsNotActive() {
        AbsoluteCampaign campaign = mock(AbsoluteCampaign.class);

        when(campaignRecordService.findByName("campaign")).thenReturn(campaignRecord);
        when(campaignRecord.toCampaign()).thenReturn(campaign);

        CampaignSchedulerService campaignScheduler = mock(CampaignSchedulerService.class);
        when(campaignSchedulerFactory.getCampaignScheduler("campaign")).thenReturn(campaignScheduler);

        CampaignEnrollment enrollment = new CampaignEnrollment("entity_1", "campaign");
        enrollment.setStatus(CampaignEnrollmentStatus.INACTIVE);
        when(campaignEnrollmentDataService.findByExternalIdAndCampaignName("entity_1", "campaign")).thenReturn(enrollment);

        DateTime now = now();
        DateTime startDate = now.plusDays(1);
        DateTime endDate = now.plusDays(1).plusDays(5);
        final Map<String, List<DateTime>> campaignTimings = messageCampaignService.getCampaignTimings("entity_1", "campaign", startDate, endDate);
        assertEquals(0, campaignTimings.size());
    }

    @Test
    public void shouldSaveCampaigns() {
        messageCampaignService.saveCampaign(campaignRecord);
        verify(campaignRecordService).create(campaignRecord);
    }

    @Test
    public void shouldRemoveCampaigns() {
        CampaignEnrollment enrollment = new CampaignEnrollment("extID", "PREGNANCY");

        when(campaignSchedulerFactory.getCampaignScheduler("PREGNANCY")).thenReturn(campaignSchedulerService);
        when(campaignRecordService.findByName("PREGNANCY")).thenReturn(campaignRecord);
        when(enrollmentService.search(any(CampaignEnrollmentsQuery.class)))
                .thenReturn(asList(enrollment));

        messageCampaignService.deleteCampaign("PREGNANCY");

        verify(campaignRecordService).findByName("PREGNANCY");
        verify(campaignRecordService).delete(campaignRecord);

        ArgumentCaptor<CampaignEnrollmentsQuery> captor = ArgumentCaptor.forClass(CampaignEnrollmentsQuery.class);
        verify(enrollmentService).search(captor.capture());

        Criterion primaryCriterion = captor.getValue().getPrimaryCriterion();
        primaryCriterion.fetch(campaignEnrollmentDataService);
        verify(campaignEnrollmentDataService).findByCampaignName("PREGNANCY");
        assertTrue(captor.getValue().getSecondaryCriteria().isEmpty());

        verify(campaignSchedulerService).stop(enrollment);
        verify(enrollmentService).delete(enrollment);
    }

    @Test
    public void shouldRetrieveCampaignByName() {
        when(campaignRecordService.findByName("PREGNANCY")).thenReturn(campaignRecord);
        assertEquals(campaignRecord, messageCampaignService.getCampaignRecord("PREGNANCY"));
        verify(campaignRecordService).findByName("PREGNANCY");
    }

    @Test
    public void shouldRetrieveAllCampaigns() {
        when(campaignRecordService.retrieveAll()).thenReturn(asList(campaignRecord));
        assertEquals(asList(campaignRecord), messageCampaignService.getAllCampaignRecords());
        verify(campaignRecordService).retrieveAll();
    }

    @Test(expected = CampaignNotFoundException.class)
    public void shouldThrowExceptionWhenDeletingNonExistantCampaign() {
        when(campaignRecordService.findByName("PREGNANCY")).thenReturn(null);
        messageCampaignService.deleteCampaign("PREGNANCY");
    }

    @Test
    public void shouldUpdateExistingEnrollments() {
        final LocalDate now = LocalDate.now();

        CampaignEnrollment enrollment = new CampaignEnrollment("oldExtId", "campaign");
        enrollment.setStatus(CampaignEnrollmentStatus.ACTIVE);
        enrollment.setDeliverTime(new Time(10, 50));
        enrollment.setId(9001L);
        enrollment.setReferenceDate(now.plusWeeks(1));

        CampaignSchedulerService campaignScheduler = mock(CampaignSchedulerService.class);

        when(campaignEnrollmentDataService.findById(9001L)).thenReturn(enrollment);
        when(campaignSchedulerFactory.getCampaignScheduler("campaign")).thenReturn(campaignScheduler);
        CampaignRequest campaignRequest = new CampaignRequest("enrollmentId", "campaign", now, new Time(10, 0));

        messageCampaignService.updateEnrollment(campaignRequest, 9001L);

        verify(campaignScheduler).stop(enrollment);

        ArgumentMatcher<CampaignEnrollment> matcher = new ArgumentMatcher<CampaignEnrollment>() {
            @Override
            public boolean matches(Object argument) {
                CampaignEnrollment enrollment = (CampaignEnrollment) argument;
                return Objects.equals("campaign", enrollment.getCampaignName()) && Objects.equals("enrollmentId", enrollment.getExternalId())
                        && Objects.equals(new Time(10, 0), enrollment.getDeliverTime())
                        && Objects.equals(now, enrollment.getReferenceDate());
            }
        };

        verify(campaignEnrollmentDataService).update(argThat(matcher));
        verify(campaignScheduler).start(argThat(matcher));
    }

    @Test(expected = EnrollmentNotFoundException.class)
    public void shouldThrowExceptionWhenUpdatingNonExistentEnrollment() {
        when(campaignEnrollmentDataService.findByExternalId("id")).thenReturn(null);
        messageCampaignService.updateEnrollment(new CampaignRequest(), 9001L);
    }

    @Test(expected = EnrollmentAlreadyExists.class)
    public void shouldThrowExceptionForDuplicateExtIdAndCampaignName() {
        CampaignEnrollment enrollment = new CampaignEnrollment("extId", "PREGNANCY");
        enrollment.setId(9001L);
        CampaignEnrollment otherEnrollment = new CampaignEnrollment("extId2", "PREGNANCY");
        enrollment.setId(9002L);

        when(campaignEnrollmentDataService.findById(9001L)).thenReturn(enrollment);
        when(campaignEnrollmentDataService.findByExternalIdAndCampaignName("extId2", "PREGNANCY")).thenReturn(otherEnrollment);

        CampaignRequest campaignRequest = new CampaignRequest("extId2", "PREGNANCY", null, null);

        messageCampaignService.updateEnrollment(campaignRequest, 9001L);
    }

    @Test
    public void shouldGetLatestCampaignMessage() {
        CampaignEnrollment enrollment = new CampaignEnrollment("externalId", "campaignName");
        CampaignRecord record = new CampaignRecord();
        record.setCampaignType(CampaignType.CRON);
        record.setName("campaignName");

        CronBasedCampaignMessage messageRecord1 = new CronBasedCampaignMessage("0 11 11 11 11 ?");
        messageRecord1.setMessageKey("messageKey1");
        CronBasedCampaignMessage messageRecord2 = new CronBasedCampaignMessage("0 11 11 11 11 ?");
        messageRecord2.setMessageKey("messageKey2");

        CronBasedCampaign campaign = new CronBasedCampaign("campaignName", asList(messageRecord1, messageRecord2));

        JobIdFactory factory = new JobIdFactory();
        JobId jobId1 = new CronJobId(EventKeys.SEND_MESSAGE, factory.getMessageJobIdFor(messageRecord1.getMessageKey(),
                enrollment.getExternalId(), enrollment.getCampaignName()));
        JobId jobId2 = new CronJobId(EventKeys.SEND_MESSAGE, factory.getMessageJobIdFor(messageRecord2.getMessageKey(),
                enrollment.getExternalId(), enrollment.getCampaignName()));

        when(campaignSchedulerFactory.getCampaignScheduler("campaignName")).thenReturn(campaignSchedulerService);
        when(campaignSchedulerService.getJobId(messageRecord1.getMessageKey(), "externalId", "campaignName")).thenReturn(jobId1);
        when(campaignSchedulerService.getJobId(messageRecord2.getMessageKey(), "externalId", "campaignName")).thenReturn(jobId2);
        when(campaignEnrollmentDataService.findByExternalIdAndCampaignName(enrollment.getExternalId(),
                enrollment.getCampaignName())).thenReturn(enrollment);
        when(campaignRecordService.findByName(enrollment.getCampaignName())).thenReturn(campaignRecord);
        when(campaignRecord.toCampaign()).thenReturn(campaign);
        when(schedulerService.getPreviousFireDate(jobId1)).thenReturn(new DateTime(2002, 5, 15, 12, 0));
        when(schedulerService.getPreviousFireDate(jobId2)).thenReturn(new DateTime(2002, 5, 15, 11, 55));

        String latestCampaignMessage = messageCampaignService.getLatestCampaignMessage("campaignName", "externalId");

        Assert.assertEquals(messageRecord1.getMessageKey(), latestCampaignMessage);
    }

    @Test
    public void shouldNotGetLatestCampaignMessageIfNothingSent() {
        CampaignEnrollment enrollment = new CampaignEnrollment("externalId", "campaignName");
        CampaignRecord record = new CampaignRecord();
        record.setCampaignType(CampaignType.CRON);
        record.setName("campaignName");

        CronBasedCampaignMessage messageRecord1 = new CronBasedCampaignMessage("0 11 11 11 11 ?");
        messageRecord1.setMessageKey("messageKey1");

        CronBasedCampaign campaign = new CronBasedCampaign("campaignName", asList(messageRecord1));

        JobIdFactory factory = new JobIdFactory();
        JobId jobId1 = new CronJobId(EventKeys.SEND_MESSAGE, factory.getMessageJobIdFor(messageRecord1.getMessageKey(),
                enrollment.getExternalId(), enrollment.getCampaignName()));

        when(campaignSchedulerFactory.getCampaignScheduler("campaignName")).thenReturn(campaignSchedulerService);
        when(campaignSchedulerService.getJobId(messageRecord1.getMessageKey(), "externalId", "campaignName")).thenReturn(jobId1);
        when(campaignEnrollmentDataService.findByExternalIdAndCampaignName(enrollment.getExternalId(),
                enrollment.getCampaignName())).thenReturn(enrollment);
        when(campaignRecordService.findByName(enrollment.getCampaignName())).thenReturn(campaignRecord);
        when(campaignRecord.toCampaign()).thenReturn(campaign);
        when(schedulerService.getPreviousFireDate(jobId1)).thenReturn(new DateTime(2020, 5, 15, 12, 0));

        String latestCampaignMessage = messageCampaignService.getLatestCampaignMessage("campaignName", "externalId");

        Assert.assertNull(latestCampaignMessage);
    }

    @Test
    public void shouldGetNextCampaignMessage() {
        CampaignEnrollment enrollment = new CampaignEnrollment("externalId", "campaignName");
        CampaignRecord record = new CampaignRecord();
        record.setCampaignType(CampaignType.CRON);
        record.setName("campaignName");

        CronBasedCampaignMessage messageRecord1 = new CronBasedCampaignMessage("0 11 11 11 11 ?");
        messageRecord1.setMessageKey("messageKey1");
        CronBasedCampaignMessage messageRecord2 = new CronBasedCampaignMessage("0 11 11 11 11 ?");
        messageRecord2.setMessageKey("messageKey2");

        CronBasedCampaign campaign = new CronBasedCampaign("campaignName", asList(messageRecord1, messageRecord2));

        JobIdFactory factory = new JobIdFactory();
        JobId jobId1 = new CronJobId(EventKeys.SEND_MESSAGE, factory.getMessageJobIdFor(messageRecord1.getMessageKey(),
                enrollment.getExternalId(), enrollment.getCampaignName()));
        JobId jobId2 = new CronJobId(EventKeys.SEND_MESSAGE, factory.getMessageJobIdFor(messageRecord2.getMessageKey(),
                enrollment.getExternalId(), enrollment.getCampaignName()));

        when(campaignSchedulerFactory.getCampaignScheduler("campaignName")).thenReturn(campaignSchedulerService);
        when(campaignSchedulerService.getJobId(messageRecord1.getMessageKey(), "externalId", "campaignName")).thenReturn(jobId1);
        when(campaignSchedulerService.getJobId(messageRecord2.getMessageKey(), "externalId", "campaignName")).thenReturn(jobId2);
        when(campaignEnrollmentDataService.findByExternalIdAndCampaignName(enrollment.getExternalId(),
                enrollment.getCampaignName())).thenReturn(enrollment);
        when(campaignRecordService.findByName(enrollment.getCampaignName())).thenReturn(campaignRecord);
        when(campaignRecord.toCampaign()).thenReturn(campaign);
        when(schedulerService.getNextFireDate(jobId1)).thenReturn(new DateTime(2020, 5, 15, 12, 0));
        when(schedulerService.getNextFireDate(jobId2)).thenReturn(new DateTime(2020, 5, 15, 11, 55));

        String nextCampaignMessage = messageCampaignService.getNextCampaignMessage("campaignName", "externalId");

        Assert.assertEquals(messageRecord2.getMessageKey(), nextCampaignMessage);
    }

    @Test
    public void shouldNotGetNextCampaignMessageIfNothingMoreToSend() {
        CampaignEnrollment enrollment = new CampaignEnrollment("externalId", "campaignName");
        CampaignRecord record = new CampaignRecord();
        record.setCampaignType(CampaignType.CRON);
        record.setName("campaignName");

        CronBasedCampaignMessage messageRecord1 = new CronBasedCampaignMessage("0 11 11 11 11 ?");
        messageRecord1.setMessageKey("messageKey1");

        CronBasedCampaign campaign = new CronBasedCampaign("campaignName", asList(messageRecord1));

        JobIdFactory factory = new JobIdFactory();
        JobId jobId1 = new CronJobId(EventKeys.SEND_MESSAGE, factory.getMessageJobIdFor(messageRecord1.getMessageKey(),
                enrollment.getExternalId(), enrollment.getCampaignName()));

        when(campaignSchedulerFactory.getCampaignScheduler("campaignName")).thenReturn(campaignSchedulerService);
        when(campaignSchedulerService.getJobId(messageRecord1.getMessageKey(), "externalId", "campaignName")).thenReturn(jobId1);
        when(campaignEnrollmentDataService.findByExternalIdAndCampaignName(enrollment.getExternalId(),
                enrollment.getCampaignName())).thenReturn(enrollment);
        when(campaignRecordService.findByName(enrollment.getCampaignName())).thenReturn(campaignRecord);
        when(campaignRecord.toCampaign()).thenReturn(campaign);
        when(schedulerService.getNextFireDate(jobId1)).thenReturn(new DateTime(1920, 5, 15, 12, 0));

        String nextCampaignMessage = messageCampaignService.getNextCampaignMessage("campaignName", "externalId");

        Assert.assertNull(nextCampaignMessage);
    }
}
