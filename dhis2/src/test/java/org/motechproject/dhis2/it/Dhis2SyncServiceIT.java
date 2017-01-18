package org.motechproject.dhis2.it;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.dhis2.domain.DataElement;
import org.motechproject.dhis2.domain.DataSet;
import org.motechproject.dhis2.domain.OrgUnit;
import org.motechproject.dhis2.domain.Program;
import org.motechproject.dhis2.domain.TrackedEntity;
import org.motechproject.dhis2.domain.TrackedEntityAttribute;
import org.motechproject.dhis2.rest.domain.DataElementDto;
import org.motechproject.dhis2.rest.domain.OrganisationUnitDto;
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

import java.util.List;

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
        List<DataElement> dataElements = dataElementService.findAll();

        assertNotNull(dataElements);
        assertEquals(1, dataElements.size());

        assertEquals("testDataElementId", dataElements.get(0).getUuid());
        assertEquals("testDataElementName", dataElements.get(0).getName());
    }

    @Test
    public void shouldAddDataSetsBySync() {
        List<DataSet> dataSets = dataSetService.findAll();

        assertNotNull(dataSets);
        assertEquals(1, dataSets.size());

        assertEquals("testDataSetId", dataSets.get(0).getUuid());
        assertEquals("testDataSetName", dataSets.get(0).getName());
    }

    @Test
    public void shouldAddTrackedEntityAttributesBySync() {
        List<TrackedEntityAttribute> trackedEntityAttributes = trackedEntityAttributeService.findAll();

        assertNotNull(trackedEntityAttributes);
        assertEquals(1, trackedEntityAttributes.size());

        assertEquals("trackedEntityAttributes1Id", trackedEntityAttributes.get(0).getUuid());
        assertEquals("trackedEntityAttributes1Name", trackedEntityAttributes.get(0).getName());
    }

    @Test
    public void shouldAddTrackedEntitiesBySync() {
        List<TrackedEntity> trackedEntities = trackedEntityService.findAll();

        assertNotNull(trackedEntities);
        assertEquals(1, trackedEntities.size());

        assertEquals("trackedEntities1Id", trackedEntities.get(0).getUuid());
        assertEquals("trackedEntities1Name", trackedEntities.get(0).getName());
    }

    @Test
    public void shouldAddProgramsBySync() {
        List<Program> programs = programService.findAll();

        assertNotNull(programs);
        assertEquals(1, programs.size());

        assertEquals("programId", programs.get(0).getUuid());
        assertEquals("programName", programs.get(0).getName());
    }

    @Test
    public void shouldAddOrganisationUnitsBySync() {
        List<OrgUnit> orgUnits = orgUnitService.findAll();

        assertNotNull(orgUnits);
        assertEquals(1, orgUnits.size());

        assertEquals("testOrganisationUnitId", orgUnits.get(0).getUuid());
        assertEquals("testOrganisationUnitName", orgUnits.get(0).getName());
    }
}
