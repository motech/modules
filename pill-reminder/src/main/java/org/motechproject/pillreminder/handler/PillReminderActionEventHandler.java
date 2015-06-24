package org.motechproject.pillreminder.handler;

import org.joda.time.LocalDate;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.pillreminder.EventKeys;
import org.motechproject.pillreminder.service.PillReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PillReminderActionEventHandler {

    private PillReminderService reminderService;

    @Autowired
    public PillReminderActionEventHandler(PillReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @MotechListener(subjects = {EventKeys.PILLREMINDER_REMINDER_EVENT_SUBJECT_DOSAGE_STATUS_KNOWN })
    public void handleUpdateDosageStatus(MotechEvent event) {
        Long externalId = (Long) event.getParameters().get(EventKeys.PILL_REGIMEN_ID);
        Long dosageId = (Long) event.getParameters().get(EventKeys.DOSAGE_ID_KEY);
        LocalDate lastCaptureDate = new LocalDate(event.getParameters().get(EventKeys.LAST_CAPTURE_DATE));
        reminderService.dosageStatusKnown(externalId, dosageId, lastCaptureDate);
    }

    @MotechListener(subjects =  {EventKeys.PILLREMINDER_REMINDER_EVENT_SUBJECT_UNSUBSCRIBE})
    public void handleUnsubscribe(MotechEvent event) {
        String externalId = (String) event.getParameters().get(EventKeys.EXTERNAL_ID_KEY);
        reminderService.remove(externalId);
    }
}
