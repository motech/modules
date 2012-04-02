package org.motechproject.adherence.service.impl;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.adherence.domain.AdherenceLog;
import org.motechproject.adherence.domain.Concept;
import org.motechproject.adherence.domain.ErrorFunction;
import org.motechproject.adherence.repository.AllAdherenceLogs;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.util.DateUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class AdherenceServiceImplTest extends BaseUnitTest {

    @Mock
    private AllAdherenceLogs allAdherenceLogs;

    private AdherenceServiceImpl adherenceService;
    private String externalId;
    private Concept concept;

    @Before
    public void setUp() {
        initMocks(this);
        adherenceService = new AdherenceServiceImpl(allAdherenceLogs);
        externalId = "externalId";
        concept = new Concept("conceptId", "tokenId");
    }

    @Test
    public void shouldStartRecordingAdherence() {
        when(allAdherenceLogs.findLatestLog(externalId, concept)).thenReturn(null);

        adherenceService.recordUnitAdherence(externalId, concept, true, new ErrorFunction(1, 1), null);
        ArgumentCaptor<AdherenceLog> logCapture = ArgumentCaptor.forClass(AdherenceLog.class);
        verify(allAdherenceLogs).insert(logCapture.capture());
        assertEquals(1, logCapture.getValue().getDosesTaken());
        assertEquals(concept, logCapture.getValue().getConcept());
        assertEquals(1, logCapture.getValue().getTotalDoses());
    }

    @Test
    public void shouldRecordUnitAdherence() {
        LocalDate today = DateUtil.today();
        AdherenceLog existingLog = AdherenceLog.create(externalId, concept, today);
        existingLog.setDosesTaken(1);
        existingLog.setTotalDoses(1);
        when(allAdherenceLogs.findLatestLog(externalId, concept)).thenReturn(existingLog);

        adherenceService.recordUnitAdherence(externalId, concept, true, new ErrorFunction(1, 1), null);
        ArgumentCaptor<AdherenceLog> logCapture = ArgumentCaptor.forClass(AdherenceLog.class);
        verify(allAdherenceLogs).insert(logCapture.capture());
        assertEquals(2, logCapture.getValue().getDosesTaken());
        assertEquals(2, logCapture.getValue().getTotalDoses());
    }

    @Test
    public void shouldRecordUnitNotTaken() {
        when(allAdherenceLogs.findLatestLog(externalId, concept)).thenReturn(null);

        adherenceService.recordUnitAdherence(externalId, concept, false, new ErrorFunction(1, 1), null);
        ArgumentCaptor<AdherenceLog> logCapture = ArgumentCaptor.forClass(AdherenceLog.class);
        verify(allAdherenceLogs).insert(logCapture.capture());
        assertEquals(0, logCapture.getValue().getDosesTaken());
        assertEquals(1, logCapture.getValue().getTotalDoses());
    }

    @Test
    public void shouldCorrectErrorWhenRecordingAdherence() {
        DateTime now = new DateTime(2011, 12, 2, 10, 0, 0, 0);
        mockCurrentDate(now);

        AdherenceLog existingLog = AdherenceLog.create(externalId, concept, now.toLocalDate());
        existingLog.setDosesTaken(1);
        existingLog.setTotalDoses(2);
        existingLog.setFromDate(now.toLocalDate().minusDays(2));
        existingLog.setToDate(now.toLocalDate().minusDays(2));

        when(allAdherenceLogs.findLatestLog(externalId, concept)).thenReturn(existingLog);

        adherenceService.recordUnitAdherence(externalId, concept, true, new ErrorFunction(0, 1), null);
        ArgumentCaptor<AdherenceLog> logCaptor = ArgumentCaptor.forClass(AdherenceLog.class);
        verify(allAdherenceLogs, times(2)).insert(logCaptor.capture());
        List<AdherenceLog> allLogs = logCaptor.getAllValues();
        assertEquals(1, allLogs.get(0).getDosesTaken());
        assertEquals(3, allLogs.get(0).getTotalDoses());
        assertEquals(2, allLogs.get(1).getDosesTaken());
        assertEquals(4, allLogs.get(1).getTotalDoses());
    }

    @Test
    public void shouldRecordMetaInformationWhenRecordingAdherence() {
        Map<String, Object> meta = new HashMap<String, Object>() {{
            put("label", "value");
        }};
        adherenceService.recordUnitAdherence(externalId, concept, true, new ErrorFunction(0, 0), meta);
        ArgumentCaptor<AdherenceLog> logCaptor = ArgumentCaptor.forClass(AdherenceLog.class);
        verify(allAdherenceLogs).insert(logCaptor.capture());
        assertEquals(meta, logCaptor.getValue().getMeta());
    }

    @Test
    public void shouldRecordMetaInformationWhenRecordingAdherenceOverDateRange() {
        LocalDate today = DateUtil.today();
        Map<String, Object> meta = new HashMap<String, Object>() {{
            put("label", "value");
        }};
        adherenceService.recordAdherence(externalId, concept, 1, 2, today.minusDays(1), today, new ErrorFunction(0, 0), meta);
        ArgumentCaptor<AdherenceLog> logCaptor = ArgumentCaptor.forClass(AdherenceLog.class);
        verify(allAdherenceLogs).insert(logCaptor.capture());
        assertEquals(meta, logCaptor.getValue().getMeta());
    }

    @Test
    public void shouldRecordAdherenceBetweenARange() {
        LocalDate fromDate = DateUtil.newDate(2011, 12, 1);
        LocalDate toDate = DateUtil.newDate(2011, 12, 31);
        when(allAdherenceLogs.findLatestLog(externalId, concept)).thenReturn(null);

        adherenceService.recordAdherence(externalId, null, 1, 1, fromDate, toDate, new ErrorFunction(0, 0), null);
        ArgumentCaptor<AdherenceLog> logCapture = ArgumentCaptor.forClass(AdherenceLog.class);
        verify(allAdherenceLogs).insert(logCapture.capture());
        assertEquals(fromDate, logCapture.getValue().getFromDate());
        assertEquals(toDate, logCapture.getValue().getToDate());
    }

    @Test
    public void shouldCorrectErrorWhenRecordingAdherenceBetweenARange() {
        DateTime now = new DateTime(2011, 12, 2, 10, 0, 0, 0);
        mockCurrentDate(now);

        AdherenceLog existingLog = AdherenceLog.create(externalId, concept, now.toLocalDate());
        existingLog.setDosesTaken(1);
        existingLog.setTotalDoses(2);
        existingLog.setFromDate(now.toLocalDate().minusDays(2));
        existingLog.setToDate(now.toLocalDate().minusDays(2));

        when(allAdherenceLogs.findLatestLog(externalId, concept)).thenReturn(existingLog);

        adherenceService.recordAdherence(externalId, concept, 1, 1, now.toLocalDate(), now.toLocalDate(), new ErrorFunction(0, 1), null);
        ArgumentCaptor<AdherenceLog> logCaptor = ArgumentCaptor.forClass(AdherenceLog.class);
        verify(allAdherenceLogs, times(2)).insert(logCaptor.capture());
        List<AdherenceLog> allLogs = logCaptor.getAllValues();
        assertEquals(1, allLogs.get(0).getDosesTaken());
        assertEquals(3, allLogs.get(0).getTotalDoses());
        assertEquals(2, allLogs.get(1).getDosesTaken());
        assertEquals(4, allLogs.get(1).getTotalDoses());
    }

    @Test
    public void shouldSaveMetaWhenCorrectingError() {
        DateTime now = new DateTime(2011, 12, 2, 10, 0, 0, 0);
        mockCurrentDate(now);

        AdherenceLog existingLog = AdherenceLog.create(externalId, concept, now.toLocalDate());
        existingLog.setDosesTaken(1);
        existingLog.setTotalDoses(2);
        existingLog.setFromDate(now.toLocalDate().minusDays(2));
        existingLog.setToDate(now.toLocalDate().minusDays(2));

        when(allAdherenceLogs.findLatestLog(externalId, concept)).thenReturn(existingLog);

        adherenceService.recordAdherence(externalId, concept, 1, 1, now.toLocalDate(), now.toLocalDate(), new ErrorFunction(0, 1), null);
        ArgumentCaptor<AdherenceLog> logCaptor = ArgumentCaptor.forClass(AdherenceLog.class);
        verify(allAdherenceLogs, times(2)).insert(logCaptor.capture());
        List<AdherenceLog> allLogs = logCaptor.getAllValues();
        assertEquals(true, allLogs.get(0).getMeta().get(AdherenceServiceImpl.ERROR_CORRECTION));
    }

    @Test
    public void shouldReportRunningAverageAdherence() {
        LocalDate today = DateUtil.today();
        AdherenceLog existingLog = AdherenceLog.create(externalId, concept, today);
        existingLog.setDosesTaken(1);
        existingLog.setTotalDoses(2);
        when(allAdherenceLogs.findLatestLog(externalId, concept)).thenReturn(existingLog);

        assertEquals(0.5, adherenceService.getRunningAverageAdherence(externalId, concept));
    }

    @Test
    public void shouldReportRunningAverageAdherenceOnGivenDate() {
        LocalDate today = DateUtil.today();
        AdherenceLog existingLog = AdherenceLog.create(externalId, concept, today);
        existingLog.setDosesTaken(1);
        existingLog.setTotalDoses(2);
        LocalDate date = DateUtil.newDate(2011, 12, 1);
        when(allAdherenceLogs.findByDate(externalId, concept, date)).thenReturn(existingLog);

        assertEquals(0.5, adherenceService.getRunningAverageAdherence(externalId, concept, date));
    }

    @Test
    public void shouldReportDeltaAdherence() {
        LocalDate today = DateUtil.today();
        AdherenceLog existingLog = AdherenceLog.create(externalId, concept, today);
        existingLog.setDosesTaken(1);
        existingLog.setTotalDoses(2);
        existingLog.setDeltaDosesTaken(1);
        existingLog.setDeltaTotalDoses(4);
        when(allAdherenceLogs.findLatestLog(externalId, concept)).thenReturn(existingLog);

        assertEquals(0.25, adherenceService.getDeltaAdherence(externalId, concept));
    }

    @Test
    public void shouldReportDeltaAdherenceOverDateRange() {
        LocalDate today = DateUtil.today();
        AdherenceLog log = AdherenceLog.create(externalId, concept, today);
        log.setDeltaDosesTaken(1);
        log.setDeltaTotalDoses(1);
        AdherenceLog secondLog = AdherenceLog.create(externalId, concept, today);
        secondLog.setDeltaDosesTaken(0);
        secondLog.setDeltaTotalDoses(1);

        LocalDate fromDate = DateUtil.newDate(2011, 12, 1);
        LocalDate toDate = DateUtil.newDate(2011, 12, 31);

        when(allAdherenceLogs.findLogsBetween(externalId, concept, fromDate, toDate)).thenReturn(Arrays.asList(log, secondLog));
        assertEquals(0.5, adherenceService.getDeltaAdherence(externalId, concept, fromDate, toDate));
    }

    @Test
    public void shouldUpdateLatestAdherenceForPositiveChangeInDeltas() {
        LocalDate today = DateUtil.today();
        AdherenceLog existingLog = AdherenceLog.create(externalId, concept, today);
        existingLog.setDosesTaken(1);
        existingLog.setTotalDoses(2);
        existingLog.setDeltaDosesTaken(1);
        existingLog.setDeltaTotalDoses(2);
        when(allAdherenceLogs.findLatestLog(externalId, concept)).thenReturn(existingLog);

        adherenceService.updateLatestAdherence(externalId, concept, 3, 4);
        ArgumentCaptor<AdherenceLog> logCaptor = ArgumentCaptor.forClass(AdherenceLog.class);
        verify(allAdherenceLogs, times(1)).update(logCaptor.capture());
        AdherenceLog allLog = logCaptor.getValue();
        assertEquals(3, allLog.getDosesTaken());
        assertEquals(4, allLog.getTotalDoses());
        assertEquals(3, allLog.getDosesTaken());
        assertEquals(4, allLog.getTotalDoses());
    }

    @Test
    public void shouldUpdateLatestAdherenceForNegativeChangeInDeltas() {
        LocalDate today = DateUtil.today();
        AdherenceLog existingLog = AdherenceLog.create(externalId, concept, today);
        existingLog.setDosesTaken(4);
        existingLog.setTotalDoses(5);
        existingLog.setDeltaDosesTaken(3);
        existingLog.setDeltaTotalDoses(4);
        when(allAdherenceLogs.findLatestLog(externalId, concept)).thenReturn(existingLog);

        adherenceService.updateLatestAdherence(externalId, concept, 2, 3);
        ArgumentCaptor<AdherenceLog> logCaptor = ArgumentCaptor.forClass(AdherenceLog.class);
        verify(allAdherenceLogs, times(1)).update(logCaptor.capture());
        AdherenceLog allLog = logCaptor.getValue();
        assertEquals(3, allLog.getDosesTaken());
        assertEquals(4, allLog.getTotalDoses());
        assertEquals(2, allLog.getDeltaDosesTaken());
        assertEquals(3, allLog.getDeltaTotalDoses());
    }

    @Test
    public void shouldFetchDateOfLatestAdherence() {
        LocalDate endDate = DateUtil.today();
        AdherenceLog adherenceLog = AdherenceLog.create(externalId, concept, endDate.minusDays(1), endDate);
        when(allAdherenceLogs.findLatestLog(externalId, concept)).thenReturn(adherenceLog);
        assertEquals(endDate, adherenceService.getLatestAdherenceDate(externalId, concept));
    }

    @Test
    public void shouldRollbackAdherence() {
        LocalDate logDate = DateUtil.newDate(2011, 1, 2);
        mockCurrentDate(DateUtil.newDateTime(logDate, 10, 0, 0));

        AdherenceLog adherenceLog = AdherenceLog.create(externalId, concept, logDate);
        adherenceLog.setId("logId");
        List<AdherenceLog> adherenceLogs = Arrays.asList(adherenceLog);
        when(allAdherenceLogs.findLogsBetween(externalId, concept, logDate, logDate)).thenReturn(adherenceLogs);
        assertEquals(adherenceLogs.get(0), adherenceService.rollBack(externalId, concept, logDate.minusDays(1)).get(0));
        verify(allAdherenceLogs).remove(adherenceLogs.get(0));
    }

    @Test
    public void shouldUpdateLogOnRollbackWhenTillDateCutsIt() {
        LocalDate logStartDate = DateUtil.newDate(2011, 1, 1);
        LocalDate logEndDate = DateUtil.newDate(2011, 1, 31);
        mockCurrentDate(DateUtil.newDateTime(logEndDate, 10, 0, 0));

        AdherenceLog adherenceLog = AdherenceLog.create(externalId, concept, logStartDate, logEndDate);
        adherenceLog.setId("logId");
        List<AdherenceLog> adherenceLogs = Arrays.asList(adherenceLog);
        when(allAdherenceLogs.findLogsBetween(externalId, concept, logStartDate.plusDays(1), logEndDate)).thenReturn(adherenceLogs);
        adherenceService.rollBack(externalId, concept, logStartDate.plusDays(1));
        verify(allAdherenceLogs, never()).remove(adherenceLogs.get(0));
    }

}
