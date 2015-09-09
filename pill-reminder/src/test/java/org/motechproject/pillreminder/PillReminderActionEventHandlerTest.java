package org.motechproject.pillreminder;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.event.MotechEvent;
import org.motechproject.pillreminder.event.EventKeys;
import org.motechproject.pillreminder.handler.PillReminderActionEventHandler;
import org.motechproject.pillreminder.service.PillReminderService;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class PillReminderActionEventHandlerTest {

    @Mock
    private PillReminderService pillReminder;

    private PillReminderActionEventHandler eventHandler;

    @Before
    public void setUp() {
        initMocks(this);
        eventHandler = new PillReminderActionEventHandler(pillReminder);
    }


    @Test
    public void shouldHandleUpdateDosageStatusEvent() {

        Long pillRegimenId = new Long(123456789);
        Long dosageId = new Long(987654321);
        LocalDate date = new LocalDate();

        MotechEvent event = new MotechEvent(EventKeys.PILLREMINDER_REMINDER_EVENT_SUBJECT_DOSAGE_STATUS_KNOWN);
        event.getParameters().put(EventKeys.PILL_REGIMEN_ID, pillRegimenId);
        event.getParameters().put(EventKeys.DOSAGE_ID_KEY, dosageId);
        event.getParameters().put(EventKeys.LAST_CAPTURE_DATE, date);

        eventHandler.handleUpdateDosageStatus(event);

        verify(pillReminder).dosageStatusKnown(pillRegimenId, dosageId, date);
    }

    @Test
    public void shouldHandleUnsubscribeEvent() {

        String externalId = "123456789";

        MotechEvent event = new MotechEvent(EventKeys.PILLREMINDER_REMINDER_EVENT_SUBJECT_UNSUBSCRIBE);
        event.getParameters().put(EventKeys.EXTERNAL_ID_KEY, externalId);

        eventHandler.handleUnsubscribe(event);

        verify(pillReminder).remove(externalId);
    }
}