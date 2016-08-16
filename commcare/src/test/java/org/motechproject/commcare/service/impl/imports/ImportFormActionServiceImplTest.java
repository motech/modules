package org.motechproject.commcare.service.impl.imports;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.commcare.pull.CommcareFormImporterImpl;
import org.motechproject.commcare.pull.CommcareTasksFormImporterFactory;
import org.motechproject.commcare.service.CommcareFormService;
import org.motechproject.commcare.testutil.RequestTestUtils;
import org.motechproject.commons.api.Range;
import org.motechproject.event.listener.EventRelay;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ImportFormActionServiceImplTest {

    private static final String CONFIG_NAME = "ConfigOne";

    @Mock
    private EventRelay eventRelay;

    @Mock
    private CommcareFormService formService;

    @Mock
    private CommcareTasksFormImporterFactory importerFactory;

    private ImportFormActionServiceImpl importFormActionService;

    @Mock
    private CommcareFormImporterImpl importer = new CommcareFormImporterImpl(eventRelay, formService);

    @Before
    public void setUp() {
        initMocks(this);

        importFormActionService = new ImportFormActionServiceImpl(importerFactory);
    }

    @Test
    public void shouldCallImporterMethodsWithCorrectArguments() {
        Range<DateTime> range = new Range<>(RequestTestUtils.START_DATE, RequestTestUtils.END_DATE);

        when(importerFactory.getCommcareFormImporter()).thenReturn(importer);

        when(importer.countForImport(range, CONFIG_NAME)).thenReturn(0);
        doNothing().when(importer).startImport(range, CONFIG_NAME);


        importFormActionService.importForms(CONFIG_NAME, RequestTestUtils.START_DATE, RequestTestUtils.END_DATE);

        verify(importer).countForImport(eq(range), eq(CONFIG_NAME));
        verify(importer).startImport(eq(range), eq(CONFIG_NAME));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotImportFormsWithIncorrectDateRange() {
        when(importerFactory.getCommcareFormImporter()).thenReturn(new CommcareFormImporterImpl(eventRelay, formService));
        importFormActionService.importForms(CONFIG_NAME, RequestTestUtils.END_DATE, RequestTestUtils.START_DATE);
    }

}
