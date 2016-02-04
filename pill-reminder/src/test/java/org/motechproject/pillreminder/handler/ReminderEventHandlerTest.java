package org.motechproject.pillreminder.handler;


import org.joda.time.LocalTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.motechproject.commons.date.model.Time;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.pillreminder.EventKeys;
import org.motechproject.pillreminder.builder.SchedulerPayloadBuilder;
import org.motechproject.pillreminder.builder.testbuilder.DosageBuilder;
import org.motechproject.pillreminder.builder.testbuilder.PillRegimenBuilder;
import org.motechproject.pillreminder.dao.PillRegimenDataService;
import org.motechproject.pillreminder.domain.DailyScheduleDetails;
import org.motechproject.pillreminder.domain.Dosage;
import org.motechproject.pillreminder.domain.PillRegimen;
import org.motechproject.scheduler.contract.RepeatingSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.motechproject.testing.utils.TimeFaker;

import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.testing.utils.TimeFaker.fakeNow;

public class ReminderEventHandlerTest {

    @Mock
    EventRelay eventRelay;

    @Mock
    PillRegimenDataService pillRegimenDataService;

    @Mock
    MotechSchedulerService schedulerService;

    ReminderEventHandler pillReminderEventHandler;
    LocalTime reminderStartTime = new LocalTime(10, 25);
    int pillWindow = 1;
    int retryInterval = 10;
    int bufferOverDosageTimeInMinutes = 5;


    @Before
    public void setUp() {
        initMocks(this);
        pillReminderEventHandler = new ReminderEventHandler(eventRelay, pillRegimenDataService, schedulerService);
    }

    @After
    public void tearDown() {
        TimeFaker.stopFakingTime();
    }

    @Test
    public void shouldRaiseEventsForEachPillWindow() {
        fakeNow(DateUtil.today().toDateTime(reminderStartTime.plusMinutes(25)));

        int pillWindow = 1;
        String externalId = "externalId";
        long dosageId = 26;
        int retryInterval = 10;

        Dosage dosage = buildDosageNotYetTaken(dosageId);
        PillRegimen pillRegimen = buildPillRegimen(externalId, pillWindow, bufferOverDosageTimeInMinutes, dosage, retryInterval);

        when(pillRegimenDataService.findByExternalId(externalId)).thenReturn(pillRegimen);

        MotechEvent motechEvent = buildMotechEvent(externalId, dosageId);
        pillReminderEventHandler.handleEvent(motechEvent);

        verify(pillRegimenDataService, atLeastOnce()).findByExternalId(externalId);
        verify(eventRelay, only()).sendEventMessage(Matchers.<MotechEvent>any());
    }

    @Test
    public void shouldRaiseEventWithInformationAboutTheNumberOfTimesItHasBeenRaisedIncludingCurrentEvent() {
        fakeNow(DateUtil.today().toDateTime(reminderStartTime.plusMinutes(25)));

        String externalId = "externalId";
        long dosageId = 11L;
        ArgumentCaptor<MotechEvent> event = ArgumentCaptor.forClass(MotechEvent.class);

        Dosage dosage = buildDosageNotYetTaken(dosageId);
        PillRegimen pillRegimen = buildPillRegimen(externalId, pillWindow, bufferOverDosageTimeInMinutes, dosage, retryInterval);
        when(pillRegimenDataService.findByExternalId(externalId)).thenReturn(pillRegimen);

        MotechEvent motechEvent = buildMotechEvent(externalId, dosageId);
        pillReminderEventHandler.handleEvent(motechEvent);

        verify(pillRegimenDataService, atLeastOnce()).findByExternalId(externalId);
        verify(eventRelay, times(1)).sendEventMessage(event.capture());

        assertNotNull(event.getValue().getParameters());
        assertEquals(2, event.getValue().getParameters().get(EventKeys.PILLREMINDER_TIMES_SENT));
    }

    @Test
    public void shouldRaiseEventWithInformationAboutTheNumberOfTimesItWillBeRaisedForEveryPillWindow() {
        fakeNow(DateUtil.today().toDateTime(reminderStartTime));

        String externalId = "externalId";
        long dosageId = 87;
        ArgumentCaptor<MotechEvent> event = ArgumentCaptor.forClass(MotechEvent.class);

        Dosage dosage = buildDosageNotYetTaken(dosageId);
        PillRegimen pillRegimen = buildPillRegimen(externalId, pillWindow, bufferOverDosageTimeInMinutes, dosage, retryInterval);

        when(pillRegimenDataService.findByExternalId(externalId)).thenReturn(pillRegimen);

        MotechEvent motechEvent = buildMotechEvent(externalId, dosageId);
        pillReminderEventHandler.handleEvent(motechEvent);

        verify(pillRegimenDataService, atLeastOnce()).findByExternalId(externalId);
        verify(eventRelay, times(1)).sendEventMessage(event.capture());

        assertNotNull(event.getValue().getParameters());
        assertEquals(6, event.getValue().getParameters().get(EventKeys.PILLREMINDER_TOTAL_TIMES_TO_SEND));
    }

