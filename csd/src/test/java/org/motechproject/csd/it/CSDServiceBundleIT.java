package org.motechproject.csd.it;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.csd.domain.Address;
import org.motechproject.csd.domain.AddressLine;
import org.motechproject.csd.domain.CSD;
import org.motechproject.csd.domain.Facility;
import org.motechproject.csd.domain.Service;
import org.motechproject.csd.mds.AddressLineDataService;
import org.motechproject.csd.service.CSDService;
import org.motechproject.csd.db.InitialData;
import org.motechproject.csd.service.FacilityService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Verify CSDService is present & functional.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class CSDServiceBundleIT extends BasePaxIT {

    @Inject
    private CSDService csdService;

    @Inject
    private FacilityService facilityService;

    @Inject
    private AddressLineDataService addressLineDataService;

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
    public void shouldUpdateCSDEntity() {
        CSD csd = InitialData.getInitialData();
        csdService.update(csd);
        assertTrue(csdService.getCSD().equals(csd));

        csd = InitialData.getInitialData();
        csd.getFacilityDirectory().getFacilities().iterator().next().setPrimaryName("updated name");
        csd.getServiceDirectory().getServices().add(InitialData.createService(new DateTime(), "newEntityID"));
        csdService.update(csd);
        assertTrue(csdService.getCSD().equals(csd));
    }

    @Test
    public void shouldGetEntitiesUpdatedAfterGivenDate() {
        DateTime dateUpdated = new DateTime(2020, 1, 1, 1, 1);

        Facility facility = InitialData.createFacility(dateUpdated.plusSeconds(1), "lastUpdated");
        Service service = InitialData.createService(dateUpdated.plusSeconds(1), "lastUpdated");

        CSD csd = InitialData.getInitialData();
        csd.getFacilityDirectory().getFacilities().add(facility);
        csd.getServiceDirectory().getServices().add(service);
        csdService.update(csd);
        assertTrue(csdService.getCSD().equals(csd));

        CSD lastUpdated = csdService.getByLastModification(dateUpdated);

        assertEquals(1, lastUpdated.getFacilityDirectory().getFacilities().size());
        assertEquals(0, lastUpdated.getProviderDirectory().getProviders().size());
        assertEquals(0, lastUpdated.getOrganizationDirectory().getOrganizations().size());
        assertEquals(1, lastUpdated.getServiceDirectory().getServices().size());

        assertEquals(lastUpdated.getFacilityDirectory().getFacilities().iterator().next(), facility);
        assertEquals(lastUpdated.getServiceDirectory().getServices().iterator().next(), service);
    }

    @Test
    public void shouldGetXmlContent() throws IOException {
        CSD csd = InitialData.getInitialData();
        csdService.update(csd);
        assertTrue(csdService.getCSD().equals(csd));

        InputStream in = getClass().getResourceAsStream("/initialXml.xml");
        String xml = IOUtils.toString(in);

        assertEquals(xml, csdService.getXmlContent());
    }

    @Test
    public void shouldGetEntitiesFromXmlAndSaveToDatabase() throws IOException {
        InputStream in = getClass().getResourceAsStream("/initialXml.xml");
        String xml = IOUtils.toString(in);

        csdService.saveFromXml(xml);

        CSD csd = InitialData.getInitialData();

        assertTrue(csdService.getCSD().equals(csd));
    }

    @Test
    public void shouldUpdateParentModificationDate() {
        CSD csd = InitialData.getInitialData();
        csdService.update(csd);
        assertTrue(csdService.getCSD().equals(csd));

        Facility facility = facilityService.getFacilityByEntityID("FacilityEntityID");
        assertNotNull(facility);
        DateTime modificationDate = facility.getModificationDate();
        assertNotNull(modificationDate);

        Set<Address> addresses = facility.getAddresses();
        assertTrue(addresses != null && addresses.size() > 0);

        Set<AddressLine> addressLines = addresses.iterator().next().getAddressLines();
        assertTrue(addressLines != null && addressLines.size() > 0);

        addressLineDataService.update(addressLines.iterator().next());

        facility = facilityService.getFacilityByEntityID("FacilityEntityID");
        DateTime newModificationDate = facility.getModificationDate();
        assertNotNull(newModificationDate);

        assertNotSame(modificationDate, newModificationDate);
    }
}
