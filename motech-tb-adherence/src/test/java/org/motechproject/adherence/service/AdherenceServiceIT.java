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
        request = request.status(1);

        adherenceService.recordAdherence(request);
        assertEquals(1, adherenceService.adherenceRecords("externalId", "treatmentId", DateUtil.today()).size());
    }

    @Test
    public void shouldBeIdempotentOnRecordAdherence() {
        RecordAdherenceRequest request = new RecordAdherenceRequest("externalId", "treatmentId", DateUtil.today());
        request = request.status(2);

        adherenceService.recordAdherence(request);
        adherenceService.recordAdherence(request);
        assertEquals(1, adherenceService.adherenceRecords("externalId", "treatmentId", DateUtil.today()).size());
    }

    @Test
    public void shouldFetchAdherenceRecords() {
        RecordAdherenceRequest request = new RecordAdherenceRequest("externalId", "treatmentId", DateUtil.today());
        request = request.status(1);

        adherenceService.recordAdherence(request);
        assertEquals(1, adherenceService.adherenceRecords("externalId", "treatmentId", DateUtil.today()).size());
    }

    @Test
    public void shouldFetchAllAdherenceLogsWithinGivenDate() {
        LocalDate today = DateUtil.today();
        RecordAdherenceRequest patientOneWithinDateLimit = new RecordAdherenceRequest("externalId1", "treatmentId", today);
        patientOneWithinDateLimit = patientOneWithinDateLimit.status(1);

        RecordAdherenceRequest patientOneOutsideLimit = new RecordAdherenceRequest("externalId1", "treatmentId", today.plusDays(1));
        patientOneOutsideLimit = patientOneOutsideLimit.status(1);

        RecordAdherenceRequest patientTwoWithinDateLimit = new RecordAdherenceRequest("externalId2", "treatmentId", today.minusDays(1));
        patientTwoWithinDateLimit = patientTwoWithinDateLimit.status(1);

        adherenceService.recordAdherence(patientOneOutsideLimit, patientOneWithinDateLimit, patientTwoWithinDateLimit);
        assertEquals(2, adherenceService.adherenceLogs(today).size());
    }

    @Test
    public void shouldFetchAdherenceRecordsBetweenTwoDates() {
        LocalDate today = DateUtil.today();

        RecordAdherenceRequest forYesterday = new RecordAdherenceRequest("externalId", "treatmentId", today.minusDays(1));
        forYesterday = forYesterday.status(1);
        RecordAdherenceRequest forToday = new RecordAdherenceRequest("externalId", "treatmentId", today);
        forToday = forToday.status(1);

        adherenceService.recordAdherence(forYesterday, forToday);
        assertEquals(2, adherenceService.adherenceRecords("externalId", "treatmentId", today.minusDays(1), today).size());
    }
}
