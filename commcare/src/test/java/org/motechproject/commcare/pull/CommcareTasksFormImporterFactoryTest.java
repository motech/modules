package org.motechproject.commcare.pull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.service.CommcareFormService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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
        MotechEvent event = new MotechEvent(EventSubjects.IMPORT_FORMS + '.' + "CONFIG_NAME");
        CommcareFormImporterImpl firstImporter = importerFactory.getCommcareFormImporter(event);
        CommcareFormImporterImpl secondImporter = importerFactory.getCommcareFormImporter(event);
        boolean isTheSameObjects = firstImporter == secondImporter;
        assertTrue(isTheSameObjects);
    }

    @Test
    public void shouldReturnDifferentImportersForTwoDifferentEvents() {
        MotechEvent firstEvent = new MotechEvent(EventSubjects.IMPORT_FORMS + '.' + "CONFIG_NAME");
        MotechEvent secondEvent = new MotechEvent(EventSubjects.CASE_EVENT + '.' + "CONFIG_NAME");

        CommcareFormImporterImpl firstImporter = importerFactory.getCommcareFormImporter(firstEvent);
        CommcareFormImporterImpl secondImporter = importerFactory.getCommcareFormImporter(secondEvent);
        boolean isTheSameObjects = firstImporter == secondImporter;
        assertFalse(isTheSameObjects);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForNullEvent() {
        MotechEvent event = null;
        importerFactory.getCommcareFormImporter(event);
    }

}
