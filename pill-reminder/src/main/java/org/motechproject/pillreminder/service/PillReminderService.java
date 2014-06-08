package org.motechproject.pillreminder.service;
/**
 * \defgroup pillreminder Pill Reminder
 */
import org.joda.time.LocalDate;
import org.motechproject.pillreminder.contract.DailyPillRegimenRequest;
import org.motechproject.pillreminder.contract.PillRegimenResponse;

/**
 * \ingroup pillreminder
 * Pill reminder service supports creating/querying/deleting pill schedule as per prescription.
 * @see org.motechproject.pillreminder.contract.DailyPillRegimenRequest
 */

public interface PillReminderService {
    /**
     * Subscribe to pill reminder
     * @param dailyPillRegimenRequest
     * @see org.motechproject.pillreminder.contract.DailyPillRegimenRequest
     */
    void createNew(DailyPillRegimenRequest dailyPillRegimenRequest);

    /**
     * Update the pill reminder subscription
     * @param newDailyScheduleRequest
     * @see org.motechproject.pillreminder.contract.DailyPillRegimenRequest
     */
    void renew(DailyPillRegimenRequest newDailyScheduleRequest);

    /**
     * Update the dosage take status
     * @param pillRegimenId subscription id
     * @param dosageId  Dosage id
     * @param lastCapturedDate Dosage confirmation captured date.
     */
    void dosageStatusKnown(String pillRegimenId, String dosageId, LocalDate lastCapturedDate);

    /**
     * Get pill regimen for given subscriber (externalId)
     * @param externalId
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
