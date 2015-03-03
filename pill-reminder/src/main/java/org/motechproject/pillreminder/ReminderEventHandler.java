package org.motechproject.pillreminder;

import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.pillreminder.dao.PillRegimenDataService;
import org.motechproject.pillreminder.domain.DailyScheduleDetails;
import org.motechproject.pillreminder.domain.Dosage;
import org.motechproject.pillreminder.domain.PillRegimen;
import org.motechproject.scheduler.contract.RepeatingSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class ReminderEventHandler {
    private EventRelay eventRelay;
    private PillRegimenDataService pillRegimenDataService;
    private MotechSchedulerService schedulerService;

    @Autowired
    public ReminderEventHandler(EventRelay eventRelay, PillRegimenDataService pillRegimenDataService,
                                MotechSchedulerService schedulerService) {
        this.eventRelay = eventRelay;
        this.pillRegimenDataService = pillRegimenDataService;
        this.schedulerService = schedulerService;
    }

    @MotechListener(subjects = {EventKeys.PILLREMINDER_REMINDER_EVENT_SUBJECT_SCHEDULER })
    public void handleEvent(MotechEvent motechEvent) {
        PillRegimen pillRegimen = getPillRegimen(motechEvent);
        Dosage dosage = getDosage(pillRegimen, motechEvent);

        if (!dosage.isTodaysDosageResponseCaptured()) {
            if (pillRegimen.isFirstReminderFor(dosage)) {
                scheduleRepeatReminders(motechEvent, pillRegimen, dosage);
            }
            eventRelay.sendEventMessage(createNewMotechEvent(dosage, pillRegimen, motechEvent, EventKeys.PILLREMINDER_REMINDER_EVENT_SUBJECT));
        }
    }

    private void scheduleRepeatReminders(MotechEvent motechEvent, PillRegimen pillRegimen, Dosage dosage) {
        DateTime dosageTime = dosage.todaysDosageTime();
        DailyScheduleDetails scheduleDetails = pillRegimen.getScheduleDetails();
        Date startTime = dosageTime.plusMinutes(scheduleDetails.getRepeatIntervalInMinutes()).toDate();
        Date endTime = dosageTime.plusHours(scheduleDetails.getPillWindowInHours()).plusMinutes(1).toDate();
        MotechEvent repeatingReminderEvent = createNewMotechEvent(dosage, pillRegimen, motechEvent, EventKeys.PILLREMINDER_REMINDER_EVENT_SUBJECT_SCHEDULER);

        repeatingReminderEvent.getParameters().put(MotechSchedulerService.JOB_ID_KEY, String.valueOf(dosage.getId()));
        final long millisInMinute = 60 * 1000;
        RepeatingSchedulableJob retryRemindersJob = new RepeatingSchedulableJob()
            .setMotechEvent(repeatingReminderEvent)
            .setStartTime(startTime).setEndTime(endTime)
            .setRepeatIntervalInMilliSeconds(scheduleDetails.getRepeatIntervalInMinutes() * millisInMinute)
            .setIgnorePastFiresAtStart(false);
        schedulerService.safeScheduleRepeatingJob(retryRemindersJob);
    }

    private MotechEvent createNewMotechEvent(Dosage dosage, PillRegimen pillRegimen, MotechEvent eventRaisedByScheduler, String subject) {
        MotechEvent motechEvent = new MotechEvent(subject);
        Map<String, Object> eventParams = motechEvent.getParameters();
        eventParams.putAll(eventRaisedByScheduler.getParameters());
        eventParams.put(EventKeys.PILLREMINDER_TIMES_SENT, pillRegimen.numberOfTimesPillRemindersSentFor(dosage));
        eventParams.put(EventKeys.PILLREMINDER_TOTAL_TIMES_TO_SEND, pillRegimen.timesPillRemainderWillBeSent());
        eventParams.put(EventKeys.PILLREMINDER_RETRY_INTERVAL, pillRegimen.getScheduleDetails().getRepeatIntervalInMinutes());
        return motechEvent;
    }

    private PillRegimen getPillRegimen(MotechEvent motechEvent) {
        final String pillRegimenExternalId = (String) motechEvent.getParameters().get(EventKeys.EXTERNAL_ID_KEY);
        return pillRegimenDataService.findByExternalId(pillRegimenExternalId);
    }

    private Dosage getDosage(PillRegimen pillRegimen, MotechEvent motechEvent) {
        Long dosageId;
        Object dosageIdObj = motechEvent.getParameters().get(EventKeys.DOSAGE_ID_KEY);

        if (dosageIdObj instanceof Long) {
            dosageId = (Long) dosageIdObj;
        } else if (dosageIdObj instanceof String) {
            dosageId = Long.valueOf((String) dosageIdObj);
        } else {
            throw new IllegalArgumentException("Cannot parse to a dosage ID " + dosageIdObj);
        }

        return findDosage(dosageId, pillRegimen);
    }

    private Dosage findDosage(final Long dosageId, PillRegimen pillRegimen) {
        for (Dosage dosage : pillRegimen.getDosages()) {
            if (dosage.getId().equals(dosageId)) {
                return dosage;
            }
        }
        return null;
    }
}
