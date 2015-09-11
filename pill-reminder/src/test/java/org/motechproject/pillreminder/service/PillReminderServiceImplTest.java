package org.motechproject.pillreminder.service;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.motechproject.commons.date.model.Time;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.pillreminder.builder.PillRegimenBuilder;
import org.motechproject.pillreminder.contract.DailyPillRegimenRequest;
import org.motechproject.pillreminder.contract.DosageRequest;
import org.motechproject.pillreminder.contract.MedicineRequest;
import org.motechproject.pillreminder.contract.PillRegimenResponse;
import org.motechproject.pillreminder.dao.PillRegimenDataService;
import org.motechproject.pillreminder.domain.DailyScheduleDetails;
import org.motechproject.pillreminder.domain.Dosage;
import org.motechproject.pillreminder.domain.Medicine;
import org.motechproject.pillreminder.domain.PillRegimen;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PillReminderServiceImplTest {

    PillReminderServiceImpl service;

    @Mock
    private PillRegimenDataService pillRegimenDataService;
    @Mock
    private PillRegimenJobScheduler pillRegimenJobScheduler;

    @Before
    public void setUp() {
        initMocks(this);
        service = new PillReminderServiceImpl();
        service.setPillRegimenDataService(pillRegimenDataService);
        service.setPillRegimenJobScheduler(pillRegimenJobScheduler);
    }

    @Test
    public void shouldCreateAPillRegimenFromRequestAndPersist() {
        LocalDate startDate = DateUtil.today();
        LocalDate endDate = startDate.plusDays(2);
        String externalId = "123";

        MedicineRequest medicineRequest1 = new MedicineRequest("m1", startDate, endDate);
        MedicineRequest medicineRequest2 = new MedicineRequest("m2", startDate.plusDays(1), startDate.plusDays(4));
        List<MedicineRequest> medicineRequests = asList(medicineRequest1, medicineRequest2);

        DosageRequest dosageRequest = new DosageRequest(9, 5, medicineRequests);
        DailyPillRegimenRequest dailyPillRegimenRequest = new DailyPillRegimenRequest(externalId, 5, 20, 5, asList(dosageRequest));
        when(pillRegimenDataService.create(argThat(new PillRegimenArgumentMatcher())))
                .thenReturn(new PillRegimenBuilder().createDailyPillRegimenFrom(dailyPillRegimenRequest));

        service.createNew(dailyPillRegimenRequest);

        verify(pillRegimenDataService).create(argThat(new PillRegimenArgumentMatcher()));
        verify(pillRegimenJobScheduler).scheduleDailyJob(argThat(new PillRegimenArgumentMatcher()));
    }

    @Test
    public void shouldRenewAPillRegimenFromRequest() {
        String externalId = "123";
        LocalDate startDate = DateUtil.today();
        LocalDate endDate = startDate.plusDays(2);

        MedicineRequest medicineRequest1 = new MedicineRequest("m1", startDate, endDate);
        MedicineRequest medicineRequest2 = new MedicineRequest("m2", startDate.plusDays(1), startDate.plusDays(4));
        List<MedicineRequest> medicineRequests = asList(medicineRequest1, medicineRequest2);

        DosageRequest dosageRequest = new DosageRequest(9, 5, medicineRequests);
        DailyPillRegimenRequest dailyPillRegimenRequest = new DailyPillRegimenRequest(externalId, 5, 20, 5, asList(dosageRequest));
        Set<Dosage> dosages = new HashSet<Dosage>() {{
            final Dosage dosage = new Dosage(new Time(10, 30), null);
            dosage.setId(88L);
            add(dosage);
        }};
        when(pillRegimenDataService.create(argThat(new PillRegimenArgumentMatcher())))
                .thenReturn(new PillRegimenBuilder().createDailyPillRegimenFrom(dailyPillRegimenRequest));

        PillRegimen pillRegimen = new PillRegimen(externalId, dosages, new DailyScheduleDetails(20, 2, 5));

        when(pillRegimenDataService.findByExternalId(externalId)).thenReturn(pillRegimen);

        service.renew(dailyPillRegimenRequest);

        verify(pillRegimenJobScheduler).unscheduleJobs(pillRegimen);
        verify(pillRegimenDataService).delete(pillRegimen);
        verify(pillRegimenDataService).create(argThat(new PillRegimenArgumentMatcher()));
        verify(pillRegimenJobScheduler).scheduleDailyJob(argThat(new PillRegimenArgumentMatcher()));
    }

    @Test
    public void shouldUnschedulePillReminderJobs() {
        String externalId = "123";

        Set<Dosage> dosages = new HashSet<Dosage>() {{
            final Dosage dosage = new Dosage(new Time(10, 30), null);
            dosage.setId(4L);
            add(dosage);
        }};

        PillRegimen pillRegimen = new PillRegimen(externalId, dosages, new DailyScheduleDetails(20, 2, 5));

        when(pillRegimenDataService.findByExternalId(externalId)).thenReturn(pillRegimen);

        service.remove(externalId);

        verify(pillRegimenJobScheduler).unscheduleJobs(pillRegimen);
    }

    @Test
    public void shouldCallAllPillRegimensToUpdateDosageDate() {
        final LocalDate today = DateUtil.today();
        final String externalId = "testId";
        final Long regimenId = 4L;
        final Long dosageId = 15L;

        Set<Dosage> dosages = new HashSet<Dosage>() {{
            final Dosage dosage = new Dosage(new Time(10, 30), null);
            dosage.setId(dosageId);
            add(dosage);
        }};
        PillRegimen pillRegimen = new PillRegimen(externalId, dosages, new DailyScheduleDetails(20, 2, 5));
        pillRegimen.setId(regimenId);

        when(pillRegimenDataService.findById(regimenId)).thenReturn(pillRegimen);

        service.dosageStatusKnown(regimenId, dosageId, today);

        verify(pillRegimenDataService).update(argThat(new ArgumentMatcher<PillRegimen>() {
            @Override
            public boolean matches(Object argument) {
                PillRegimen regimen = (PillRegimen) argument;
                return externalId.equals(regimen.getExternalId()) && regimenId.equals(regimen.getId()) &&
                        today.equals(regimen.getDosages().iterator().next().getResponseLastCapturedDate());
            }
        }));
    }

    @Test
    public void shouldGetPillRegimenGivenAnExternalId() {
        Long dosageId = 14L;
        Long pillRegimenId = 22L;
        String patientId = "patientId";

        Dosage dosage = new Dosage(new Time(20, 5), new HashSet<Medicine>());
        dosage.setId(dosageId);

        HashSet<Dosage> dosages = new HashSet<>();
        dosages.add(dosage);

        PillRegimen pillRegimen = new PillRegimen("patientId", dosages, new DailyScheduleDetails(15, 2, 5));
        pillRegimen.setId(pillRegimenId);
        when(pillRegimenDataService.findByExternalId(patientId)).thenReturn(pillRegimen);

        PillRegimenResponse pillRegimenResponse = service.getPillRegimen(patientId);
        assertEquals(pillRegimenId, pillRegimenResponse.getPillRegimenId());
        assertEquals(dosageId, pillRegimenResponse.getDosages().get(0).getDosageId());
    }

    private class PillRegimenArgumentMatcher extends ArgumentMatcher<PillRegimen> {
        @Override
        public boolean matches(Object o) {
            PillRegimen pillRegimen = (PillRegimen) o;
            return pillRegimen.getExternalId().equals("123") && pillRegimen.getDosages().size() == 1;
        }
    }
}
