package org.motechproject.dhis2.it;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.motechproject.dhis2.repository.DataElementDataService;
import org.motechproject.dhis2.repository.OrgUnitDataService;
import org.motechproject.dhis2.repository.ProgramDataService;
import org.motechproject.dhis2.repository.StageDataService;
import org.motechproject.dhis2.repository.TrackedEntityAttributeDataService;
import org.motechproject.dhis2.repository.TrackedEntityDataService;
import org.motechproject.dhis2.repository.TrackedEntityInstanceMappingDataService;

import javax.inject.Inject;

@RunWith(Suite.class)
@Suite.SuiteClasses({TasksBundleIT.class, EventHandlerBundleIT.class})
public class Dhis2IntegrationTests {

    @Inject
    private OrgUnitDataService orgUnitDataService;
    @Inject
    private DataElementDataService dataElementDataService;
    @Inject
    private ProgramDataService programDataService;
    @Inject
    private TrackedEntityAttributeDataService trackedEntityAttributeDataService;
    @Inject
    private TrackedEntityDataService trackedEntityDataService;
    @Inject
    private StageDataService stageDataService;
    @Inject
    private TrackedEntityInstanceMappingDataService trackedEntityInstanceMappingDataService;

    @Before
    public void setup() {
        clearDatabase();

    }

    @After
    public void tearDown () {
        clearDatabase();
    }


    private void clearDatabase () {
        orgUnitDataService.deleteAll();
        dataElementDataService.deleteAll();
        programDataService.deleteAll();
        trackedEntityAttributeDataService.deleteAll();
        trackedEntityDataService.deleteAll();
        stageDataService.deleteAll();
        trackedEntityInstanceMappingDataService.deleteAll();
    }



}
