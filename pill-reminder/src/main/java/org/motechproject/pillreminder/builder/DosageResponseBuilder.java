package org.motechproject.pillreminder.builder;

import org.motechproject.pillreminder.contract.DosageResponse;
import org.motechproject.pillreminder.contract.MedicineResponse;
import org.motechproject.pillreminder.domain.Dosage;
import org.motechproject.pillreminder.domain.Medicine;

import java.util.ArrayList;
import java.util.List;

public class DosageResponseBuilder {

    private MedicineResponseBuilder medicineResponseBuilder = new MedicineResponseBuilder();

    public DosageResponse createFrom(Dosage dosage) {
        List<MedicineResponse> medicines = new ArrayList<MedicineResponse>();
        for (Medicine medicine : dosage.getMedicines()) {
            medicines.add(medicineResponseBuilder.createFrom(medicine));
        }
        return new DosageResponse(dosage.getId(), dosage.getDosageTime(), dosage.getStartDate(), dosage.getEndDate(), dosage.getResponseLastCapturedDate(), medicines);
    }
}
