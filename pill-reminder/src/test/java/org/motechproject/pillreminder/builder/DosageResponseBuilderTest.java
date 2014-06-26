package org.motechproject.pillreminder.builder;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.commons.date.model.Time;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.pillreminder.contract.DosageResponse;
import org.motechproject.pillreminder.contract.MedicineResponse;
import org.motechproject.pillreminder.domain.Dosage;
import org.motechproject.pillreminder.domain.Medicine;

import java.util.HashSet;
import java.util.List;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;

public class DosageResponseBuilderTest {

    @Test
    public void shouldConstructADosageResponseGivenADosage() {
        LocalDate date = DateUtil.today();
        HashSet<Medicine> medicines = new HashSet<Medicine>();
        medicines.add(new Medicine("medicine1", date, date));
        medicines.add(new Medicine("medicine2", date, date));
        Dosage dosage = new Dosage(new Time(10, 5), medicines);
        dosage.setId(14L);

        DosageResponse dosageResponse = new DosageResponseBuilder().createFrom(dosage);

        assertEquals(Long.valueOf(14), dosageResponse.getDosageId());
        assertEquals(10, dosageResponse.getDosageHour());
        assertEquals(5, dosageResponse.getDosageMinute());

        List<MedicineResponse> dosageResponseMedicines = dosageResponse.getMedicines();
        assertEquals(asList("medicine1", "medicine2"), extract(dosageResponseMedicines, on(MedicineResponse.class).getName()));
    }
}
