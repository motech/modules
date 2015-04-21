package org.motechproject.csd.it;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.csd.domain.CSD;
import org.motechproject.csd.domain.Provider;
import org.motechproject.csd.service.CSDService;
import org.motechproject.csd.service.ProviderService;
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
 * Verify ProviderService is present & functional.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class ProviderServiceBundleIT extends BasePaxIT {

    @Inject
    private CSDService csdService;

    @Inject
    private ProviderService providerService;

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
    public void shouldGetAllProviders() {
        CSD csd = InitialData.getInitialData();
        csd.getFacilityDirectory().getFacilities().clear();
        csd.getServiceDirectory().getServices().clear();
        csd.getOrganizationDirectory().getOrganizations().clear();
        csdService.create(csd);
        assertTrue(csdService.getCSD().equals(csd));

        assertTrue(providerService.allProviders().containsAll(csd.getProviderDirectory().getProviders()));
    }

    @Test
    public void shouldGetProviderByEntityID() {
        CSD csd = InitialData.getInitialData();
        csd.getFacilityDirectory().getFacilities().clear();
        csd.getServiceDirectory().getServices().clear();
        csd.getOrganizationDirectory().getOrganizations().clear();
        Provider provider = InitialData.createProvider(new DateTime(), "EntityIDToFind");
        csd.getProviderDirectory().getProviders().add(provider);
        csdService.create(csd);
        assertTrue(csdService.getCSD().equals(csd));

        assertEquals(providerService.getProviderByEntityID("EntityIDToFind"), provider);
    }
}
