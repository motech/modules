package org.motechproject.csd.it;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.csd.domain.CSD;
import org.motechproject.csd.domain.Service;
import org.motechproject.csd.domain.ServiceDirectory;
import org.motechproject.csd.mds.ServiceDirectoryDataService;
import org.motechproject.csd.service.CSDService;
import org.motechproject.csd.service.ServiceDirectoryService;
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
 * Verify ServiceDirectoryService is present & functional.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class ServiceDirectoryServiceBundleIT extends BasePaxIT {

    @Inject
    private CSDService csdService;

    @Inject
    private ServiceDirectoryService serviceDirectoryService;

    @Inject
    private ServiceDirectoryDataService serviceDirectoryDataService;

    @Before
    public void cleanBefore() {
        getLogger().info("Clean database before test");
        csdService.delete();
    }

    @After
    public void cleanAfter() {
        getLogger().info("Clean database after test");
        csdService.delete();
        serviceDirectoryDataService.deleteAll();
    }

    @Test
    public void shouldUpdateServiceDirectoryEntity() {
        CSD csd = InitialData.getInitialData();
        csd.getProviderDirectory().getProviders().clear();
        csd.getFacilityDirectory().getFacilities().clear();
        csd.getOrganizationDirectory().getOrganizations().clear();
        csdService.create(csd);
        assertTrue(csdService.getCSD().equals(csd));

        csd = InitialData.getInitialData();
        csd.getServiceDirectory().getServices().iterator().next().getCodedType().setCode("new code");
        csd.getServiceDirectory().getServices().add(InitialData.createService(new DateTime(), "newService"));
        serviceDirectoryService.update(csd.getServiceDirectory());
        assertTrue(serviceDirectoryService.getServiceDirectory().equals(csd.getServiceDirectory()));
    }

    @Test
    public void shouldGetEntitiesUpdatedAfterGivenDate() {
        DateTime dateUpdated = new DateTime(2020, 1, 1, 1, 1);

        Service service = InitialData.createService(dateUpdated.plusSeconds(1), "lastUpdated");

        CSD csd = InitialData.getInitialData();
        csd.getProviderDirectory().getProviders().clear();
        csd.getServiceDirectory().getServices().add(service);
        csd.getOrganizationDirectory().getOrganizations().clear();
        csd.getFacilityDirectory().getFacilities().clear();
        csdService.create(csd);
        assertTrue(csdService.getCSD().equals(csd));

        Set<Service> lastUpdated = serviceDirectoryService.getModifiedAfter(dateUpdated);

        assertEquals(1, lastUpdated.size());

        assertEquals(lastUpdated.iterator().next(), service);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getServiceDirectoryShouldThrowExceptionIfThereIsMoreThanOneServiceDirectoryEntityInTheDatabase() {
        serviceDirectoryDataService.create(new ServiceDirectory());
        serviceDirectoryDataService.create(new ServiceDirectory());

        serviceDirectoryService.getServiceDirectory();
    }
}
