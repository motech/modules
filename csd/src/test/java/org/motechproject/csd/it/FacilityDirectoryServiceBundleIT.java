package org.motechproject.csd.it;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.csd.domain.CSD;
import org.motechproject.csd.domain.Facility;
import org.motechproject.csd.domain.FacilityDirectory;
import org.motechproject.csd.mds.FacilityDirectoryDataService;
import org.motechproject.csd.service.CSDService;
import org.motechproject.csd.service.FacilityDirectoryService;
import org.motechproject.csd.db.InitialData;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Verify FacilityDirectoryService is present & functional.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class FacilityDirectoryServiceBundleIT extends BasePaxIT {

    @Inject
    private CSDService csdService;

    @Inject
    private FacilityDirectoryService facilityDirectoryService;

    @Inject
    private FacilityDirectoryDataService facilityDirectoryDataService;

    @Before
    public void cleanBefore() {
        getLogger().info("Clean database before test");
        csdService.delete();
    }

    @After
    public void cleanAfter() {
        getLogger().info("Clean database after test");
        csdService.delete();
        facilityDirectoryDataService.deleteAll();
    }

    @Test
    public void shouldUpdateFacilityDirectoryEntity() {
        CSD csd = InitialData.getInitialData();
        csd.getProviderDirectory().getProviders().clear();
        csd.getServiceDirectory().getServices().clear();
        csd.getOrganizationDirectory().getOrganizations().clear();
        csdService.create(csd);
        assertTrue(csdService.getCSD().equals(csd));

        csd = InitialData.getInitialData();
        csd.getFacilityDirectory().getFacilities().iterator().next().setPrimaryName("updated name");
        csd.getFacilityDirectory().getFacilities().add(InitialData.createFacility(new DateTime(), "newFacility"));
        facilityDirectoryService.update(csd.getFacilityDirectory());
        assertTrue(facilityDirectoryService.getFacilityDirectory().equals(csd.getFacilityDirectory()));
    }

    @Test
    public void shouldGetEntitiesUpdatedAfterGivenDate() {
        DateTime dateUpdated = new DateTime(2020, 1, 1, 1, 1);

        Facility facility = InitialData.createFacility(dateUpdated.plusSeconds(1), "lastUpdated");

        CSD csd = InitialData.getInitialData();
        csd.getProviderDirectory().getProviders().clear();
        csd.getServiceDirectory().getServices().clear();
        csd.getOrganizationDirectory().getOrganizations().clear();
        csd.getFacilityDirectory().getFacilities().add(facility);
        csdService.create(csd);
        assertTrue(csdService.getCSD().equals(csd));

        Set<Facility> lastUpdated = facilityDirectoryService.getModifiedAfter(dateUpdated);

        assertEquals(1, lastUpdated.size());

        assertEquals(lastUpdated.iterator().next(), facility);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFacilityDirectoryShouldThrowExceptionIfThereIsMoreThanOneFacilityDirectoryEntityInTheDatabase() {
        facilityDirectoryDataService.create(new FacilityDirectory());
        facilityDirectoryDataService.create(new FacilityDirectory());

        facilityDirectoryService.getFacilityDirectory();
    }
}
