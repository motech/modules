package org.motechproject.pillreminder.builder;

import org.motechproject.pillreminder.contract.MedicineRequest;
import org.motechproject.pillreminder.domain.Medicine;
public class MedicineBuilder {
    public Medicine createFrom(MedicineRequest medicineRequest) {
        return new Medicine(medicineRequest.getName(), medicineRequest.getStartDate(), medicineRequest.getEndDate());
    }
}
