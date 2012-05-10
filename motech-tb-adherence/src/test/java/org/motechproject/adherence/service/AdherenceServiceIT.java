package org.motechproject.adherence.service;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.adherence.common.SpringIntegrationTest;
import org.motechproject.adherence.contract.AdherenceData;
import org.motechproject.adherence.repository.AllAdherenceAuditLogs;
import org.motechproject.adherence.repository.AllAdherenceLogs;
import org.motechproject.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import static junit.framework.Assert.assertEquals;

public class AdherenceServiceIT extends SpringIntegrationTest {

    @Autowired
    AllAdherenceLogs allAdherenceLogs;
    @Autowired
    AllAdherenceAuditLogs allAdherenceAuditLogs;

    AdherenceService adherenceService;

    @Before
    public void setUp() {
        adherenceService = new AdherenceService(allAdherenceLogs);
    }

    @After
    public void tearDown() {
        markForDeletion(allAdherenceLogs.getAll().toArray());
        markForDeletion(allAdherenceAuditLogs.getAll().toArray());
    }

    @Test
    public void shouldRecordAdherence() {
        AdherenceData data = new AdherenceData("externalId", "treatmentId", DateUtil.today());
        data = data.status(1);

        adherenceService.recordAdherence("someUser", "TEST", data);
        assertEquals(1, adherenceService.adherenceRecords("externalId", "treatmentId", DateUtil.today()).size());
    }

    @Test
    public void shouldBeIdempotentOnRecordAdherence() {
        AdherenceData data = new AdherenceData("externalId", "treatmentId", DateUtil.today());
        data = data.status(2);

        adherenceService.recordAdherence("someUser", "TEST", data);
        adherenceService.recordAdherence("someUser", "TEST", data);
        assertEquals(1, adherenceService.adherenceRecords("externalId", "treatmentId", DateUtil.today()).size());
    }

    @Test
    public void shouldFetchAdherenceRecords() {
        AdherenceData data = new AdherenceData("externalId", "treatmentId", DateUtil.today());
        data = data.status(1);

        adherenceService.recordAdherence("someUser", "TEST", data);
        assertEquals(1, adherenceService.adherenceRecords("externalId", "treatmentId", DateUtil.today()).size());
    }

    @Test
    public void shouldFetchAllAdherenceLogsWithinGivenDate() {
        LocalDate today = DateUtil.today();
        AdherenceData patientOneWithinDateLimit = new AdherenceData("externalId1", "treatmentId", today);
        patientOneWithinDateLimit = patientOneWithinDateLimit.status(1);

        AdherenceData patientOneOutsideLimit = new AdherenceData("externalId1", "treatmentId", today.plusDays(1));
        patientOneOutsideLimit = patientOneOutsideLimit.status(1);

        AdherenceData patientTwoWithinDateLimit = new AdherenceData("externalId2", "treatmentId", today.minusDays(1));
        patientTwoWithinDateLimit = patientTwoWithinDateLimit.status(1);

        adherenceService.recordAdherence("someUser", "TEST", patientOneOutsideLimit, patientOneWithinDateLimit, patientTwoWithinDateLimit);
        assertEquals(2, adherenceService.adherenceLogs(today).size());
    }

    @Test
    public void shouldFetchAdherenceRecordsBetweenTwoDates() {
        LocalDate today = DateUtil.today();

        AdherenceData forYesterday = new AdherenceData("externalId", "treatmentId", today.minusDays(1));
        forYesterday = forYesterday.status(1);
        AdherenceData forToday = new AdherenceData("externalId", "treatmentId", today);
        forToday = forToday.status(1);

        adherenceService.recordAdherence("someUser", "TEST", forYesterday, forToday);
        assertEquals(2, adherenceService.adherenceRecords("externalId", "treatmentId", today.minusDays(1), today).size());
    }
}
