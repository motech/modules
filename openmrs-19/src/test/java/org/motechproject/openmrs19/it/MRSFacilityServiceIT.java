package org.motechproject.openmrs19.it;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventListener;
import org.motechproject.event.listener.EventListenerRegistryService;
import org.motechproject.openmrs19.domain.OpenMRSFacility;
import org.motechproject.openmrs19.service.EventKeys;
import org.motechproject.openmrs19.service.OpenMRSFacilityService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class MRSFacilityServiceIT extends BasePaxIT {

    @Inject
    private OpenMRSFacilityService facilityAdapter;

    @Inject
    EventListenerRegistryService eventListenerRegistry;

    final Object lock = new Object();

    MrsListener mrsListener;

    OpenMRSFacility facilityOne;
    OpenMRSFacility facilityTwo;

    @Before
    public void initialize() throws InterruptedException {
        mrsListener = new MrsListener();
        eventListenerRegistry.registerListener(mrsListener, Arrays.asList(EventKeys.CREATED_NEW_FACILITY_SUBJECT, EventKeys.UPDATED_FACILITY_SUBJECT, EventKeys.DELETED_FACILITY_SUBJECT));

        facilityOne = createFacility(prepareFacilityOne());
        facilityTwo = createFacility(prepareFacilityTwo());
    }

    @Test
    public void shouldCreateFacility() throws InterruptedException {

        assertNotNull(facilityTwo);

        assertTrue(mrsListener.created);
        assertFalse(mrsListener.updated);
        assertFalse(mrsListener.deleted);
        assertEquals(facilityTwo.getName(), mrsListener.eventParameters.get(EventKeys.FACILITY_NAME));
        assertEquals(facilityTwo.getCountry(), mrsListener.eventParameters.get(EventKeys.FACILITY_COUNTRY));
        assertEquals(facilityTwo.getRegion(), mrsListener.eventParameters.get(EventKeys.FACILITY_REGION));
        assertEquals(facilityTwo.getCountyDistrict(), mrsListener.eventParameters.get(EventKeys.FACILITY_COUNTY_DISTRICT));
        assertEquals(facilityTwo.getStateProvince(), mrsListener.eventParameters.get(EventKeys.FACILITY_STATE_PROVINCE));

        deleteFacility(facilityOne);
    }

    @Test
    public void shouldUpdateFacility() throws InterruptedException {

        final String newCountry = "FooCountryTwo";
        final String newStateProvince = "FooStateProvinceTwo";

        facilityOne.setCountry(newCountry);
        facilityOne.setStateProvince(newStateProvince);

        OpenMRSFacility updatedFacility;

        synchronized (lock) {
            updatedFacility = facilityAdapter.updateFacility(facilityOne);
            assertNotNull(updatedFacility);

            lock.wait(60000);
        }

        assertEquals(updatedFacility.getStateProvince(), mrsListener.eventParameters.get(EventKeys.FACILITY_STATE_PROVINCE));
        assertEquals(updatedFacility.getCountry(), mrsListener.eventParameters.get(EventKeys.FACILITY_COUNTRY));

        assertTrue(mrsListener.created);
        assertTrue(mrsListener.updated);
        assertFalse(mrsListener.deleted);
    }

    @Test
    public void shouldDeleteFacility() throws InterruptedException {

        synchronized (lock) {
            facilityAdapter.deleteFacility(facilityOne.getFacilityId());
            assertNull(facilityAdapter.getFacility(facilityOne.getFacilityId()));

            lock.wait(60000);
        }

        assertTrue(mrsListener.created);
        assertFalse(mrsListener.updated);
        assertTrue(mrsListener.deleted);
    }

    @Test
    public void shouldGetFacilities() {

        List<? extends OpenMRSFacility> facilities = facilityAdapter.getFacilities(1, 3);

        assertEquals(3, facilities.size());
        assertTrue(facilities.containsAll(Arrays.asList(facilityOne, facilityTwo)));
    }

    @Test
    public void shouldGetAllFacilities() {

        List<? extends OpenMRSFacility> facilities = facilityAdapter.getAllFacilities();

        assertEquals(3, facilities.size());
        assertTrue(facilities.containsAll(Arrays.asList(facilityOne, facilityTwo)));
    }

    @Test
    public void shouldFindFacilitiesByName() {

        List<? extends OpenMRSFacility> facilities = facilityAdapter.getFacilities("FooName");

        assertEquals(2, facilities.size());
    }

    @Test
    public void shouldFindFacilityById() {

        assertNotNull(facilityAdapter.getFacility(facilityOne.getFacilityId()));
    }

    @After
    public void tearDown() {
        deleteFacility(facilityOne);
        deleteFacility(facilityTwo);
        eventListenerRegistry.clearListenersForBean("mrsTestListener");
    }

    private OpenMRSFacility prepareFacilityOne() {
        return new OpenMRSFacility("FooName", "FooCountry", "FooRegion", "FooCountryDistrict", "FooStateProvince");
    }

    private OpenMRSFacility prepareFacilityTwo() {
        return new OpenMRSFacility("FooNameTwo", "FooCountryTwo", "FooRegionTwo", "FooCountryDistrictTwo", "FooStateProvinceTwo");
    }

    private OpenMRSFacility createFacility(OpenMRSFacility facility) throws InterruptedException {

        OpenMRSFacility created;

        synchronized (lock) {
            created = facilityAdapter.createFacility(facility);
            assertNotNull(created);

            lock.wait(60000);
        }

        return created;
    }

    private void deleteFacility(OpenMRSFacility facility) {
        facilityAdapter.deleteFacility(facility.getFacilityId());

        assertNull(facilityAdapter.getFacility(facility.getFacilityId()));
    }

    public class MrsListener implements EventListener {

        private boolean created = false;
        private boolean updated = false;
        private boolean deleted = false;
        private Map<String, Object> eventParameters;

        public void handle(MotechEvent event) {
            if (event.getSubject().equals(EventKeys.CREATED_NEW_FACILITY_SUBJECT)) {
                created = true;
            } else if (event.getSubject().equals(EventKeys.UPDATED_FACILITY_SUBJECT)) {
                updated = true;
            } else if (event.getSubject().equals(EventKeys.DELETED_FACILITY_SUBJECT)) {
                deleted = true;
            }
            eventParameters = event.getParameters();
            synchronized (lock) {
                lock.notify();
            }
        }

        @Override
        public String getIdentifier() {
            return "mrsTestListener";
        }
    }
}
