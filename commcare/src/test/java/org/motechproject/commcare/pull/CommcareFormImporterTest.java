package org.motechproject.commcare.pull;

import com.google.common.collect.ArrayListMultimap;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.commcare.domain.CommcareForm;
import org.motechproject.commcare.domain.CommcareFormList;
import org.motechproject.commcare.domain.CommcareMetadataJson;
import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.request.FormListRequest;
import org.motechproject.commcare.service.CommcareFormService;
import org.motechproject.commcare.service.impl.CommcareFormsEventParser;
import org.motechproject.commcare.util.CommcareParamHelper;
import org.motechproject.commons.api.Range;
import org.motechproject.commons.api.TasksEventParser;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.testing.osgi.wait.Wait;
import org.motechproject.testing.osgi.wait.WaitCondition;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommcareFormImporterTest {

    private static final DateTime START = new DateTime(2012, 10, 20, 9, 10, 45);
    private static final DateTime END = new DateTime(2013, 10, 20, 9, 10, 45);
    private static final String CONFIG_NAME = "Bihar";
    private static final int TOTAL_COUNT = 5;
    private static final String FORM = "form";
    private static final String FORM_ID = "id0";

    private static final String ATTR_KEY1 = "attr1";
    private static final String ATTR_KEY2 = "attr2";

    private static final List<DateTime> REC_DATES = Arrays.asList(
            new DateTime(2012, 11, 1, 10, 20, 33),
            new DateTime(2012, 12, 24, 1, 1, 59),
            new DateTime(2013, 2, 14, 20, 6, 12),
            new DateTime(2013, 6, 11, 20, 20, 33),
            new DateTime(2013, 10, 5, 2, 19, 33)
    );

    private CommcareFormImporter importer;

    @Mock
    private EventRelay eventRelay;

    @Mock
    private CommcareFormService formService;

    @Before
    public void setUp() {
        setUpSuccessfulImport();
        importer = new CommcareFormImporterImpl(eventRelay, formService);
        importer.setFetchSize(2);
    }

    @Test
    public void shouldImportForms() throws InterruptedException {
        importer.startImport(new Range<>(START, END), CONFIG_NAME);
        waitForImportEnd();

        ArgumentCaptor<FormListRequest> requestCaptor = ArgumentCaptor.forClass(FormListRequest.class);
        verify(formService, times(4)).retrieveFormList(requestCaptor.capture(), eq(CONFIG_NAME)); // count + 3 fetches

        verifyFormListRequest(requestCaptor.getAllValues().get(0), 1, 1);
        verifyFormListRequest(requestCaptor.getAllValues().get(1), 3, 2);
        verifyFormListRequest(requestCaptor.getAllValues().get(2), 2, 2);
        verifyFormListRequest(requestCaptor.getAllValues().get(3), 1, 2);

        ArgumentCaptor<MotechEvent> eventCaptor = ArgumentCaptor.forClass(MotechEvent.class);
        verify(eventRelay, times(5)).sendEventMessage(eventCaptor.capture());

        for (int i = 0; i < 4; i++) {
            verifyFormEvent(eventCaptor.getAllValues().get(i), "id" + i, REC_DATES.get(i));
        }

        // verify status
        FormImportStatus status = importer.importStatus();

        assertNotNull(status);
        assertNull(status.getErrorMsg());
        assertFalse(status.isError());
        assertEquals(5, status.getFormsImported());
        assertEquals(5, status.getTotalForms());
        assertEquals(CommcareParamHelper.printDateTime(REC_DATES.get(4)), status.getLastImportDate());
        assertEquals("id4", status.getLastImportFormId());
        assertFalse(status.isImportInProgress());
    }

    @Test
    public void shouldImportFormById() {
        importer.startImportById(new String(), CONFIG_NAME);

        ArgumentCaptor<String> requestCaptor = ArgumentCaptor.forClass(String.class);
        verify(formService).retrieveForm(requestCaptor.capture(), eq(CONFIG_NAME));

        ArgumentCaptor<MotechEvent> eventCaptor = ArgumentCaptor.forClass(MotechEvent.class);
        verify(eventRelay).sendEventMessage(eventCaptor.capture());

        verifyFormEvent(eventCaptor.getAllValues().get(0), FORM_ID, REC_DATES.get(0));


        // verify status
        FormImportStatus status = importer.importStatus();

        assertNotNull(status);
        assertNull(status.getErrorMsg());
        assertFalse(status.isError());
        assertEquals(1, status.getFormsImported());
        assertEquals(1, status.getTotalForms());
        assertEquals(CommcareParamHelper.printDateTime(REC_DATES.get(0)), status.getLastImportDate());
        assertEquals("id0", status.getLastImportFormId());
        assertFalse(status.isImportInProgress());
    }

    @Test
    public void shouldCountFormsForImport() {
        long count = importer.countForImport(new Range<>(START, END), CONFIG_NAME);

        assertEquals(5, count);

        ArgumentCaptor<FormListRequest> requestCaptor = ArgumentCaptor.forClass(FormListRequest.class);
        verify(formService).retrieveFormList(requestCaptor.capture(), eq(CONFIG_NAME));

        verifyFormListRequest(requestCaptor.getValue(), 1, 1);

        // verify status didn't change
        FormImportStatus status = importer.importStatus();

        assertNotNull(status);
        assertNull(status.getErrorMsg());
        assertFalse(status.isError());
        assertEquals(0, status.getFormsImported());
        assertEquals(0, status.getTotalForms());
        assertNull(status.getLastImportDate());
        assertNull(status.getLastImportFormId());
        assertFalse(status.isImportInProgress());
    }

    @Test
    public void shouldCheckFormIdForImport() {
        boolean answer = importer.checkFormIdForImport(new String(), CONFIG_NAME);

        assertTrue(answer);

        ArgumentCaptor<String> requestCaptor = ArgumentCaptor.forClass(String.class);
        verify(formService).retrieveForm(requestCaptor.capture(), eq(CONFIG_NAME));

        // verify status didn't change
        FormImportStatus status = importer.importStatus();

        assertNotNull(status);
        assertNull(status.getErrorMsg());
        assertFalse(status.isError());
        assertEquals(0, status.getFormsImported());
        assertEquals(0, status.getTotalForms());
        assertNull(status.getLastImportDate());
        assertNull(status.getLastImportFormId());
        assertFalse(status.isImportInProgress());
    }

    @Test
    public void shouldStopImportOnError() throws InterruptedException {
        // throw error on the second page
        final String errorMsg = "Failure";
        when(formService.retrieveFormList(page(2), eq(CONFIG_NAME))).thenThrow(new RuntimeException(errorMsg));

        importer.startImport(new Range<>(START, END), CONFIG_NAME);
        waitForImportEnd();

        ArgumentCaptor<FormListRequest> requestCaptor = ArgumentCaptor.forClass(FormListRequest.class);
        verify(formService, times(3)).retrieveFormList(requestCaptor.capture(), eq(CONFIG_NAME));

        verifyFormListRequest(requestCaptor.getAllValues().get(0), 1, 1);
        verifyFormListRequest(requestCaptor.getAllValues().get(1), 3, 2);
        verifyFormListRequest(requestCaptor.getAllValues().get(2), 2, 2);

        ArgumentCaptor<MotechEvent> eventCaptor = ArgumentCaptor.forClass(MotechEvent.class);
        verify(eventRelay, times(3)).sendEventMessage(eventCaptor.capture());

        verifyFormEvent(eventCaptor.getAllValues().get(0), "id0", REC_DATES.get(0));
        verifyFormFailureEvent(eventCaptor.getAllValues().get(1), errorMsg);
        verifyFormFailureStatusMessageEvent(eventCaptor.getAllValues().get(2), errorMsg);
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
        when(formService.retrieveFormList(page(3), eq(CONFIG_NAME))).thenReturn(formList(
            form("id0", REC_DATES.get(0))
        ));
        when(formService.retrieveFormList(page(2), eq(CONFIG_NAME))).thenReturn(formList(
           form("id2", REC_DATES.get(2)), form("id1", REC_DATES.get(1))
        ));
        when(formService.retrieveFormList(page(1), eq(CONFIG_NAME))).thenReturn(formList(
           form("id4", REC_DATES.get(4)), form("id3", REC_DATES.get(3))
        ));
        when(formService.retrieveForm(getFormId("id0"), eq(CONFIG_NAME))).thenReturn(form("id0", REC_DATES.get(0)));
    }

    private FormListRequest page(final int page) {
        return argThat(new ArgumentMatcher<FormListRequest>() {
            @Override
            public boolean matches(Object argument) {
                FormListRequest request = (FormListRequest) argument;
                return request != null && page == request.getPageNumber();
            }
        });
    }

    private String getFormId(final String formId){
        return argThat(new ArgumentMatcher<String>() {
            @Override
            public boolean matches(Object argument) {
                String answer = argument.toString();
                return answer != null && formId.equals(FORM_ID);
            }
        });
    }

    private CommcareFormList formList(CommcareForm... forms) {
        CommcareMetadataJson meta = new CommcareMetadataJson();
        ReflectionTestUtils.setField(meta, "totalCount", TOTAL_COUNT);

        CommcareFormList formList = new CommcareFormList();
        formList.setMeta(meta);
        formList.setObjects(asList(forms));

        return formList;
    }

    private CommcareForm form(String id, DateTime receivedOn) {
        CommcareForm form = new CommcareForm();

        form.setConfigName(CONFIG_NAME);
        form.setId(id);
        form.setReceivedOn(CommcareParamHelper.printDateTime(receivedOn));

        Map<String, String> attributes = new HashMap<>();
        attributes.put(ATTR_KEY1, ATTR_KEY1 + " " + id);
        attributes.put(ATTR_KEY2, ATTR_KEY2 + " " + id);

        FormValueElement formValueElement = new FormValueElement();
        formValueElement.setAttributes(attributes);
        formValueElement.setElementName(FORM);
        formValueElement.setSubElements(ArrayListMultimap.<String, FormValueElement>create());

        form.setForm(formValueElement);

        return form;
    }

    private void verifyFormEvent(MotechEvent event, String id, DateTime receivedOn) {
        assertNotNull(event);
        assertEquals(EventSubjects.FORMS_EVENT, event.getSubject());

        Map<String, Object> params = event.getParameters();

        assertNotNull(params);
        assertEquals(CONFIG_NAME, params.get(EventDataKeys.CONFIG_NAME));
        assertEquals(CommcareParamHelper.printDateTime(receivedOn), params.get(EventDataKeys.RECEIVED_ON));
        assertEquals(FORM, params.get(EventDataKeys.ELEMENT_NAME));
        assertEquals(CommcareFormsEventParser.PARSER_NAME, params.get(TasksEventParser.CUSTOM_PARSER_EVENT_KEY));

        Map<String, String> attributes = (Map<String, String>) params.get(EventDataKeys.ATTRIBUTES);
        assertNotNull(attributes);
        assertEquals(3, attributes.size());
        assertEquals(ATTR_KEY1 + " " + id, attributes.get(ATTR_KEY1));
        assertEquals(ATTR_KEY2 + " " + id, attributes.get(ATTR_KEY2));

        assertNotNull(params.get(EventDataKeys.SUB_ELEMENTS));
    }

    private void verifyFormFailureEvent(MotechEvent event, String msg) {
        assertNotNull(event);
        assertEquals(EventSubjects.FORMS_FAIL_EVENT, event.getSubject());
        assertEquals(CommcareFormsEventParser.PARSER_NAME, event.getParameters().get(TasksEventParser.CUSTOM_PARSER_EVENT_KEY));
        assertEquals(CONFIG_NAME, event.getParameters().get(EventDataKeys.CONFIG_NAME));
        assertEquals(msg, event.getParameters().get(EventDataKeys.FAILED_FORM_MESSAGE));
    }

    private void verifyFormFailureStatusMessageEvent(MotechEvent event, String msg) {
        assertNotNull(event);
        assertEquals("org.motechproject.message", event.getSubject());
        assertEquals("Error while importing form: " + msg, event.getParameters().get("message"));
        assertEquals("CRITICAL", event.getParameters().get("level"));
        assertEquals("commcare", event.getParameters().get("moduleName"));
    }

    private void verifyFormListRequest(FormListRequest request, Integer page, Integer pageSize) {
        assertNotNull(request);
        assertEquals(page, request.getPageNumber());
        assertEquals(pageSize, request.getPageSize());
        assertEquals(START, request.getReceivedOnStart());
        assertEquals(END, request.getReceivedOnEnd());
        assertNull(request.isIncludeArchived());
        assertNull(request.getAppVersion());
        assertNull(request.getXmlns());
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
