package org.motechproject.pillreminder.builder;

import org.motechproject.pillreminder.contract.DosageResponse;
import org.motechproject.pillreminder.contract.PillRegimenResponse;
import org.motechproject.pillreminder.domain.DailyScheduleDetails;
import org.motechproject.pillreminder.domain.Dosage;
import org.motechproject.pillreminder.domain.PillRegimen;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds pill regimen responses from the provided pill regimen domain
 * objects.
 */
public class PillRegimenResponseBuilder {

    private DosageResponseBuilder dosageResponseBuilder = new DosageResponseBuilder();

    /**
     * Creates a pill regimen response from the provided pill regimen domain object.
     * @param pillRegimen the domain object used for constructing the pill regimen response
     * @return the response built from the pill regimen domain object
     */
    public PillRegimenResponse createFrom(PillRegimen pillRegimen) {
        List<DosageResponse> dosages = new ArrayList<>();
        for (Dosage dosage : pillRegimen.getDosages()) {
            dosages.add(dosageResponseBuilder.createFrom(dosage));
        }

        DailyScheduleDetails scheduleDetails = pillRegimen.getScheduleDetails();

        return new PillRegimenResponse(pillRegimen.getId(), pillRegimen.getExternalId(),
                scheduleDetails.getPillWindowInHours(), scheduleDetails.getRepeatIntervalInMinutes(),
                scheduleDetails.getBufferOverDosageTimeInMinutes(), dosages);
    }
}
