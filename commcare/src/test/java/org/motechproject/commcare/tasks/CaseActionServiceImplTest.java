package org.motechproject.commcare.tasks;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.motechproject.commcare.domain.CaseTask;
import org.motechproject.commcare.service.CommcareCaseService;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class CaseActionServiceImplTest {

    @Mock
    private CommcareCaseService commcareCaseService;

    private static final String CONFIG_NAME = "config1";
    private static final String CASE_ID = "123";
    private static final String CASE_TYPE = "pregnancy";
    private static final String OWNER_ID = "111";
    private static final String CASE_NAME = "Register Pregnancy";

    private CaseActionServiceImpl caseActionService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        caseActionService = new CaseActionServiceImpl(commcareCaseService);
    }

    @Test
    public void testCreateCase() {
        caseActionService.createCase(CONFIG_NAME, CASE_TYPE, OWNER_ID, CASE_NAME, getCaseProperties());

        ArgumentCaptor<CaseTask> captor = ArgumentCaptor.forClass(CaseTask.class);

        verify(commcareCaseService).uploadCase(captor.capture(), eq(CONFIG_NAME));
        CaseTask actual = captor.getValue();

        assertNotNull(actual.getCaseId());
        assertTrue(StringUtils.isNotBlank(actual.getCaseId()));
        assertEquals(CASE_TYPE, actual.getCreateTask().getCaseType());
        assertEquals(CASE_NAME, actual.getCreateTask().getCaseName());
        assertEquals(OWNER_ID, actual.getCreateTask().getOwnerId());
        assertEquals(getCaseProperties().size(), actual.getUpdateTask().getFieldValues().size());
    }

    @Test
    public void testUpdateCaseWithoutClosingCase() {
        caseActionService.updateCase(CONFIG_NAME, CASE_ID, OWNER_ID, null, getCaseProperties());

        ArgumentCaptor<CaseTask> captor = ArgumentCaptor.forClass(CaseTask.class);

        verify(commcareCaseService).uploadCase(captor.capture(), eq(CONFIG_NAME));
        CaseTask actual = captor.getValue();

        assertNotNull(actual.getCaseId());
        assertTrue(StringUtils.isNotBlank(actual.getCaseId()));
        assertEquals(CASE_ID, actual.getCaseId());
        assertEquals(OWNER_ID, actual.getUpdateTask().getOwnerId());
        assertNull(actual.getCloseTask());
        assertEquals(getCaseProperties().size(), actual.getUpdateTask().getFieldValues().size());
    }

    @Test
    public void testUpdateCaseWithClosingCase() {
        caseActionService.updateCase(CONFIG_NAME, CASE_ID, OWNER_ID, true, getCaseProperties());

        ArgumentCaptor<CaseTask> captor = ArgumentCaptor.forClass(CaseTask.class);

        verify(commcareCaseService).uploadCase(captor.capture(), eq(CONFIG_NAME));
        CaseTask actual = captor.getValue();

        assertNotNull(actual.getCaseId());
        assertTrue(StringUtils.isNotBlank(actual.getCaseId()));
        assertEquals(CASE_ID, actual.getCaseId());
        assertEquals(OWNER_ID, actual.getUpdateTask().getOwnerId());
        assertTrue(actual.getCloseTask().isClose());
        assertEquals(getCaseProperties().size(), actual.getUpdateTask().getFieldValues().size());
    }

    private static Map<String, Object> getCaseProperties() {
        Map<String, Object> caseProperties = new HashMap<>();
        caseProperties.put("field1", "value1");
        caseProperties.put("field2", "value2");

        return caseProperties;
    }

}
