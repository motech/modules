package org.motechproject.pillreminder.builder;

import org.motechproject.pillreminder.contract.MedicineResponse;
import org.motechproject.pillreminder.domain.Medicine;

public class MedicineResponseBuilder {
    public MedicineResponse createFrom(Medicine medicine) {
        return new MedicineResponse(medicine.getName(), medicine.getStartDate(), medicine.getEndDate());
    }
}
