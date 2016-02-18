package org.motechproject.pillreminder.handler;

import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.pillreminder.EventKeys;
import org.motechproject.pillreminder.dao.PillRegimenDataService;
import org.motechproject.pillreminder.domain.DailyScheduleDetails;
import org.motechproject.pillreminder.domain.Dosage;
import org.motechproject.pillreminder.domain.PillRegimen;
import org.motechproject.scheduler.contract.RepeatingSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * This is the handler responsible for handling reminder events coming from the scheduler.
 * This is the main heart of the pill reminder module, responsible for receiving the scheduler events
 * and deciding whether to send the reminder. Sending the reminder amounts to publishing a different event.
 */
@Component
public class ReminderEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReminderEventHandler.class);

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

    /**
     * Handles the event coming from the scheduled reminder job. If the dosage status is marked as captured,
     * this method will not do anything. If the dosage status is not known, repeating reminders will be scheduled (if
     * not yet scheduled) and an event signalling a reminder will be published.
     * @param motechEvent the event from the scheduler to handle
     */
    @MotechListener(subjects = {EventKeys.PILLREMINDER_REMINDER_EVENT_SUBJECT_SCHEDULER })
    public void handleEvent(MotechEvent motechEvent) {
        final String externalId = getRegimenExternalId(motechEvent);
        final Object dosageId = getDosageId(motechEvent); // dosage ID is accepted as String or Long

        PillRegimen pillRegimen = getPillRegimen(motechEvent);
        Dosage dosage = getDosage(pillRegimen, motechEvent);

        if (pillRegimen == null) {
            LOGGER.warn("Received an event for a non existing Pill Regimen with External ID {}",
                    externalId);
        } else if (dosage == null) {
            LOGGER.warn("Received an event for a non existing Dosage with ID {} for the Pill Regimen with external ID {}",
                    dosageId, externalId);
        } else if (!dosage.isTodaysDosageResponseCaptured()) {
            // a valid reminder
            logDosageDetails("Sending a reminder", externalId, dosageId);

            if (pillRegimen.isFirstReminderFor(dosage)) {
                // schedule repeating reminders
                logDosageDetails("Scheduling repeat reminders", externalId, dosageId);

                scheduleRepeatReminders(motechEvent, pillRegimen, dosage);
            }
            eventRelay.sendEventMessage(createNewMotechEvent(dosage, pillRegimen, motechEvent, EventKeys.PILLREMINDER_REMINDER_EVENT_SUBJECT));
        } else {
            logDosageDetails("Dosage status already known", externalId, dosageId);
        }
    }

    private void scheduleRepeatReminders(MotechEvent motechEvent, PillRegimen pillRegimen, Dosage dosage) {
        DateTime dosageTime = dosage.todaysDosageTime();
        DailyScheduleDetails scheduleDetails = pillRegimen.getScheduleDetails();
        DateTime startTime = dosageTime.plusMinutes(scheduleDetails.getRepeatIntervalInMinutes());
        DateTime endTime = dosageTime.plusHours(scheduleDetails.getPillWindowInHours()).plusMinutes(1);
        MotechEvent repeatingReminderEvent = createNewMotechEvent(dosage, pillRegimen, motechEvent, EventKeys.PILLREMINDER_REMINDER_EVENT_SUBJECT_SCHEDULER);

        repeatingReminderEvent.getParameters().put(MotechSchedulerService.JOB_ID_KEY, String.valueOf(dosage.getId()));
        final int secondsInMinute = 60;
        RepeatingSchedulableJob retryRemindersJob = new RepeatingSchedulableJob();
        retryRemindersJob.setMotechEvent(repeatingReminderEvent);
        retryRemindersJob.setStartDate(startTime);
        retryRemindersJob.setEndDate(endTime);
        retryRemindersJob.setRepeatIntervalInSeconds(scheduleDetails.getRepeatIntervalInMinutes() * secondsInMinute);
        retryRemindersJob.setIgnorePastFiresAtStart(false);
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
        final String pillRegimenExternalId = getRegimenExternalId(motechEvent);
        return pillRegimenDataService.findByExternalId(pillRegimenExternalId);
    }

    private Dosage getDosage(PillRegimen pillRegimen, MotechEvent motechEvent) {
        Long dosageId;
        Object dosageIdObj = getDosageId(motechEvent);

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

    private String getRegimenExternalId(MotechEvent event) {
        return (String) event.getParameters().get(EventKeys.EXTERNAL_ID_KEY);
    }

    private Object getDosageId(MotechEvent event) {
        return event.getParameters().get(EventKeys.DOSAGE_ID_KEY);
    }

    private void logDosageDetails(String message, String externalId, Object dosageId) {
        LOGGER.debug("{} for Pill Regimen with External ID {} and Dosage ID {}", message, externalId, dosageId);
    }
}
