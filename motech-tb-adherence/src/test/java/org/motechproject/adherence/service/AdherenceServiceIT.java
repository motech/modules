package org.motechproject.adherence.service;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.motechproject.adherence.common.SpringIntegrationTest;
import org.motechproject.adherence.contract.AdherenceData;
import org.motechproject.adherence.domain.AdherenceAuditLog;
import org.motechproject.adherence.repository.AllAdherenceAuditLogs;
import org.motechproject.adherence.repository.AllAdherenceLogs;
import org.motechproject.util.DateUtil;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.springframework.beans.factory.annotation.Autowired;

import static junit.framework.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.support.membermodification.MemberMatcher.method;

@PrepareForTest(DateUtil.class)
public class AdherenceServiceIT extends SpringIntegrationTest {

    @Autowired
    AllAdherenceLogs allAdherenceLogs;
    @Autowired
    AllAdherenceAuditLogs allAdherenceAuditLogs;

    @Rule
    public PowerMockRule rule = new PowerMockRule();


    AdherenceService adherenceService;

    @Before
    public void setUp() {
        initMocks(this);
        adherenceService = new AdherenceService(allAdherenceLogs, allAdherenceAuditLogs);
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

        adherenceService.saveOrUpdateAdherence("someUser", "TEST", data);
        assertEquals(1, adherenceService.adherenceRecords("externalId", "treatmentId", DateUtil.today()).size());
    }

    @Test
    public void shouldCreateAuditLogsWhileRecordingAdherence() {
        DateTime now = new DateTime(2011, 11, 29, 10, 30, 0);
        PowerMockito.stub(method(DateUtil.class, "now")).toReturn(now);

        AdherenceData data = new AdherenceData("externalId", "treatmentId", DateUtil.today());
        data = data.status(1);

        adherenceService.saveOrUpdateAdherence("someUser", "TEST", data);

        AdherenceAuditLog adherenceAuditLog = allAdherenceAuditLogs.getAll().get(0);
        assertEquals("someUser", adherenceAuditLog.user());
        assertEquals("TEST", adherenceAuditLog.source());
        assertEquals("externalId", adherenceAuditLog.externalId());
        assertEquals("treatmentId", adherenceAuditLog.treatmentId());
        assertEquals(data.doseDate(), adherenceAuditLog.doseDate());
        assertEquals(data.status(), adherenceAuditLog.status());
        assertEquals(now, adherenceAuditLog.dateModified());
    }

    @Test
    public void shouldBeIdempotentOnRecordAdherence() {
        AdherenceData data = new AdherenceData("externalId", "treatmentId", DateUtil.today());
        data = data.status(2);

        adherenceService.saveOrUpdateAdherence("someUser", "TEST", data);
        adherenceService.saveOrUpdateAdherence("someUser", "TEST", data);
        assertEquals(1, adherenceService.adherenceRecords("externalId", "treatmentId", DateUtil.today()).size());
    }

    @Test
    public void shouldFetchAdherenceRecords() {
        AdherenceData data = new AdherenceData("externalId", "treatmentId", DateUtil.today());
        data = data.status(1);

        adherenceService.saveOrUpdateAdherence("someUser", "TEST", data);
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

        adherenceService.saveOrUpdateAdherence("someUser", "TEST", patientOneOutsideLimit, patientOneWithinDateLimit, patientTwoWithinDateLimit);
        assertEquals(1, adherenceService.adherenceLogs(today, 0, 1).size());
        assertEquals(1, adherenceService.adherenceLogs(today, 1, 1).size());
    }

    @Test
    public void shouldFetchAdherenceRecordsBetweenTwoDates() {
        LocalDate today = DateUtil.today();

        AdherenceData forYesterday = new AdherenceData("externalId", "treatmentId", today.minusDays(1));
        forYesterday = forYesterday.status(1);
        AdherenceData forToday = new AdherenceData("externalId", "treatmentId", today);
        forToday = forToday.status(1);

        adherenceService.saveOrUpdateAdherence("someUser", "TEST", forYesterday, forToday);
        assertEquals(2, adherenceService.adherenceRecords("externalId", "treatmentId", today.minusDays(1), today).size());
    }
}
