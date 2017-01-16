package org.motechproject.dhis2.it;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.dhis2.domain.DataElement;
import org.motechproject.dhis2.service.DataElementService;
import org.motechproject.dhis2.service.DataSetService;
import org.motechproject.dhis2.service.OrgUnitService;
import org.motechproject.dhis2.service.ProgramService;
import org.motechproject.dhis2.service.Settings;
import org.motechproject.dhis2.service.SettingsService;
import org.motechproject.dhis2.service.StageService;
import org.motechproject.dhis2.service.SyncService;
import org.motechproject.dhis2.service.TrackedEntityAttributeService;
import org.motechproject.dhis2.service.TrackedEntityService;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;


@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class Dhis2SyncServiceIT extends Dhis2ServerBaseIT {

    @Inject
    private DataSetService dataSetService;
    @Inject
    private ProgramService programService;
    @Inject
    private TrackedEntityAttributeService trackedEntityAttributeService;
    @Inject
    private TrackedEntityService trackedEntityService;
    @Inject
    private OrgUnitService orgUnitService;
    @Inject
    private DataElementService dataElementService;
    @Inject
    private StageService stageService;
    @Inject
    private SettingsService settingsService;
    @Inject
    private SyncService syncService;

    @Before
    public void sync() {
        Settings settings = new Settings("http://localhost:9780", "name","password");

        settingsService.updateSettings(settings);
        syncService.sync();
    }

    @Test
    public void shouldAddDataElementsBySync() {
        DataElement dataElement = dataElementService.findByName("testDataElementName");

        assertNotNull(dataElement);
        assertEquals("testDataElementName", dataElement.getName());
    }
}
