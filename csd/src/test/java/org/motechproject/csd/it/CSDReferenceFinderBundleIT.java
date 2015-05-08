package org.motechproject.csd.it;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.csd.domain.Address;
import org.motechproject.csd.domain.AddressLine;
import org.motechproject.csd.domain.CSD;
import org.motechproject.csd.domain.Facility;
import org.motechproject.csd.domain.Geocode;
import org.motechproject.csd.domain.Person;
import org.motechproject.csd.domain.Provider;
import org.motechproject.csd.mds.PersonDataService;
import org.motechproject.csd.service.CSDService;
import org.motechproject.csd.service.FacilityService;
import org.motechproject.csd.service.ProviderService;
import org.motechproject.csd.util.CSDReferenceFinder;
import org.motechproject.csd.db.InitialData;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;

import java.util.Set;

import static org.junit.Assert.*;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class CSDReferenceFinderBundleIT extends BasePaxIT {

    @Inject
    private BundleContext bundleContext;

    @Inject
    private CSDService csdService;

    @Inject
    private FacilityService facilityService;

    @Inject
    private ProviderService providerService;

    @Inject
    private PersonDataService personDataService;

    private CSDReferenceFinder csdReferenceFinder;

    @Before
    public void before() throws Exception {
        this.csdReferenceFinder = new CSDReferenceFinder();
        assertNotNull(bundleContext);
        csdReferenceFinder.setBundleContext(bundleContext);

        getLogger().info("Clean database before test");
        csdService.delete();
    }

    @After
    public void after() {
        getLogger().info("Clean database after test");
        csdService.delete();
    }

    @Test
    public void shouldFindReferences() {
        CSD csd = InitialData.getInitialData();
        csdService.update(csd);
        assertTrue(csdService.getCSD().equals(csd));

        Facility facility = facilityService.getFacilityByEntityID("FacilityEntityID");
        assertNotNull(facility);

        Geocode geocode = facility.getGeocode();
        assertNotNull(geocode);

        Set<Object> facilities = csdReferenceFinder.findReferences(geocode);
        assertEquals(1, facilities.size());
        assertTrue(facilities.contains(facility));

        Provider provider = providerService.getProviderByEntityID("providerEntityID");
        assertNotNull(provider);

        Long personID = provider.getDemographic().getId();

        Person person = personDataService.findById(personID);
        assertNotNull(person);

        Set<Object> providers = csdReferenceFinder.findReferences(person);
        assertEquals(1, providers.size());
        assertTrue(providers.contains(provider));
    }

    @Test
    public void shouldFindParentEntity() {
        CSD csd = InitialData.getInitialData();
        csdService.update(csd);
        assertTrue(csdService.getCSD().equals(csd));

        Facility facility = facilityService.getFacilityByEntityID("FacilityEntityID");
        assertNotNull(facility);

        Set<Address> addresses = facility.getAddresses();
        assertTrue(addresses != null && addresses.size() > 0);

        Set<AddressLine> addressLines = addresses.iterator().next().getAddressLines();
        assertTrue(addressLines != null && addressLines.size() > 0);

        Object parent = csdReferenceFinder.findParentEntity(addressLines.iterator().next());
        assertNotNull(parent);
        assertEquals(facility, parent);
    }
}
