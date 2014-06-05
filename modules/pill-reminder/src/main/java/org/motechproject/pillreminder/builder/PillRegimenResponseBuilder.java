package org.motechproject.pillreminder.builder;

import org.motechproject.pillreminder.contract.DosageResponse;
import org.motechproject.pillreminder.contract.PillRegimenResponse;
import org.motechproject.pillreminder.domain.DailyScheduleDetails;
import org.motechproject.pillreminder.domain.Dosage;
import org.motechproject.pillreminder.domain.PillRegimen;

import java.util.ArrayList;
import java.util.List;

public class PillRegimenResponseBuilder {

    private DosageResponseBuilder dosageResponseBuilder = new DosageResponseBuilder();

    public PillRegimenResponse createFrom(PillRegimen pillRegimen) {
        List<DosageResponse> dosages = new ArrayList<DosageResponse>();
        for (Dosage dosage : pillRegimen.getDosages()) {
            dosages.add(dosageResponseBuilder.createFrom(dosage));
        }
        DailyScheduleDetails scheduleDetails = pillRegimen.getScheduleDetails();
        return new PillRegimenResponse(pillRegimen.getId(), pillRegimen.getExternalId(), scheduleDetails.getPillWindowInHours(), scheduleDetails.getRepeatIntervalInMinutes(), scheduleDetails.getBufferOverDosageTimeInMinutes(), dosages);
    }
}
