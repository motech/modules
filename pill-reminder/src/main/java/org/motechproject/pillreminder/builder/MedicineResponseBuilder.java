package org.motechproject.pillreminder.builder;

import org.motechproject.pillreminder.contract.MedicineResponse;
import org.motechproject.pillreminder.domain.Medicine;

/**
 * Builds medicine responses from provided medicine domain objects.
 */
public class MedicineResponseBuilder {

    /**
     * Builds a medicine response from the provided medicine domain object.
     * @param medicine the medicine domain object to build the response from
     * @return the medicine response built from the provided medicine object
     */
    public MedicineResponse createFrom(Medicine medicine) {
        return new MedicineResponse(medicine.getName(), medicine.getStartDate(), medicine.getEndDate());
    }
}
