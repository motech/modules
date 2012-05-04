package org.motechproject.adherence.service;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.adherence.common.SpringIntegrationTest;
import org.motechproject.adherence.contract.RecordAdherenceRequest;
import org.motechproject.adherence.repository.AllAdherenceLogs;
import org.motechproject.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import static junit.framework.Assert.assertEquals;

public class AdherenceServiceIT extends SpringIntegrationTest {

    @Autowired
    AllAdherenceLogs allAdherenceLogs;

    AdherenceService adherenceService;

    @Before
    public void setUp() {
        adherenceService = new AdherenceService(allAdherenceLogs);
    }

    @After
    public void tearDown() {
        markForDeletion(allAdherenceLogs.getAll().toArray());
    }

    @Test
    public void shouldRecordAdherence() {
        RecordAdherenceRequest request = new RecordAdherenceRequest("externalId", "treatmentId", DateUtil.today());
        request = request.dosesTaken(1).dosesMissed(1);

        adherenceService.recordAdherence(request);
        assertEquals(1, adherenceService.adherenceAsOf("externalId", "treatmentId", DateUtil.today()).totalDosesTaken());
    }

    @Test
    public void shouldBeIdempotentOnRecordAdherence() {
        RecordAdherenceRequest request = new RecordAdherenceRequest("externalId", "treatmentId", DateUtil.today());
        request = request.dosesTaken(2).dosesMissed(2);

        adherenceService.recordAdherence(request);
        adherenceService.recordAdherence(request);
        assertEquals(2, adherenceService.adherenceAsOf("externalId", "treatmentId", DateUtil.today()).totalDosesTaken());
    }

    @Test
    public void shouldFetchAdherenceRecords() {
        RecordAdherenceRequest request = new RecordAdherenceRequest("externalId", "treatmentId", DateUtil.today());
        request = request.dosesTaken(1).dosesMissed(1);

        adherenceService.recordAdherence(request);
        assertEquals(1, adherenceService.adherenceRecords("externalId", "treatmentId", DateUtil.today()).size());
    }

    @Test
    public void shouldFetchAdherenceRecordsBetweenTwoDates() {
        LocalDate today = DateUtil.today();

        RecordAdherenceRequest forYesterday = new RecordAdherenceRequest("externalId", "treatmentId", today.minusDays(1));
        forYesterday = forYesterday.dosesTaken(1).dosesMissed(1);
        RecordAdherenceRequest forToday = new RecordAdherenceRequest("externalId", "treatmentId", today);
        forToday = forToday.dosesTaken(1).dosesMissed(1);

        adherenceService.recordAdherence(forYesterday, forToday);
        assertEquals(2, adherenceService.adherenceRecords("externalId", "treatmentId", today.minusDays(1), today).size());
    }
}
