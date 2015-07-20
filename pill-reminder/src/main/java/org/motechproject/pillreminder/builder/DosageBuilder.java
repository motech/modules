package org.motechproject.pillreminder.builder;

import org.motechproject.commons.date.model.Time;
import org.motechproject.pillreminder.contract.DosageRequest;
import org.motechproject.pillreminder.contract.MedicineRequest;
import org.motechproject.pillreminder.domain.Dosage;
import org.motechproject.pillreminder.domain.Medicine;

import java.util.HashSet;
import java.util.Set;

/**
 * Builds dosage domain objects from dosage requests.
 */
public class DosageBuilder {

    /**
     * Builds a dosage object from the request.
     * @param dosageRequest the request to base the domain object on
     * @return the newly built dosage domain object
     */
    public Dosage createFrom(DosageRequest dosageRequest) {
        Set<Medicine> medicines = new HashSet<>();
        Time dosageTime = new Time(dosageRequest.getStartHour(), dosageRequest.getStartMinute());
        for (MedicineRequest medicineRequest : dosageRequest.getMedicineRequests()) {
            medicines.add(new MedicineBuilder().createFrom(medicineRequest));
        }
        return new Dosage(dosageTime, medicines);
    }
}