    @Test
    public void shouldRaiseEventWithInformationAboutTheRetryIntervalTime() {
        fakeNow(DateUtil.today().toDateTime(reminderStartTime.plusMinutes(25)));

        int pillWindow = 0;
        String externalId = "externalId";
        long dosageId = 10L;
        ArgumentCaptor<MotechEvent> event = ArgumentCaptor.forClass(MotechEvent.class);

        Dosage dosage = buildDosageNotYetTaken(dosageId);
        PillRegimen pillRegimen = buildPillRegimen(externalId, pillWindow, bufferOverDosageTimeInMinutes, dosage, retryInterval);

        when(pillRegimenDataService.findByExternalId(externalId)).thenReturn(pillRegimen);

        MotechEvent motechEvent = buildMotechEvent(externalId, dosageId);
        pillReminderEventHandler.handleEvent(motechEvent);

        verify(pillRegimenDataService, atLeastOnce()).findByExternalId(externalId);
        verify(eventRelay, times(1)).sendEventMessage(event.capture());

        assertNotNull(event.getValue().getParameters());
        assertEquals(retryInterval, event.getValue().getParameters().get(EventKeys.PILLREMINDER_RETRY_INTERVAL));
    }

    @Test
    public void shoulNotRaiseEventWhenDosageIsAlreadyTakenForCurrentPillWindow() {
        int pillWindow = 0;
        String externalId = "externalId";
        long dosageId = 10;
        int retryInterval = 4;

        Dosage dosage = buildDosageTaken(dosageId);

        PillRegimen pillRegimen = buildPillRegimen(externalId, pillWindow, bufferOverDosageTimeInMinutes, dosage, retryInterval);

        when(pillRegimenDataService.findByExternalId(externalId)).thenReturn(pillRegimen);

        MotechEvent motechEvent = buildMotechEvent(externalId, dosageId);
        pillReminderEventHandler.handleEvent(motechEvent);

        verify(pillRegimenDataService, atLeastOnce()).findByExternalId(externalId);
        verify(eventRelay, never()).sendEventMessage(Matchers.<MotechEvent>any());
    }

    @Test
    public void shouldScheduleRepeatRemindersForFirstCall() {
        fakeNow(DateUtil.today().toDateTime(reminderStartTime));
        String externalId = "externalId";
        long dosageId = 17;

        Dosage dosage = buildDosageNotYetTaken(dosageId);

        PillRegimen pillRegimen = buildPillRegimen(externalId, pillWindow, bufferOverDosageTimeInMinutes, dosage, retryInterval);

        when(pillRegimenDataService.findByExternalId(externalId)).thenReturn(pillRegimen);

        MotechEvent motechEvent = buildMotechEvent(externalId, dosageId);
        pillReminderEventHandler.handleEvent(motechEvent);

        ArgumentCaptor<RepeatingSchedulableJob> captor = ArgumentCaptor.forClass(RepeatingSchedulableJob.class);
        verify(schedulerService).safeScheduleRepeatingJob(captor.capture());

        assertEquals(10, captor.getValue().getStartTime().getHourOfDay());
        assertEquals(35, captor.getValue().getStartTime().getMinuteOfHour());
        assertEquals(String.valueOf(dosage.getId()), captor.getValue().getMotechEvent().getParameters().get(MotechSchedulerService.JOB_ID_KEY));
    }

    @Test
    public void shouldNotScheduleRepeatRemindersForReminderCalls() {
        int pillWindow = 1;
        String externalId = "externalId";
        long dosageId = 14;
        int retryInterval = 4;

        Dosage dosage = buildDosageNotYetTaken(dosageId);

        PillRegimen pillRegimen = buildPillRegimen(externalId, pillWindow, bufferOverDosageTimeInMinutes, dosage, retryInterval);

        when(pillRegimenDataService.findByExternalId(externalId)).thenReturn(pillRegimen);

        MotechEvent motechEvent = buildMotechEvent(externalId, dosageId);

        pillReminderEventHandler.handleEvent(motechEvent);

        verify(schedulerService, never()).scheduleRepeatingJob(Matchers.<RepeatingSchedulableJob>any());
    }

    private MotechEvent buildMotechEvent(String externalId, Long dosageId) {
        Map<String, Object> eventParams = new SchedulerPayloadBuilder().withDosageId(dosageId).withExternalId(externalId).payload();
        return new MotechEvent(EventKeys.PILLREMINDER_REMINDER_EVENT_SUBJECT, eventParams);
    }

    private Dosage buildDosageNotYetTaken(Long dosageId) {
        return DosageBuilder.newDosage()
                .withDosageTime(new Time(reminderStartTime))
                .withResponseLastCapturedDate(DateUtil.today().minusDays(1))
                .withId(dosageId)
                .build();
    }

    private Dosage buildDosageTaken(Long dosageId) {
        return DosageBuilder.newDosage()
                .withDosageTime(new Time(10, 25))
                .withResponseLastCapturedDate(DateUtil.today())
                .withId(dosageId)
                .build();
    }

    private PillRegimen buildPillRegimen(String externalId, int pillWindow, int bufferOverDosageTimeInMinutes, Dosage dosage, int retryInterval) {
        return PillRegimenBuilder.newPillRegimen()
                .withExternalId(externalId)
                .withScheduleDetails(new DailyScheduleDetails(retryInterval, pillWindow, bufferOverDosageTimeInMinutes))
                .withSingleDosage(dosage)
                .build();
    }
}
