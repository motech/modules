package org.motechproject.pillreminder.builder;

import org.motechproject.pillreminder.contract.DailyPillRegimenRequest;
import org.motechproject.pillreminder.contract.DosageRequest;
import org.motechproject.pillreminder.domain.DailyScheduleDetails;
import org.motechproject.pillreminder.domain.Dosage;
import org.motechproject.pillreminder.domain.PillRegimen;

import java.util.HashSet;
import java.util.Set;

/**
 * Builds pill regimen domain objects from provided pill regimen request.
 */
public class PillRegimenBuilder {

    private DosageBuilder dosageBuilder = new DosageBuilder();

    /**
     * Builds a pill regimen from daily pill regimen request.
     * @param dailyPillRegimenRequest the daily pill regimen to base the pill regimen object on
     * @return the newly built pill regimen domain object
     */
    public PillRegimen createDailyPillRegimenFrom(DailyPillRegimenRequest dailyPillRegimenRequest) {
        final int pillWindowInHours = dailyPillRegimenRequest.getPillWindowInHours();
        final int reminderRepeatIntervalInMinutes = dailyPillRegimenRequest.getReminderRepeatIntervalInMinutes();
        final int bufferOverDosageTime = dailyPillRegimenRequest.getBufferOverDosageTimeInMinutes();
        final DailyScheduleDetails scheduleDetails = new DailyScheduleDetails(reminderRepeatIntervalInMinutes, pillWindowInHours, bufferOverDosageTime);
        return new PillRegimen(dailyPillRegimenRequest.getExternalId(), getDosages(dailyPillRegimenRequest), scheduleDetails);
    }

    private Set<Dosage> getDosages(DailyPillRegimenRequest pillRegimenRequest) {
        Set<Dosage> dosages = new HashSet<>();
        for (DosageRequest dosageRequest : pillRegimenRequest.getDosageRequests()) {
            dosages.add(dosageBuilder.createFrom(dosageRequest));
        }
        return dosages;
    }
}
