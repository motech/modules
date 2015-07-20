package org.motechproject.pillreminder.service;

import org.joda.time.LocalDate;
import org.motechproject.pillreminder.contract.DailyPillRegimenRequest;
import org.motechproject.pillreminder.contract.PillRegimenResponse;

/**
 * Pill reminder service supports creating/querying/deleting pill schedule as per prescription.
 * @see org.motechproject.pillreminder.contract.DailyPillRegimenRequest
 */
public interface PillReminderService {
    /**
     * Subscribe to a pill reminder.
     * @param dailyPillRegimenRequest the request for a daily pill regimen
     * @see org.motechproject.pillreminder.contract.DailyPillRegimenRequest
     */
    void createNew(DailyPillRegimenRequest dailyPillRegimenRequest);

    /**
     * Update the pill reminder subscription
     * @param newDailyScheduleRequest the request used for updating the subscription.
     * @see org.motechproject.pillreminder.contract.DailyPillRegimenRequest
     */
    void renew(DailyPillRegimenRequest newDailyScheduleRequest);

    /**
     * Update the dosage take status. Marking a dosage status as known, will stop the module
     * from sending repeat reminders.
     * @param pillRegimenId subscription id
     * @param dosageId  Dosage id
     * @param lastCapturedDate Dosage confirmation captured date.
     */
    void dosageStatusKnown(Long pillRegimenId, Long dosageId, LocalDate lastCapturedDate);

    /**
     * Get pill regimen for given subscriber (externalId).
     * @param externalId the external ID set from regimen
     * @return Dosage details along with reminder config
     * @see org.motechproject.pillreminder.contract.PillRegimenResponse
     */
    PillRegimenResponse getPillRegimen(String externalId);

    /**
     * Unsubscribe from pill reminder service.
     * @param externalID Unique subscriber id.
     */
    void remove(String externalID);
}
