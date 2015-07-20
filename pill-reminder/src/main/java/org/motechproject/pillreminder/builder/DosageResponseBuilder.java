package org.motechproject.pillreminder.builder;

import org.motechproject.pillreminder.contract.DosageResponse;
import org.motechproject.pillreminder.contract.MedicineResponse;
import org.motechproject.pillreminder.domain.Dosage;
import org.motechproject.pillreminder.domain.Medicine;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds dosage responses from a dosage domain object.
 */
public class DosageResponseBuilder {

    private MedicineResponseBuilder medicineResponseBuilder = new MedicineResponseBuilder();

    /**
     * Builds a dosage response from the given dosage domain object.
     * @param dosage the dosage to build the response from
     * @return the newly built dosage response
     */
    public DosageResponse createFrom(Dosage dosage) {
        List<MedicineResponse> medicines = new ArrayList<>();
        for (Medicine medicine : dosage.getMedicines()) {
            medicines.add(medicineResponseBuilder.createFrom(medicine));
        }
        return new DosageResponse(dosage.getId(), dosage.getDosageTime(), dosage.getStartDate(), dosage.getEndDate(),
                dosage.getResponseLastCapturedDate(), medicines);
    }
}
