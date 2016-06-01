package org.motechproject.commcare.events.imports;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.pull.CommcareFormImporterImpl;
import org.motechproject.commcare.pull.CommcareTasksFormImporterFactory;
import org.motechproject.commcare.service.CommcareFormService;
import org.motechproject.commons.api.Range;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ImportFormActionEventHandlerTest {

    private static final List<DateTime> DATES = Arrays.asList(
            new DateTime(2012, 11, 1, 10, 20, 33),
            new DateTime(2012, 12, 24, 1, 1, 59)
    );

    private static final String CONFIG_NAME = "ConfigOne";

    @Mock
    private EventRelay eventRelay;

    @Mock
    private CommcareFormService formService;

    @Mock
    private CommcareTasksFormImporterFactory importerFactory;

    private ImportFormActionEventHandler eventHandler;

    @Mock
    private CommcareFormImporterImpl importer = new CommcareFormImporterImpl(eventRelay, formService);

    @Before
    public void setUp() {
        initMocks(this);
        eventHandler = new ImportFormActionEventHandler();
        eventHandler.setImporterFactory(importerFactory);
    }

    @Test
    public void shouldCallImporterMethodsWithCorrectArguments() {
        Range<DateTime> range = new Range<>(DATES.get(0), DATES.get(1));
        MotechEvent event = prepareEvent(true);

        when(importerFactory.getCommcareFormImporter(event)).thenReturn(importer);

        when(importer.countForImport(range, CONFIG_NAME)).thenReturn(0);
        doNothing().when(importer).startImport(range, CONFIG_NAME);


        eventHandler.handleEvent(event);

        verify(importer).countForImport(eq(range), eq(CONFIG_NAME));
        verify(importer).startImport(eq(range), eq(CONFIG_NAME));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotImportFormsWithIncorrectDateRange() {
        MotechEvent event = prepareEvent(false);
        when(importerFactory.getCommcareFormImporter(event)).thenReturn(new CommcareFormImporterImpl(eventRelay, formService));
        eventHandler.handleEvent(event);
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
