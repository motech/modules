package org.motechproject.csd.it;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.csd.domain.CSD;
import org.motechproject.csd.domain.Organization;
import org.motechproject.csd.domain.OrganizationDirectory;
import org.motechproject.csd.mds.OrganizationDirectoryDataService;
import org.motechproject.csd.service.CSDService;
import org.motechproject.csd.service.OrganizationDirectoryService;
import org.motechproject.csd.util.InitialData;
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
 * Verify OrganizationDirectoryService is present & functional.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class OrganizationDirectoryServiceBundleIT extends BasePaxIT {

    @Inject
    private CSDService csdService;

    @Inject
    private OrganizationDirectoryService organizationDirectoryService;

    @Inject
    private OrganizationDirectoryDataService organizationDirectoryDataService;

    @Before
    public void cleanBefore() {
        getLogger().info("Clean database before test");
        csdService.delete();
    }

    @After
    public void cleanAfter() {
        getLogger().info("Clean database after test");
        csdService.delete();
        organizationDirectoryDataService.deleteAll();
    }

    @Test
    public void shouldUpdateOrganizationDirectoryEntity() {
        CSD csd = InitialData.getInitialData();
        csd.getProviderDirectory().getProviders().clear();
        csd.getServiceDirectory().getServices().clear();
        csd.getFacilityDirectory().getFacilities().clear();
        csdService.create(csd);
        assertTrue(csdService.getCSD().equals(csd));

        csd = InitialData.getInitialData();
        csd.getOrganizationDirectory().getOrganizations().iterator().next().setPrimaryName("updated name");
        csd.getOrganizationDirectory().getOrganizations().add(InitialData.createOrganization(new DateTime(), "newOrganization"));
        organizationDirectoryService.update(csd.getOrganizationDirectory());
        assertTrue(organizationDirectoryService.getOrganizationDirectory().equals(csd.getOrganizationDirectory()));
    }

    @Test
    public void shouldGetEntitiesUpdatedAfterGivenDate() {
        DateTime dateUpdated = new DateTime(2020, 1, 1, 1, 1);

        Organization organization = InitialData.createOrganization(dateUpdated.plusSeconds(1), "lastUpdated");

        CSD csd = InitialData.getInitialData();
        csd.getProviderDirectory().getProviders().clear();
        csd.getServiceDirectory().getServices().clear();
        csd.getFacilityDirectory().getFacilities().clear();
        csd.getOrganizationDirectory().getOrganizations().add(organization);
        csdService.create(csd);
        assertTrue(csdService.getCSD().equals(csd));

        Set<Organization> lastUpdated = organizationDirectoryService.getModifiedAfter(dateUpdated);

        assertEquals(1, lastUpdated.size());

        assertEquals(lastUpdated.iterator().next(), organization);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getOrganizationDirectoryShouldThrowExceptionIfThereIsMoreThanOneOrganizationDirectoryEntityInTheDatabase() {
        organizationDirectoryDataService.create(new OrganizationDirectory());
        organizationDirectoryDataService.create(new OrganizationDirectory());

        organizationDirectoryService.getOrganizationDirectory();
    }
}
