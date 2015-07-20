package org.motechproject.pillreminder.builder;

import org.motechproject.pillreminder.contract.MedicineRequest;
import org.motechproject.pillreminder.domain.Medicine;

/**
 * Builds medicine domain objects from medicine request.
 */
public class MedicineBuilder {

    /**
     * Builds a medicine domain object from the given
     * @param medicineRequest the request for which we should create the medicine
     * @return the newly created medicine domain object
     */
    public Medicine createFrom(MedicineRequest medicineRequest) {
        return new Medicine(medicineRequest.getName(), medicineRequest.getStartDate(), medicineRequest.getEndDate());
    }
}
