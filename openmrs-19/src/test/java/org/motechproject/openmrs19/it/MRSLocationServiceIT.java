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
public class MRSLocationServiceIT extends BasePaxIT {

    @Inject
    private OpenMRSLocationService locationAdapter;

    @Inject
    EventListenerRegistryService eventListenerRegistry;

    final Object lock = new Object();

    MrsListener mrsListener;

    Location locationOne;
    Location locationTwo;

    @Before
    public void initialize() throws InterruptedException {
        mrsListener = new MrsListener();
        eventListenerRegistry.registerListener(mrsListener, Arrays.asList(EventKeys.CREATED_NEW_LOCATION_SUBJECT, EventKeys.UPDATED_LOCATION_SUBJECT, EventKeys.DELETED_LOCATION_SUBJECT));

        locationOne = createLocation(prepareLocationOne());
        locationTwo = createLocation(prepareLocationTwo());
    }

    @Test
    public void shouldCreateLocation() throws InterruptedException {

        assertNotNull(locationTwo);

        assertTrue(mrsListener.created);
        assertFalse(mrsListener.updated);
        assertFalse(mrsListener.deleted);
        assertEquals(locationTwo.getName(), mrsListener.eventParameters.get(EventKeys.LOCATION_NAME));
        assertEquals(locationTwo.getCountry(), mrsListener.eventParameters.get(EventKeys.LOCATION_COUNTRY));
        assertEquals(locationTwo.getAddress6(), mrsListener.eventParameters.get(EventKeys.LOCATION_REGION));
        assertEquals(locationTwo.getCountyDistrict(), mrsListener.eventParameters.get(EventKeys.LOCATION_COUNTY_DISTRICT));
        assertEquals(locationTwo.getStateProvince(), mrsListener.eventParameters.get(EventKeys.LOCATION_STATE_PROVINCE));

        deleteLocation(locationOne);
    }

    @Test
    public void shouldUpdateLocation() throws InterruptedException {

        final String newCountry = "FooCountryTwo";
        final String newStateProvince = "FooStateProvinceTwo";

        locationOne.setCountry(newCountry);
        locationOne.setStateProvince(newStateProvince);

        Location updatedLocation;

        synchronized (lock) {
            updatedLocation = locationAdapter.updateLocation(locationOne);
            assertNotNull(updatedLocation);

            lock.wait(60000);
        }

        assertEquals(updatedLocation.getStateProvince(), mrsListener.eventParameters.get(EventKeys.LOCATION_STATE_PROVINCE));
        assertEquals(updatedLocation.getCountry(), mrsListener.eventParameters.get(EventKeys.LOCATION_COUNTRY));

        assertTrue(mrsListener.created);
        assertTrue(mrsListener.updated);
        assertFalse(mrsListener.deleted);
    }

    @Test
    public void shouldDeleteLocation() throws InterruptedException {

        synchronized (lock) {
            locationAdapter.deleteLocation(locationOne.getUuid());
            assertNull(locationAdapter.getLocationByUuid(locationOne.getUuid()));

            lock.wait(60000);
        }

        assertTrue(mrsListener.created);
        assertFalse(mrsListener.updated);
        assertTrue(mrsListener.deleted);
    }

    @Test
    public void shouldGetLocations() {

        List<Location> locations = locationAdapter.getLocations(1, 3);

        assertEquals(3, locations.size());
        assertTrue(locations.containsAll(Arrays.asList(locationOne, locationTwo)));
    }

    @Test
    public void shouldGetAllLocations() {

        List<Location> locations = locationAdapter.getAllLocations();

        assertEquals(3, locations.size());
        assertTrue(locations.containsAll(Arrays.asList(locationOne, locationTwo)));
    }

    @Test
    public void shouldFindLocationsByName() {

        List<Location> locations = locationAdapter.getLocations("FooName");

        assertEquals(2, locations.size());
    }

    @Test
    public void shouldFindLocationById() {

        assertNotNull(locationAdapter.getLocationByUuid(locationOne.getUuid()));
    }

    @After
    public void tearDown() {
        deleteLocation(locationOne);
        deleteLocation(locationTwo);
        eventListenerRegistry.clearListenersForBean("mrsTestListener");
    }

    private Location prepareLocationOne() {
        return new Location("FooName", "FooCountry", "FooRegion", "FooCountryDistrict", "FooStateProvince");
    }

    private Location prepareLocationTwo() {
        return new Location("FooNameTwo", "FooCountryTwo", "FooRegionTwo", "FooCountryDistrictTwo", "FooStateProvinceTwo");
    }

    private Location createLocation(Location location) throws InterruptedException {

        Location created;

        synchronized (lock) {
            created = locationAdapter.createLocation(location);
            assertNotNull(created);

            lock.wait(60000);
        }

        return created;
    }

    private void deleteLocation(Location location) {
        locationAdapter.deleteLocation(location.getUuid());

        assertNull(locationAdapter.getLocationByUuid(location.getUuid()));
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
