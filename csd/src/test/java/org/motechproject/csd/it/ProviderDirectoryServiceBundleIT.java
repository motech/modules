package org.motechproject.csd.it;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.csd.domain.CSD;
import org.motechproject.csd.domain.Provider;
import org.motechproject.csd.domain.ProviderDirectory;
import org.motechproject.csd.mds.ProviderDirectoryDataService;
import org.motechproject.csd.service.CSDService;
import org.motechproject.csd.service.ProviderDirectoryService;
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
 * Verify ProviderDirectoryService is present & functional.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class ProviderDirectoryServiceBundleIT extends BasePaxIT {

    @Inject
    private CSDService csdService;

    @Inject
    private ProviderDirectoryService providerDirectoryService;

    @Inject
    private ProviderDirectoryDataService providerDirectoryDataService;

    @Before
    public void cleanBefore() {
        getLogger().info("Clean database before test");
        csdService.delete();
    }

    @After
    public void cleanAfter() {
        getLogger().info("Clean database after test");
        csdService.delete();
        providerDirectoryDataService.deleteAll();
    }

    @Test
    public void shouldUpdateProviderDirectoryEntity() {
        CSD csd = InitialData.getInitialData();
        csd.getFacilityDirectory().getFacilities().clear();
        csd.getServiceDirectory().getServices().clear();
        csd.getOrganizationDirectory().getOrganizations().clear();
        csdService.create(csd);
        assertTrue(csdService.getCSD().equals(csd));

        csd = InitialData.getInitialData();
        csd.getProviderDirectory().getProviders().iterator().next().getDemographic().setGender("M");
        csd.getProviderDirectory().getProviders().add(InitialData.createProvider(new DateTime(), "newProvider"));
        providerDirectoryService.update(csd.getProviderDirectory());
        assertTrue(providerDirectoryService.getProviderDirectory().equals(csd.getProviderDirectory()));
    }

    @Test
    public void shouldGetEntitiesUpdatedAfterGivenDate() {
        DateTime dateUpdated = new DateTime(2020, 1, 1, 1, 1);

        Provider provider = InitialData.createProvider(dateUpdated.plusSeconds(1), "updatedProvider");

        CSD csd = InitialData.getInitialData();
        csd.getFacilityDirectory().getFacilities().clear();
        csd.getServiceDirectory().getServices().clear();
        csd.getOrganizationDirectory().getOrganizations().clear();
        csd.getProviderDirectory().getProviders().add(provider);
        csdService.create(csd);
        assertTrue(csdService.getCSD().equals(csd));

        Set<Provider> lastUpdated = providerDirectoryService.getModifiedAfter(dateUpdated);

        assertEquals(1, lastUpdated.size());

        assertEquals(lastUpdated.iterator().next(), provider);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getProviderDirectoryShouldThrowExceptionIfThereIsMoreThanOneProviderDirectoryEntityInTheDatabase() {
        providerDirectoryDataService.create(new ProviderDirectory());
        providerDirectoryDataService.create(new ProviderDirectory());

        providerDirectoryService.getProviderDirectory();
    }
}
