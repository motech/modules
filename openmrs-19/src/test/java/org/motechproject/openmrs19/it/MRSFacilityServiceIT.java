package org.motechproject.openmrs19.it;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventListener;
import org.motechproject.event.listener.EventListenerRegistryService;
import org.motechproject.openmrs19.domain.Location;
import org.motechproject.openmrs19.service.EventKeys;
import org.motechproject.openmrs19.service.OpenMRSLocationService;
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
    private OpenMRSLocationService facilityAdapter;

    @Inject
    EventListenerRegistryService eventListenerRegistry;

    final Object lock = new Object();

    MrsListener mrsListener;

    Location facilityOne;
    Location facilityTwo;

    @Before
    public void initialize() throws InterruptedException {
        mrsListener = new MrsListener();
        eventListenerRegistry.registerListener(mrsListener, Arrays.asList(EventKeys.CREATED_NEW_LOCATION_SUBJECT, EventKeys.UPDATED_LOCATION_SUBJECT, EventKeys.DELETED_LOCATION_SUBJECT));

        facilityOne = createFacility(prepareFacilityOne());
        facilityTwo = createFacility(prepareFacilityTwo());
    }

    @Test
    public void shouldCreateFacility() throws InterruptedException {

        assertNotNull(facilityTwo);

        assertTrue(mrsListener.created);
        assertFalse(mrsListener.updated);
        assertFalse(mrsListener.deleted);
        assertEquals(facilityTwo.getName(), mrsListener.eventParameters.get(EventKeys.LOCATION_NAME));
        assertEquals(facilityTwo.getCountry(), mrsListener.eventParameters.get(EventKeys.LOCATION_COUNTRY));
        assertEquals(facilityTwo.getAddress6(), mrsListener.eventParameters.get(EventKeys.LOCATION_REGION));
        assertEquals(facilityTwo.getCountyDistrict(), mrsListener.eventParameters.get(EventKeys.LOCATION_COUNTY_DISTRICT));
        assertEquals(facilityTwo.getStateProvince(), mrsListener.eventParameters.get(EventKeys.LOCATION_STATE_PROVINCE));

        deleteFacility(facilityOne);
    }

    @Test
    public void shouldUpdateFacility() throws InterruptedException {

        final String newCountry = "FooCountryTwo";
        final String newStateProvince = "FooStateProvinceTwo";

        facilityOne.setCountry(newCountry);
        facilityOne.setStateProvince(newStateProvince);

        Location updatedFacility;

        synchronized (lock) {
            updatedFacility = facilityAdapter.updateLocation(facilityOne);
            assertNotNull(updatedFacility);

            lock.wait(60000);
        }

        assertEquals(updatedFacility.getStateProvince(), mrsListener.eventParameters.get(EventKeys.LOCATION_STATE_PROVINCE));
        assertEquals(updatedFacility.getCountry(), mrsListener.eventParameters.get(EventKeys.LOCATION_COUNTRY));

        assertTrue(mrsListener.created);
        assertTrue(mrsListener.updated);
        assertFalse(mrsListener.deleted);
    }

    @Test
    public void shouldDeleteFacility() throws InterruptedException {

        synchronized (lock) {
            facilityAdapter.deleteLocation(facilityOne.getUuid());
            assertNull(facilityAdapter.getLocationByUuid(facilityOne.getUuid()));

            lock.wait(60000);
        }

        assertTrue(mrsListener.created);
        assertFalse(mrsListener.updated);
        assertTrue(mrsListener.deleted);
    }

    @Test
    public void shouldGetFacilities() {

        List<? extends Location> facilities = facilityAdapter.getLocations(1, 3);

        assertEquals(3, facilities.size());
        assertTrue(facilities.containsAll(Arrays.asList(facilityOne, facilityTwo)));
    }

    @Test
    public void shouldGetAllFacilities() {

        List<? extends Location> facilities = facilityAdapter.getAllLocations();

        assertEquals(3, facilities.size());
        assertTrue(facilities.containsAll(Arrays.asList(facilityOne, facilityTwo)));
    }

    @Test
    public void shouldFindFacilitiesByName() {

        List<? extends Location> facilities = facilityAdapter.getLocations("FooName");

        assertEquals(2, facilities.size());
    }

    @Test
    public void shouldFindFacilityById() {

        assertNotNull(facilityAdapter.getLocationByUuid(facilityOne.getUuid()));
    }

    @After
    public void tearDown() {
        deleteFacility(facilityOne);
        deleteFacility(facilityTwo);
        eventListenerRegistry.clearListenersForBean("mrsTestListener");
    }

    private Location prepareFacilityOne() {
        return new Location("FooName", "FooCountry", "FooRegion", "FooCountryDistrict", "FooStateProvince");
    }

    private Location prepareFacilityTwo() {
        return new Location("FooNameTwo", "FooCountryTwo", "FooRegionTwo", "FooCountryDistrictTwo", "FooStateProvinceTwo");
    }

    private Location createFacility(Location facility) throws InterruptedException {

        Location created;

        synchronized (lock) {
            created = facilityAdapter.createLocation(facility);
            assertNotNull(created);

            lock.wait(60000);
        }

        return created;
    }

    private void deleteFacility(Location facility) {
        facilityAdapter.deleteLocation(facility.getUuid());

        assertNull(facilityAdapter.getLocationByUuid(facility.getUuid()));
    }

    public class MrsListener implements EventListener {

        private boolean created = false;
        private boolean updated = false;
        private boolean deleted = false;
        private Map<String, Object> eventParameters;

        public void handle(MotechEvent event) {
            if (event.getSubject().equals(EventKeys.CREATED_NEW_LOCATION_SUBJECT)) {
                created = true;
            } else if (event.getSubject().equals(EventKeys.UPDATED_LOCATION_SUBJECT)) {
                updated = true;
            } else if (event.getSubject().equals(EventKeys.DELETED_LOCATION_SUBJECT)) {
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
