package org.motechproject.pillreminder.domain;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.commons.date.model.Time;
import org.motechproject.commons.date.util.DateUtil;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class PillRegimenTest {

    @Test(expected = ValidationException.class)
    public void shouldNotValidateIfEndDateIsBeforeTheStartDate() {
        LocalDate startDate = DateUtil.newDate(2011, 2, 1);
        LocalDate endDate = DateUtil.newDate(2011, 1, 1);

        Set<Medicine> medicines = new HashSet<>();
        medicines.add(new Medicine("m1", startDate, endDate));

        Set<Dosage> dosages = new HashSet<>();
        dosages.add(new Dosage(new Time(9, 5), medicines));

        PillRegimen regimen = new PillRegimen("1", dosages, new DailyScheduleDetails(10, 5, 5));
        regimen.validate();
    }

    @Test
    public void shouldValidateIfNoEndDateIsProvided() {
        LocalDate startDate = DateUtil.newDate(2011, 1, 1);

        Set<Medicine> medicines = new HashSet<>();
        medicines.add(new Medicine("m1", startDate, null));

        Set<Dosage> dosages = new HashSet<>();
        dosages.add(new Dosage(new Time(9, 5), medicines));

        PillRegimen regimen = new PillRegimen("1", dosages, new DailyScheduleDetails(10, 5, 5));

        regimen.validate();
    }

    @Test
    public void shouldTestAccessors() {
        PillRegimen regimen = new PillRegimen();

        regimen.setExternalId("123");
        assertEquals("123", regimen.getExternalId());

        Set<Dosage> dosages = new HashSet<>();
        regimen.setDosages(dosages);
        assertEquals(dosages, regimen.getDosages());
    }

    @Test
    public void shouldFindDosageById() {
        Dosage dosage1 = new Dosage();
        dosage1.setId(1L);
        Dosage dosage2 = new Dosage();
        dosage2.setId(2L);

        Set<Dosage> dosages = new HashSet<>();
        dosages.add(dosage1);
        dosages.add(dosage2);

        PillRegimen regimen = new PillRegimen();
        regimen.setDosages(dosages);

        assertEquals(dosage1, regimen.getDosage(1L));
        assertEquals(dosage2, regimen.getDosage(2L));
    }
}
