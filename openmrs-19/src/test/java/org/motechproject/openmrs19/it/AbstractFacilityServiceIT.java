package org.motechproject.openmrs19.it;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventListener;
import org.motechproject.event.listener.EventListenerRegistryService;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.openmrs19.EventKeys;
import org.motechproject.openmrs19.domain.OpenMRSFacility;
import org.motechproject.openmrs19.service.OpenMRSFacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
public abstract class AbstractFacilityServiceIT {

    @Autowired
    private OpenMRSFacilityService facilityAdapter;

    @Autowired
    EventListenerRegistryService eventListenerRegistry;

    MrsListener mrsListener;
    final Object lock = new Object();

    @Before
    public void initialize() {
        mrsListener = new MrsListener();
        eventListenerRegistry.registerListener(mrsListener, Arrays.asList(EventKeys.CREATED_NEW_FACILITY_SUBJECT, EventKeys.UPDATED_FACILITY_SUBJECT, EventKeys.DELETED_FACILITY_SUBJECT));
    }

    @Test
    public void shouldSaveFacility() throws InterruptedException {
        OpenMRSFacility facility = new OpenMRSFacility("Test Facility", "US", "New York", "First", "Manhattan");
        OpenMRSFacility created;

        synchronized (lock) {
            created = (OpenMRSFacility) facilityAdapter.saveFacility(facility);
            lock.wait(60000);
        }

        assertNotNull(created.getId());

        assertTrue(mrsListener.created);
        assertFalse(mrsListener.updated);
        assertFalse(mrsListener.deleted);
        assertEquals(created.getName(), mrsListener.eventParameters.get(EventKeys.FACILITY_NAME));
        assertEquals(created.getCountry(), mrsListener.eventParameters.get(EventKeys.FACILITY_COUNTRY));
        assertEquals(created.getRegion(), mrsListener.eventParameters.get(EventKeys.FACILITY_REGION));
        assertEquals(created.getCountyDistrict(), mrsListener.eventParameters.get(EventKeys.FACILITY_COUNTY_DISTRICT));
        assertEquals(created.getStateProvince(), mrsListener.eventParameters.get(EventKeys.FACILITY_STATE_PROVINCE));
    }

    @Test
    public void shouldUpdateFacility() throws InterruptedException {
        OpenMRSFacility facility = new OpenMRSFacility("Facility to update", "Pl", "Danzig", "Pomerania", "Neptun");
        OpenMRSFacility created;

        synchronized (lock) {
            created = (OpenMRSFacility) facilityAdapter.saveFacility(facility);
            lock.wait(60000);
        }

        assertNotNull(created.getId());
        assertTrue(mrsListener.created);
        assertFalse(mrsListener.updated);
        assertFalse(mrsListener.deleted);
        assertEquals(created.getName(), mrsListener.eventParameters.get(EventKeys.FACILITY_NAME));
        assertEquals(created.getCountry(), mrsListener.eventParameters.get(EventKeys.FACILITY_COUNTRY));

        created.setCountry("US");
        created.setStateProvince("LA");
        OpenMRSFacility updatedFacility;

        synchronized (lock) {
            updatedFacility = (OpenMRSFacility) facilityAdapter.updateFacility(created);
            lock.wait(60000);
        }

        assertNotNull(updatedFacility.getId());
        assertTrue(mrsListener.created);
        assertTrue(mrsListener.updated);
        assertFalse(mrsListener.deleted);
        assertEquals(updatedFacility.getStateProvince(), mrsListener.eventParameters.get(EventKeys.FACILITY_STATE_PROVINCE));
        assertEquals(created.getCountry(), mrsListener.eventParameters.get(EventKeys.FACILITY_COUNTRY));
        assertEquals(created.getCountry(), "US");
        assertEquals(created.getStateProvince(), "LA");
    }

    @Test
    public void shouldDeleteFacility() throws InterruptedException {
        OpenMRSFacility facility = new OpenMRSFacility("Facility to delete", "Pl", "Danzig", "Pomerania", "Neptun");
        OpenMRSFacility created;

        synchronized (lock) {
            created = (OpenMRSFacility) facilityAdapter.saveFacility(facility);
            lock.wait(60000);
        }

        assertNotNull(created.getId());
        assertTrue(mrsListener.created);
        assertFalse(mrsListener.updated);
        assertFalse(mrsListener.deleted);
        assertEquals(created.getName(), mrsListener.eventParameters.get(EventKeys.FACILITY_NAME));
        assertEquals(created.getCountry(), mrsListener.eventParameters.get(EventKeys.FACILITY_COUNTRY));

        synchronized (lock) {
            facilityAdapter.deleteFacility(created.getFacilityId());
            lock.wait(60000);
        }

        assertNull(facilityAdapter.getFacility(created.getFacilityId()));
        assertTrue(mrsListener.created);
        assertFalse(mrsListener.updated);
        assertTrue(mrsListener.deleted);
    }

    @Test
    public void shouldFindMultipleFacilities() {
        List<? extends OpenMRSFacility> facilities = facilityAdapter.getFacilities();

        assertTrue(facilities.size() > 0);
    }

    @Test
    public void shouldFindSingleFacilityByName() {
        List<? extends OpenMRSFacility> facilities = facilityAdapter.getFacilities("Clinic 1");

        assertEquals(1, facilities.size());
    }

    @Test
    public void shouldFindFacilityById() {
        OpenMRSFacility facility = (OpenMRSFacility) facilityAdapter.getFacility("Clinic 2");
        OpenMRSFacility persisted = (OpenMRSFacility) facilityAdapter.getFacility(facility.getFacilityId());

        assertNotNull(persisted);
    }

    @After
    public void tearDown() {
        eventListenerRegistry.clearListenersForBean("mrsTestListener");
    }

    public class MrsListener implements EventListener {

        private boolean created = false;
        private boolean updated = false;
        private boolean deleted = false;
        private Map<String, Object> eventParameters;

        @MotechListener(subjects = {EventKeys.CREATED_NEW_FACILITY_SUBJECT, EventKeys.UPDATED_FACILITY_SUBJECT, EventKeys.DELETED_FACILITY_SUBJECT})
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
