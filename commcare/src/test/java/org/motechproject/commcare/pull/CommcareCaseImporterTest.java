package org.motechproject.commcare.pull;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.commcare.domain.CaseInfo;
import org.motechproject.commcare.domain.CasesInfo;
import org.motechproject.commcare.domain.CommcareMetadataInfo;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.service.CommcareCaseService;
import org.motechproject.commcare.service.impl.CommcareCaseEventParser;
import org.motechproject.commons.api.Range;
import org.motechproject.commons.api.TasksEventParser;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.testing.osgi.wait.Wait;
import org.motechproject.testing.osgi.wait.WaitCondition;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommcareCaseImporterTest {
    private static final DateTime START = new DateTime(2013, 1, 15, 10, 25, 32);
    private static final DateTime END = new DateTime(2015, 8, 15, 10, 25, 32);
    private static final String CONFIG_NAME = "Bihar";
    private static final int TOTAL_COUNT = 5;
    private static final int FETCH_SIZE = 2;
    private static final int TOTAL_PAGES = 3;
    private static final String CASE = "case";
    private static final String CASE_ID = "id0";

    private static final String ATTR_KEY1 = "attr1";
    private static final String ATTR_KEY2 = "attr2";

    private static final List<String> REC_DATES = Arrays.asList(
            dateTimeToString(new DateTime(2013, 2, 1, 10, 20, 33)),
            dateTimeToString(new DateTime(2013, 10, 24, 1, 1, 59)),
            dateTimeToString(new DateTime(2014, 6, 11, 20, 20, 33)),
            dateTimeToString(new DateTime(2014, 11, 14, 20, 6, 12)),
            dateTimeToString(new DateTime(2015, 8, 5, 2, 19, 33))
    );

    private CommcareCaseImporter importer;

    @Mock
    private EventRelay eventRelay;

    @Mock
    private CommcareCaseService caseService;

    @Before
    public void setup() {
        setUpSuccessfulImport();
        importer = new CommcareCaseImporterImpl(eventRelay, caseService);
        importer.setFetchSize(2);
    }

    @Test
    public void shouldImportCases() throws InterruptedException {
        importer.startImport(new Range<>(START, END), CONFIG_NAME);
        waitForImportEnd();

        ArgumentCaptor<Integer> pageCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(caseService, times(4)).getCasesByCasesTimeWithMetadata(eq(dateTimeToString(START)), eq(dateTimeToString(END)),
                anyInt(), pageCaptor.capture(), eq(CONFIG_NAME)); // count + 3 fetches

        assertEquals(pageCaptor.getAllValues().get(0), (Integer)1);
        assertEquals(pageCaptor.getAllValues().get(1), (Integer)1);
        assertEquals(pageCaptor.getAllValues().get(2), (Integer)2);
        assertEquals(pageCaptor.getAllValues().get(3), (Integer)3);
        ArgumentCaptor<MotechEvent> eventCaptor = ArgumentCaptor.forClass(MotechEvent.class);
        verify(eventRelay, times(5)).sendEventMessage(eventCaptor.capture());

        for (int i = 0; i < 5; i++) {
            verifyCaseEvent(eventCaptor.getAllValues().get(i), "id" + i, REC_DATES.get(i));
        }

        // verify status
        CaseImportStatus status = importer.importStatus();

        assertNotNull(status);
        assertNull(status.getErrorMsg());
        assertFalse(status.isError());
        assertEquals(5, status.getCasesImported());
        assertEquals(5, status.getTotalCases());
        assertEquals(REC_DATES.get(4), status.getLastImportDate());
        assertEquals("id4", status.getLastImportCaseId());
        assertFalse(status.isImportInProgress());
    }

    @Test
    public void shouldImportCaseById() {
        importer.importSingleCase(CASE_ID, CONFIG_NAME);

        verify(caseService).getCaseByCaseId(eq(CASE_ID), eq(CONFIG_NAME));

        ArgumentCaptor<MotechEvent> eventCaptor = ArgumentCaptor.forClass(MotechEvent.class);
        verify(eventRelay).sendEventMessage(eventCaptor.capture());

        verifyCaseEvent(eventCaptor.getAllValues().get(0), CASE_ID, REC_DATES.get(0));


        // verify status
        CaseImportStatus status = importer.importStatus();

        assertNotNull(status);
        assertNull(status.getErrorMsg());
        assertFalse(status.isError());
        assertEquals(1, status.getCasesImported());
        assertEquals(REC_DATES.get(0), status.getLastImportDate());
        assertEquals("id0", status.getLastImportCaseId());
        assertFalse(status.isImportInProgress());
    }

    @Test
    public void shouldCountCasesForImport() {
        long count = importer.countForImport(new Range<>(START, END), CONFIG_NAME);

        assertEquals(5, count);

        ArgumentCaptor<Integer> pageCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(caseService).getCasesByCasesTimeWithMetadata(eq(dateTimeToString(START)), eq(dateTimeToString(END)),
                anyInt(), pageCaptor.capture(), eq(CONFIG_NAME));

        // verify status didn't change
        CaseImportStatus status = importer.importStatus();

        assertNotNull(status);
        assertNull(status.getErrorMsg());
        assertFalse(status.isError());
        assertEquals(0, status.getCasesImported());
        assertEquals(0, status.getTotalCases());
        assertNull(status.getLastImportDate());
        assertNull(status.getLastImportCaseId());
        assertFalse(status.isImportInProgress());
    }

    @Test
    public void shouldStopImportOnError() throws InterruptedException {
        // throw error on the second page
        final String errorMsg = "Failure";
        when(caseService.getCasesByCasesTimeWithMetadata(eq(dateTimeToString(START)), eq(dateTimeToString(END)),
                eq(FETCH_SIZE), getPageNumber(2), eq(CONFIG_NAME))).thenThrow(new RuntimeException(errorMsg));

        importer.startImport(new Range<>(START, END), CONFIG_NAME);
        waitForImportEnd();

        ArgumentCaptor<Integer> pageCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(caseService, times(3)).getCasesByCasesTimeWithMetadata(eq(dateTimeToString(START)), eq(dateTimeToString(END)),
                anyInt(), pageCaptor.capture(), eq(CONFIG_NAME));

        assertEquals(pageCaptor.getAllValues().get(0), (Integer)1);
        assertEquals(pageCaptor.getAllValues().get(1), (Integer)1);
        assertEquals(pageCaptor.getAllValues().get(2), (Integer)2);

        ArgumentCaptor<MotechEvent> eventCaptor = ArgumentCaptor.forClass(MotechEvent.class);
        verify(eventRelay, times(4)).sendEventMessage(eventCaptor.capture());

        verifyCaseEvent(eventCaptor.getAllValues().get(0), "id0", REC_DATES.get(0));
        verifyCaseFailureEvent(eventCaptor.getAllValues().get(2), errorMsg);
        verifyCaseFailureStatusMessageEvent(eventCaptor.getAllValues().get(3), errorMsg);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectNullDateRangeWhenStartingImport() {
        importer.startImport(null, CONFIG_NAME);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectNullDateRangeWhenCountingForImport() {
        importer.countForImport(null, CONFIG_NAME);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectInvalidDateRangeWhenStartingImport() {
        importer.startImport(new Range<>(END, START), CONFIG_NAME);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectInvalidDateRangeWhenCountingForImport() {
        importer.countForImport(new Range<>(END, START), CONFIG_NAME);
    }

    private void setUpSuccessfulImport() {
        when(caseService.getCasesByCasesTimeWithMetadata(eq(dateTimeToString(START)), eq(dateTimeToString(END)),
                eq(FETCH_SIZE), getPageNumber(3), eq(CONFIG_NAME))).thenReturn(caseList(
                caseInfo("id4", REC_DATES.get(4))
        ));
        when(caseService.getCasesByCasesTimeWithMetadata(eq(dateTimeToString(START)), eq(dateTimeToString(END)),
                eq(FETCH_SIZE), getPageNumber(2), eq(CONFIG_NAME))).thenReturn(caseList(
                caseInfo("id2", REC_DATES.get(2)), caseInfo("id3", REC_DATES.get(3))
        ));
        when(caseService.getCasesByCasesTimeWithMetadata(eq(dateTimeToString(START)), eq(dateTimeToString(END)),
                eq(FETCH_SIZE), getPageNumber(1), eq(CONFIG_NAME))).thenReturn(caseList(
                caseInfo("id0", REC_DATES.get(0)), caseInfo("id1", REC_DATES.get(1))
        ));
        when(caseService.getCasesByCasesTimeWithMetadata(eq(dateTimeToString(START)), eq(dateTimeToString(END)), eq(1),
                getPageNumber(1), eq(CONFIG_NAME))).thenReturn(caseList(
                caseInfo("id0", REC_DATES.get(0))
        ));
        when(caseService.getCaseByCaseId(eq("id0"), eq(CONFIG_NAME))).thenReturn(caseInfo("id0", REC_DATES.get(0)));
    }

    private static String dateTimeToString(DateTime dateTime) {
        if (dateTime == null) {
            return null;
        } else {
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            return formatter.print(dateTime);
        }
    }

    private CasesInfo caseList(CaseInfo... cases) {
        CommcareMetadataInfo meta = new CommcareMetadataInfo();
        ReflectionTestUtils.setField(meta, "totalCount", TOTAL_COUNT);

        CasesInfo caseList = new CasesInfo(asList(cases), meta);

        return caseList;
    }

    private CaseInfo caseInfo(String id, String date) {
        CaseInfo caseInfo = new CaseInfo();

        caseInfo.setCaseId(id);
        caseInfo.setConfigName(CONFIG_NAME);
        caseInfo.setDateModified(date);
        caseInfo.setCaseType(CASE);

        return caseInfo;
    }

    private Integer getPageNumber(final int pageNumber) {
        return argThat(new ArgumentMatcher<Integer>() {
            @Override
            public boolean matches(Object o) {
                Integer pageNum = (Integer) o;
                return pageNum == pageNumber;
            }
        });
    }

    private void verifyCaseEvent(MotechEvent event, String id, String dateModified) {
        assertNotNull(event);
        assertEquals(EventSubjects.CASE_EVENT, event.getSubject());

        Map<String, Object> params = event.getParameters();

        assertNotNull(params);
        assertEquals(CONFIG_NAME, params.get(EventDataKeys.CONFIG_NAME));
        assertEquals(id, params.get(EventDataKeys.CASE_ID));
        assertEquals(dateModified, params.get(EventDataKeys.DATE_MODIFIED));
        assertEquals(CASE, params.get(EventDataKeys.CASE_TYPE));
    }

    private void verifyCaseFailureEvent(MotechEvent event, String msg) {
        assertNotNull(event);
        assertEquals(EventSubjects.CASES_FAIL_EVENT, event.getSubject());
        assertEquals(CommcareCaseEventParser.PARSER_NAME, event.getParameters().get(TasksEventParser.CUSTOM_PARSER_EVENT_KEY));
        assertEquals(CONFIG_NAME, event.getParameters().get(EventDataKeys.CONFIG_NAME));
        assertEquals(msg, event.getParameters().get(EventDataKeys.FAILED_CASE_MESSAGE));
    }

    private void verifyCaseFailureStatusMessageEvent(MotechEvent event, String msg) {
        assertNotNull(event);
        assertEquals("org.motechproject.message", event.getSubject());
        assertEquals("Error while importing case: " + msg, event.getParameters().get("message"));
        assertEquals("CRITICAL", event.getParameters().get("level"));
        assertEquals("commcare", event.getParameters().get("moduleName"));
    }

    private void waitForImportEnd() throws InterruptedException {
        new Wait(new WaitCondition() {
            @Override
            public boolean needsToWait() {
                return importer.isImportInProgress();
            }
        }, 5000).start();
    }
}
