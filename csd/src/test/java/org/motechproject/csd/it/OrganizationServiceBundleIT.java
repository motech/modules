package org.motechproject.csd.it;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.csd.domain.CSD;
import org.motechproject.csd.domain.Organization;
import org.motechproject.csd.service.CSDService;
import org.motechproject.csd.service.OrganizationService;
import org.motechproject.csd.util.InitialData;
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
 * Verify OrganizationService is present & functional.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class OrganizationServiceBundleIT extends BasePaxIT {

    @Inject
    private CSDService csdService;

    @Inject
    private OrganizationService organizationService;

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
    public void shouldGetAllOrganizations() {
        CSD csd = InitialData.getInitialData();
        csd.getProviderDirectory().getProviders().clear();
        csd.getServiceDirectory().getServices().clear();
        csd.getFacilityDirectory().getFacilities().clear();
        csdService.create(csd);
        assertTrue(csdService.getCSD().equals(csd));

        assertTrue(organizationService.allOrganizations().containsAll(csd.getOrganizationDirectory().getOrganizations()));
    }

    @Test
    public void shouldGetOrganizationByEntityID() {
        CSD csd = InitialData.getInitialData();
        csd.getProviderDirectory().getProviders().clear();
        csd.getServiceDirectory().getServices().clear();
        csd.getFacilityDirectory().getFacilities().clear();
        Organization organization = InitialData.createOrganization(new DateTime(), "EntityIDToFind");
        csd.getOrganizationDirectory().getOrganizations().add(organization);
        csdService.create(csd);
        assertTrue(csdService.getCSD().equals(csd));

        assertEquals(organizationService.getOrganizationByEntityID("EntityIDToFind"), organization);
    }
}
