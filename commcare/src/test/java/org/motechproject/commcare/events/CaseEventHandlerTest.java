package org.motechproject.commcare.events;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.motechproject.commcare.domain.CaseTask;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.service.CommcareCaseService;
import org.motechproject.commcare.tasks.CaseActionServiceImpl;
import org.motechproject.event.MotechEvent;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class CaseEventHandlerTest {

    @Mock
    private CommcareCaseService commcareCaseService;

    private MotechEvent createCaseEvent;
    private MotechEvent updateCaseEvent;

    private static final String CASE_ID = "123";
    private static final String CASE_TYPE = "pregnancy";
    private static final String OWNER_ID = "111";
    private static final String CASE_NAME = "Register Pregnancy";

    private CaseActionServiceImpl caseActionService;

    private CaseEventHandler eventHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        caseActionService = new CaseActionServiceImpl(commcareCaseService);
        eventHandler = new CaseEventHandler(caseActionService);

        Map<String, String> caseProperties = new HashMap<>();
        caseProperties.put("field1", "value1");
        caseProperties.put("field2", "value2");

        Map<String, Object> createCaseParams = new HashMap<>();
        createCaseParams.put(EventDataKeys.CASE_TYPE, CASE_TYPE);
        createCaseParams.put(EventDataKeys.OWNER_ID, OWNER_ID);
        createCaseParams.put(EventDataKeys.CASE_NAME, CASE_NAME);
        createCaseParams.put(EventDataKeys.FIELD_VALUES, caseProperties);

        createCaseEvent = new MotechEvent(EventSubjects.CREATE_CASE + ".config1", createCaseParams);

        Map<String, Object> updateCaseParams = new HashMap<>();
        updateCaseParams.put(EventDataKeys.CASE_ID, CASE_ID);
        updateCaseParams.put(EventDataKeys.OWNER_ID, OWNER_ID);
        updateCaseParams.put(EventDataKeys.FIELD_VALUES, caseProperties);

        updateCaseEvent = new MotechEvent(EventSubjects.UPDATE_CASE + ".config1", updateCaseParams);
    }

    @Test
    public void shouldHandleCreateCaseEvent() {
        eventHandler.createCase(createCaseEvent);

        ArgumentCaptor<CaseTask> captor = ArgumentCaptor.forClass(CaseTask.class);

        verify(commcareCaseService).uploadCase(captor.capture(), eq("config1"));
        CaseTask actual = captor.getValue();

        assertNotNull(actual.getCaseId());
        assertTrue(StringUtils.isNotBlank(actual.getCaseId()));
        assertEquals(CASE_TYPE, actual.getCreateTask().getCaseType());
        assertEquals(CASE_NAME, actual.getCreateTask().getCaseName());
        assertEquals(OWNER_ID, actual.getCreateTask().getOwnerId());
        assertEquals(2, actual.getUpdateTask().getFieldValues().size());
    }

    @Test
    public void shouldHandleUpdateCaseEventWithoutClosingCase() {
        eventHandler.updateCase(updateCaseEvent);

        ArgumentCaptor<CaseTask> captor = ArgumentCaptor.forClass(CaseTask.class);

        verify(commcareCaseService).uploadCase(captor.capture(), eq("config1"));
        CaseTask actual = captor.getValue();

        assertNotNull(actual.getCaseId());
        assertTrue(StringUtils.isNotBlank(actual.getCaseId()));
        assertEquals(CASE_ID, actual.getCaseId());
        assertEquals(OWNER_ID, actual.getUpdateTask().getOwnerId());
        assertNull(actual.getCloseTask());
        assertEquals(2, actual.getUpdateTask().getFieldValues().size());
    }

    @Test
    public void shouldHandleUpdateCaseEventWithClosingCase() {
        updateCaseEvent.getParameters().put(EventDataKeys.CLOSE_CASE, true);

        eventHandler.updateCase(updateCaseEvent);

        ArgumentCaptor<CaseTask> captor = ArgumentCaptor.forClass(CaseTask.class);

        verify(commcareCaseService).uploadCase(captor.capture(), eq("config1"));
        CaseTask actual = captor.getValue();

        assertNotNull(actual.getCaseId());
        assertTrue(StringUtils.isNotBlank(actual.getCaseId()));
        assertEquals(CASE_ID, actual.getCaseId());
        assertEquals(OWNER_ID, actual.getUpdateTask().getOwnerId());
        assertTrue(actual.getCloseTask().isClose());
        assertEquals(2, actual.getUpdateTask().getFieldValues().size());
    }

}
