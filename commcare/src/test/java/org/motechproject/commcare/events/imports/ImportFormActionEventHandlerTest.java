package org.motechproject.commcare.events.imports;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.tasks.imports.ImportFormActionServiceImpl;
import org.motechproject.event.MotechEvent;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class ImportFormActionEventHandlerTest {

    private static final List<DateTime> DATES = Arrays.asList(
            new DateTime(2012, 11, 1, 10, 20, 33),
            new DateTime(2012, 12, 24, 1, 1, 59)
    );

    private static final String CONFIG_NAME = "ConfigOne";

    @Mock
    private ImportFormActionServiceImpl importFormActionService;

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
                eq(DATES.get(0)),
                eq(DATES.get(1))
        );
    }

    @Test
    public void shouldCallImportFormsWithIncorrectArguments() {
        MotechEvent event = prepareEvent(false);
        eventHandler.handleEvent(event);

        verify(importFormActionService).importForms(
                eq(CONFIG_NAME),
                eq(DATES.get(1)),
                eq(DATES.get(0))
        );
    }

    public MotechEvent prepareEvent(boolean correctDataRange) {
        String subject = EventSubjects.IMPORT_FORMS + '.' + CONFIG_NAME;

        Map<String, Object> params = new LinkedHashMap<>();
        if (correctDataRange) {
            params.put(EventDataKeys.START_DATE, DATES.get(0));
            params.put(EventDataKeys.END_DATE, DATES.get(1));
        } else {
            params.put(EventDataKeys.START_DATE, DATES.get(1));
            params.put(EventDataKeys.END_DATE, DATES.get(0));
        }

        return new MotechEvent(subject, params);
    }

}
