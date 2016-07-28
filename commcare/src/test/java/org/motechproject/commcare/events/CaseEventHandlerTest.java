package org.motechproject.commcare.events;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.tasks.CaseActionServiceImpl;
import org.motechproject.event.MotechEvent;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class CaseEventHandlerTest {

    private MotechEvent createCaseEvent;
    private MotechEvent updateCaseEvent;

    private static final String CONFIG_NAME = "config1";
    private static final String CASE_ID = "123";
    private static final String CASE_TYPE = "pregnancy";
    private static final String OWNER_ID = "111";
    private static final String CASE_NAME = "Register Pregnancy";

    private static final Class<Map<String, Object>> MAP_CLASS = (Class<Map<String, Object>>)(Class)Map.class;
    private static final ArgumentCaptor<Map<String, Object>> CAPTOR = ArgumentCaptor.forClass(MAP_CLASS);

    @Mock
    private CaseActionServiceImpl caseActionService;

    private CaseEventHandler eventHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        eventHandler = new CaseEventHandler(caseActionService);

        Map<String, String> caseProperties = new HashMap<>();
        caseProperties.put("field1", "value1");
        caseProperties.put("field2", "value2");

        Map<String, Object> createCaseParams = new HashMap<>();
        createCaseParams.put(EventDataKeys.CASE_TYPE, CASE_TYPE);
        createCaseParams.put(EventDataKeys.OWNER_ID, OWNER_ID);
        createCaseParams.put(EventDataKeys.CASE_NAME, CASE_NAME);
        createCaseParams.put(EventDataKeys.FIELD_VALUES, caseProperties);

        createCaseEvent = new MotechEvent(EventSubjects.CREATE_CASE + '.' + CONFIG_NAME, createCaseParams);

        Map<String, Object> updateCaseParams = new HashMap<>();
        updateCaseParams.put(EventDataKeys.CASE_ID, CASE_ID);
        updateCaseParams.put(EventDataKeys.OWNER_ID, OWNER_ID);
        updateCaseParams.put(EventDataKeys.FIELD_VALUES, caseProperties);

        updateCaseEvent = new MotechEvent(EventSubjects.UPDATE_CASE + '.' + CONFIG_NAME, updateCaseParams);
    }

    @Test
    public void shouldCallCreateCaseEvent() {
        eventHandler.createCase(createCaseEvent);

        verify(caseActionService).createCase(
                eq(CONFIG_NAME),
                eq(CASE_TYPE),
                eq(OWNER_ID),
                eq(CASE_NAME),
                CAPTOR.capture()
        );

        Map<String, Object> actual = CAPTOR.getValue();

        assertEquals(2, actual.size());
    }

    @Test
    public void shouldCallUpdateCaseEventWithoutClosingCase() {
        eventHandler.updateCase(updateCaseEvent);

        verify(caseActionService).updateCase(
                eq(CONFIG_NAME),
                eq(CASE_ID),
                eq(OWNER_ID),
                eq(null),
                CAPTOR.capture()
        );

        Map<String, Object> actual = CAPTOR.getValue();

        assertEquals(2, actual.size());
    }

    @Test
    public void shouldCallUpdateCaseEventWithClosingCase() {
        updateCaseEvent.getParameters().put(EventDataKeys.CLOSE_CASE, true);
        eventHandler.updateCase(updateCaseEvent);

        verify(caseActionService).updateCase(
                eq(CONFIG_NAME),
                eq(CASE_ID),
                eq(OWNER_ID),
                eq(true),
                CAPTOR.capture()
        );

        Map<String, Object> actual = CAPTOR.getValue();

        assertEquals(2, actual.size());
    }

}
