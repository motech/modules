package org.motechproject.dhis2.it;

import org.junit.After;
import org.junit.Before;
import org.motechproject.dhis2.repository.DataElementDataService;
import org.motechproject.dhis2.repository.OrgUnitDataService;
import org.motechproject.dhis2.repository.ProgramDataService;
import org.motechproject.dhis2.repository.StageDataService;
import org.motechproject.dhis2.repository.TrackedEntityAttributeDataService;
import org.motechproject.dhis2.repository.TrackedEntityDataService;
import org.motechproject.dhis2.repository.TrackedEntityInstanceMappingDataService;
import org.motechproject.testing.osgi.BasePaxIT;

import javax.inject.Inject;

public abstract class BaseDhisIT extends BasePaxIT {

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
    public void baseSetUp() {
        clearDatabase();
    }

    @After
    public void baseTearDown () {
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
