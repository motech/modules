package org.motechproject.commcare.events.imports;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.service.imports.ImportFormActionService;
import org.motechproject.commcare.testutil.RequestTestUtils;
import org.motechproject.event.MotechEvent;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class ImportFormActionEventHandlerTest {

    private static final String CONFIG_NAME = "ConfigOne";

    @Mock
    private ImportFormActionService importFormActionService;

    private ImportFormActionEventHandler eventHandler;

    @Before
    public void setUp() {
        initMocks(this);

        eventHandler = new ImportFormActionEventHandler(importFormActionService);
    }

    @Test
    public void shouldCallImportFormsWithCorrectArguments() {
        MotechEvent event = prepareEvent(true);
        eventHandler.handleEvent(event);

        verify(importFormActionService).importForms(
                eq(CONFIG_NAME),
                eq(RequestTestUtils.START_DATE),
                eq(RequestTestUtils.END_DATE)
        );
    }

    @Test
    public void shouldCallImportFormsWithIncorrectArguments() {
        MotechEvent event = prepareEvent(false);
        eventHandler.handleEvent(event);

        verify(importFormActionService).importForms(
                eq(CONFIG_NAME),
                eq(RequestTestUtils.END_DATE),
                eq(RequestTestUtils.START_DATE)
        );
    }

    public MotechEvent prepareEvent(boolean correctDataRange) {
        String subject = EventSubjects.IMPORT_FORMS + '.' + CONFIG_NAME;

        Map<String, Object> params = new LinkedHashMap<>();
        if (correctDataRange) {
            params.put(EventDataKeys.START_DATE, RequestTestUtils.START_DATE);
            params.put(EventDataKeys.END_DATE, RequestTestUtils.END_DATE);
        } else {
            params.put(EventDataKeys.START_DATE, RequestTestUtils.END_DATE);
            params.put(EventDataKeys.END_DATE, RequestTestUtils.START_DATE);
        }

        return new MotechEvent(subject, params);
    }

}
