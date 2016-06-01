package org.motechproject.commcare.pull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.commcare.service.CommcareFormService;
import org.motechproject.event.listener.EventRelay;

import static org.junit.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

public class CommcareTasksFormImporterFactoryTest {

    @Mock
    private EventRelay eventRelay;

    @Mock
    private CommcareFormService formService;

    private CommcareTasksFormImporterFactory importerFactory;

    @Before
    public void setUp() {
        initMocks(this);
        importerFactory = new CommcareTasksFormImporterFactory();
        importerFactory.setEventRelay(eventRelay);
        importerFactory.setFormService(formService);
    }

    @Test
    public void shouldReturnTheSameImportersForOneEvent() {
        CommcareFormImporterImpl importer = importerFactory.getCommcareFormImporter();
        assertNotNull(importer);
    }

}
