package org.motechproject.openmrs19.it;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventListener;
import org.motechproject.event.listener.EventListenerRegistryService;
import org.motechproject.openmrs19.service.EventKeys;
import org.motechproject.openmrs19.domain.OpenMRSAttribute;
import org.motechproject.openmrs19.domain.OpenMRSPerson;
import org.motechproject.openmrs19.exception.OpenMRSException;
import org.motechproject.openmrs19.service.OpenMRSPersonService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class MRSPersonServiceIT extends BasePaxIT {

    @Inject
    private OpenMRSPersonService personAdapter;

    @Inject
    private EventListenerRegistryService eventListenerRegistry;

    final Object lock = new Object();

    private MrsListener mrsListener;

    private OpenMRSPerson person;

    @Before
    public void initialize() throws InterruptedException {
        mrsListener = new MrsListener();
        eventListenerRegistry.registerListener(mrsListener, Arrays.asList(EventKeys.CREATED_NEW_PERSON_SUBJECT,
            EventKeys.UPDATED_PERSON_SUBJECT, EventKeys.DELETED_PERSON_SUBJECT));

        person = savePerson(preparePerson());
    }

    @Test
    public void shouldCreatePerson() {

        assertNotNull(person.getId());
        assertEquals(mrsListener.eventParameters.get(EventKeys.PERSON_ID), person.getPersonId());
        assertEquals(mrsListener.eventParameters.get(EventKeys.PERSON_FIRST_NAME), person.getFirstName());
        assertEquals(mrsListener.eventParameters.get(EventKeys.PERSON_LAST_NAME), person.getLastName());

        assertTrue(mrsListener.created);
        assertFalse(mrsListener.deleted);
        assertFalse(mrsListener.updated);
    }

    /*
        We are able to convert fields from openMRS to single string field only for now
        that is why we are checking assertTrue with contains function for changed address
        rather than assertEquals for string field
     */
    @Test
    public void shouldUpdatePerson() throws OpenMRSException, InterruptedException {

        final String newFirstName = "NewFooFirstName";
        final String newMiddleName = "NewFooMiddleName";
        final String newLastName = "NewFooLastName";
        final String newAddress = "NewFooAddress";
        final String newGender = "F";

        person.setFirstName(newFirstName);
        person.setMiddleName(newMiddleName);
        person.setLastName(newLastName);
        person.setAddress(newAddress);
        person.setGender(newGender);

        OpenMRSPerson updated;

        synchronized (lock) {
            assertNotNull(personAdapter.updatePerson(person));
            lock.wait(60000);
        }

        updated = personAdapter.getPersonByUuid(person.getPersonId());

        assertNotNull(updated);
        assertEquals(newFirstName, updated.getFirstName());
        assertEquals(newMiddleName, updated.getMiddleName());
        assertEquals(newLastName, updated.getLastName());
        assertTrue(updated.getAddress().contains(newAddress));
        assertEquals(newGender, updated.getGender());

        assertTrue(mrsListener.created);
        assertTrue(mrsListener.updated);
        assertFalse(mrsListener.deleted);
    }

    @Test
    public void shouldDeletePerson() throws InterruptedException {

        synchronized (lock) {
            personAdapter.deletePerson(person.getPersonId());
            assertNull(personAdapter.getPersonByUuid(person.getPersonId()));

            lock.wait(60000);
        }

        assertTrue(mrsListener.created);
        assertFalse(mrsListener.updated);
        assertTrue(mrsListener.deleted);
    }

    @Test
    public void shouldGetById() throws InterruptedException {

        OpenMRSPerson fetched = personAdapter.getPersonByUuid(person.getPersonId());

        assertNotNull(fetched);
        assertEquals(person.getId(), fetched.getId());
    }

    @After
    public void tearDown() throws InterruptedException {

        deletePerson(person);

        eventListenerRegistry.clearListenersForBean("mrsTestListener");
    }

    private OpenMRSPerson preparePerson() {
        OpenMRSPerson person = new OpenMRSPerson();
        person.setFirstName("John");
        person.setLastName("Smith");
        person.setAddress("10 Fifth Avenue");
        person.setBirthDateEstimated(false);
        person.setGender("M");
        person.setAge(25);
        person.setDead(false);
        person.setMiddleName("John");

        OpenMRSAttribute attr = new OpenMRSAttribute("Birthplace", "Motech");
        List<OpenMRSAttribute> attributes = new ArrayList<>();
        attributes.add(attr);
        person.setAttributes(attributes);

        return person;
    }

    private OpenMRSPerson savePerson(OpenMRSPerson person) throws InterruptedException {

        OpenMRSPerson created;

        synchronized (lock) {
            created = personAdapter.createPerson(person);
            assertNotNull(created);

            lock.wait(60000);
        }

        return created;
    }

    private void deletePerson(OpenMRSPerson person) throws InterruptedException {

        personAdapter.deletePerson(person.getPersonId());
        assertNull(personAdapter.getPersonByUuid(person.getPersonId()));
    }

    public class MrsListener implements EventListener {

        private boolean created = false;
        private boolean updated = false;
        private boolean deleted = false;
        private Map<String, Object> eventParameters;

        public void handle(MotechEvent event) {
            if (event.getSubject().equals(EventKeys.CREATED_NEW_PERSON_SUBJECT)) {
                created = true;
            } else if (event.getSubject().equals(EventKeys.UPDATED_PERSON_SUBJECT)) {
                updated = true;
            } else if (event.getSubject().equals(EventKeys.DELETED_PERSON_SUBJECT)) {
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
