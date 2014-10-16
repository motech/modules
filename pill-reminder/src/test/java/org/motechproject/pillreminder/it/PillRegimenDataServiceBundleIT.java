package org.motechproject.pillreminder.it;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.commons.date.model.Time;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.pillreminder.dao.PillRegimenDataService;
import org.motechproject.pillreminder.domain.DailyScheduleDetails;
import org.motechproject.pillreminder.domain.Dosage;
import org.motechproject.pillreminder.domain.Medicine;
import org.motechproject.pillreminder.domain.PillRegimen;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class PillRegimenDataServiceBundleIT extends BasePaxIT {

    @Inject
    private PillRegimenDataService pillRegimenDataService;

    private LocalDate startDate;
    private LocalDate endDate;

    @Before
    public void setUp() {
        startDate = DateUtil.newDate(2011, 1, 1);
        endDate = DateUtil.newDate(2011, 3, 1);
    }

    @Test
    public void shouldSaveThePillRegimenWithoutDosage() {
        PillRegimen pillRegimen = new PillRegimen("1234", null, new DailyScheduleDetails(20, 5, 5));

        pillRegimenDataService.create(pillRegimen);

        assertNotNull(pillRegimen.getId());
        pillRegimenDataService.delete(pillRegimen);
    }

    @Test
    public void shouldSaveThePillRegimenWithDosages() {
        PillRegimen pillRegimen = setUpPillRegimen();
        pillRegimenDataService.create(pillRegimen);

        final Long regimenId = pillRegimen.getId();
        assertNotNull(regimenId);

        PillRegimen pillRegimenFromDB = pillRegimenDataService.findById(regimenId);
        DailyScheduleDetails scheduleDetailsFromDB = pillRegimenFromDB.getScheduleDetails();
        assertEquals(5, scheduleDetailsFromDB.getPillWindowInHours());
        assertEquals(20, scheduleDetailsFromDB.getRepeatIntervalInMinutes());

        Object[] dosagesFromDB = pillRegimenFromDB.getDosages().toArray();
        assertEquals(1, dosagesFromDB.length);

        Set<Medicine> medicinesFromDB = ((Dosage) dosagesFromDB[0]).getMedicines();
        assertEquals(2, medicinesFromDB.toArray().length);

        pillRegimenDataService.delete(pillRegimen);
        assertNull(pillRegimenDataService.findById(regimenId));
    }

    @Test
    public void shouldGetPillRegimenByExternalId() {
        PillRegimen pillRegimen = new PillRegimen("1234", null, new DailyScheduleDetails(20, 5, 5));
        pillRegimenDataService.create(pillRegimen);
        PillRegimen returnedRegimen = pillRegimenDataService.findByExternalId("1234");
        assertNotNull(returnedRegimen);
        assertEquals(returnedRegimen.getExternalId(), "1234");
    }

    private PillRegimen setUpPillRegimen() {
        Medicine medicine = new Medicine("m1", startDate, endDate);
        Medicine medicine2 = new Medicine("m2", startDate, startDate.plusMonths(3));
        Set<Medicine> medicines = new HashSet<>();
        medicines.add(medicine);
        medicines.add(medicine2);

        Dosage dosage = new Dosage(new Time(9, 5), medicines);
        Set<Dosage> dosages = new HashSet<>();
        dosages.add(dosage);
        return new PillRegimen("1234", dosages, new DailyScheduleDetails(20, 5, 5));
    }
}
