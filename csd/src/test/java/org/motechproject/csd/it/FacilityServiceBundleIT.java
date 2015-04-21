package org.motechproject.csd.it;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.csd.domain.CSD;
import org.motechproject.csd.domain.Facility;
import org.motechproject.csd.service.CSDService;
import org.motechproject.csd.service.FacilityService;
import org.motechproject.csd.db.InitialData;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Verify FacilityService is present & functional.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class FacilityServiceBundleIT extends BasePaxIT {

    @Inject
    private CSDService csdService;

    @Inject
    private FacilityService facilityService;

    @Before
    public void cleanBefore() {
        getLogger().info("Clean database before test");
        csdService.delete();
    }

    @After
    public void cleanAfter() {
        getLogger().info("Clean database after test");
        csdService.delete();
    }

    @Test
    public void shouldGetAllFacilities() {
        CSD csd = InitialData.getInitialData();
        csd.getProviderDirectory().getProviders().clear();
        csd.getServiceDirectory().getServices().clear();
        csd.getOrganizationDirectory().getOrganizations().clear();
        csdService.create(csd);
        assertTrue(csdService.getCSD().equals(csd));

        assertTrue(facilityService.allFacilities().containsAll(csd.getFacilityDirectory().getFacilities()));
    }

    @Test
    public void shouldGetFacilityByEntityID() {
        CSD csd = InitialData.getInitialData();
        csd.getProviderDirectory().getProviders().clear();
        csd.getServiceDirectory().getServices().clear();
        csd.getOrganizationDirectory().getOrganizations().clear();
        Facility facility = InitialData.createFacility(new DateTime(), "EntityIDToFind");
        csd.getFacilityDirectory().getFacilities().add(facility);
        csdService.create(csd);
        assertTrue(csdService.getCSD().equals(csd));

        assertEquals(facilityService.getFacilityByEntityID("EntityIDToFind"), facility);
    }
}
