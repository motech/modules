package org.motechproject.openmrs19.it;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventListener;
import org.motechproject.event.listener.EventListenerRegistryService;
import org.motechproject.openmrs19.domain.Person;
import org.motechproject.openmrs19.exception.OpenMRSException;
import org.motechproject.openmrs19.service.EventKeys;
import org.motechproject.openmrs19.service.OpenMRSPersonService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.motechproject.openmrs19.util.TestConstants.DEFAULT_CONFIG_NAME;

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

    private Person person;

    @Before
    public void initialize() throws InterruptedException {
        mrsListener = new MrsListener();
        eventListenerRegistry.registerListener(mrsListener, Arrays.asList(EventKeys.CREATED_NEW_PERSON_SUBJECT,
            EventKeys.UPDATED_PERSON_SUBJECT, EventKeys.DELETED_PERSON_SUBJECT));

        person = savePerson(preparePerson());
    }

    @Test
    public void shouldCreatePerson() {

        assertNotNull(person.getUuid());
        assertEquals(mrsListener.eventParameters.get(EventKeys.PERSON_ID), person.getUuid());
        assertEquals(mrsListener.eventParameters.get(EventKeys.PERSON_GIVEN_NAME), person.getPreferredName().getGivenName());
        assertEquals(mrsListener.eventParameters.get(EventKeys.PERSON_FAMILY_NAME), person.getPreferredName().getFamilyName());

        assertTrue(mrsListener.created);
        assertFalse(mrsListener.deleted);
        assertFalse(mrsListener.updated);
    }

    @Test
    public void shouldUpdatePerson() throws OpenMRSException, InterruptedException {

        final String newFirstName = "NewFooFirstName";
        final String newMiddleName = "NewFooMiddleName";
        final String newLastName = "NewFooLastName";
        final String newAddress = "NewFooAddress";
        final String newGender = "F";

        Person.Name name = new Person.Name();
        name.setGivenName(newFirstName);
        name.setMiddleName(newMiddleName);
        name.setFamilyName(newLastName);
        person.setNames(Collections.singletonList(name));

        Person.Address address = new Person.Address();
        address.setAddress1(newAddress);
        person.setAddresses(Collections.singletonList(address));

        person.setGender(newGender);

        Person updated;

        synchronized (lock) {
            assertNotNull(personAdapter.updatePerson(DEFAULT_CONFIG_NAME, person));
            lock.wait(60000);
        }

        updated = personAdapter.getPersonByUuid(DEFAULT_CONFIG_NAME, person.getUuid());

        assertNotNull(updated);
        assertEquals(newFirstName, updated.getPreferredName().getGivenName());
        assertEquals(newMiddleName, updated.getPreferredName().getMiddleName());
        assertEquals(newLastName, updated.getPreferredName().getFamilyName());
        // So far OpenMRS module stores only one field of person's address, which is 'address1'.
        // However while retrieving person from OpenMRS server all person's address fields are put
        // into one string. That's why it is checked if address field contains address1 value.
        assertTrue(updated.getPreferredAddress().getFullAddressString().contains(newAddress));
        assertEquals(newGender, updated.getGender());

        assertTrue(mrsListener.created);
        assertTrue(mrsListener.updated);
        assertFalse(mrsListener.deleted);
    }

    @Test
    public void shouldDeletePerson() throws InterruptedException {

        synchronized (lock) {
            personAdapter.deletePerson(DEFAULT_CONFIG_NAME, person.getUuid());
            assertNull(personAdapter.getPersonByUuid(DEFAULT_CONFIG_NAME, person.getUuid()));

            lock.wait(60000);
        }

        assertTrue(mrsListener.created);
        assertFalse(mrsListener.updated);
        assertTrue(mrsListener.deleted);
    }

    @Test
    public void shouldGetById() throws InterruptedException {

        Person fetched = personAdapter.getPersonByUuid(DEFAULT_CONFIG_NAME, person.getUuid());

        assertNotNull(fetched);
        assertEquals(person.getUuid(), fetched.getUuid());
    }

    @After
    public void tearDown() throws InterruptedException {

        deletePerson(person);

        eventListenerRegistry.clearListenersForBean("mrsTestListener");
    }

    private Person preparePerson() {
        Person person = new Person();

        Person.Name name = new Person.Name();
        name.setGivenName("John");
        name.setMiddleName("John");
        name.setFamilyName("Smith");
        person.setNames(Collections.singletonList(name));

        Person.Address address = new Person.Address();
        address.setAddress1("10 Fifth Avenue");
        person.setAddresses(Collections.singletonList(address));

        person.setGender("M");
        person.setAge(25);
        person.setDead(false);

        return person;
    }

    private Person savePerson(Person person) throws InterruptedException {

        Person created;

        synchronized (lock) {
            created = personAdapter.createPerson(DEFAULT_CONFIG_NAME, person);
            assertNotNull(created);

            lock.wait(60000);
        }

        return created;
    }

    private void deletePerson(Person person) throws InterruptedException {

        personAdapter.deletePerson(DEFAULT_CONFIG_NAME, person.getUuid());
        assertNull(personAdapter.getPersonByUuid(DEFAULT_CONFIG_NAME, person.getUuid()));
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
