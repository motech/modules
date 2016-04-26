package org.motechproject.openmrs19.it;

import org.joda.time.DateTime;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
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
    private Person createdPerson;

    @Before
    public void initialize() throws InterruptedException, ParseException {
        mrsListener = new MrsListener();
        eventListenerRegistry.registerListener(mrsListener, Arrays.asList(EventKeys.CREATED_NEW_PERSON_SUBJECT,
            EventKeys.UPDATED_PERSON_SUBJECT, EventKeys.DELETED_PERSON_SUBJECT));

        person = preparePerson();
        createdPerson = savePerson(person);
    }

    @Test
    public void shouldCreatePerson() {

        assertNotNull(createdPerson.getUuid());
        assertEquals(mrsListener.eventParameters.get(EventKeys.PERSON_ID), createdPerson.getUuid());
        assertEquals(mrsListener.eventParameters.get(EventKeys.PERSON_GIVEN_NAME), createdPerson.getPreferredName().getGivenName());
        assertEquals(mrsListener.eventParameters.get(EventKeys.PERSON_FAMILY_NAME), createdPerson.getPreferredName().getFamilyName());

        assertTrue(mrsListener.created);
        assertFalse(mrsListener.deleted);
        assertFalse(mrsListener.updated);
    }

    @Test
    public void shouldCreatePersonWithFields() {
        Person fetchedPerson = personAdapter.getPersonByUuid(DEFAULT_CONFIG_NAME, createdPerson.getUuid());

        assertEquals(person.getPreferredName().getGivenName(), fetchedPerson.getPreferredName().getGivenName());
        assertEquals(person.getPreferredName().getMiddleName(), fetchedPerson.getPreferredName().getMiddleName());
        assertEquals(person.getPreferredName().getFamilyName(), fetchedPerson.getPreferredName().getFamilyName());

        assertEquals(person.getPreferredAddress(), fetchedPerson.getPreferredAddress());
    }

    @Test
    public void shouldUpdatePerson() throws OpenMRSException, InterruptedException {

        final String newFirstName = "NewFooFirstName";
        final String newMiddleName = "NewFooMiddleName";
        final String newLastName = "NewFooLastName";
        final String newAddress1 = "NewFooAddress1";
        final String newAddress2 = "NewFooAddress2";
        final String newAddress3 = "NewFooAddress3";
        final String newAddress4 = "NewFooAddress4";
        final String newAddress5 = "NewFooAddress5";
        final String newAddress6 = "NewFooAddress6";
        final String newCityVillage = "NewCityVillage";
        final String newCountry = "NewCountry";
        final String newCountyDistrict = "NewCountyDistrict";
        final String newLatitude = "11.324";
        final String newLongitude = "33.333";
        final String newStateProvince = "NewStateProvince";
        final String newPostalCode = "NewPostalCode";
        final DateTime newStartDate = new DateTime("2010-01-16T00:00:00Z");
        final DateTime newEndDate = new DateTime("2013-01-16T00:00:00Z");
        final DateTime newBirthdate = new DateTime("1999-01-16T00:00:00Z");
        final Boolean newBirthdateEstimated = false;
        final String newGender = "F";
        final Boolean newDead = false;

        Person.Name name = new Person.Name();
        name.setGivenName(newFirstName);
        name.setMiddleName(newMiddleName);
        name.setFamilyName(newLastName);
        createdPerson.setPreferredName(name);

        Person.Address address = new Person.Address(newAddress1, newAddress2, newAddress3, newAddress4, newAddress5, newAddress6,
                newCityVillage, newStateProvince, newCountry, newPostalCode, newCountyDistrict,
                newLatitude, newLongitude, newStartDate.toDate(), newEndDate.toDate());
        createdPerson.setPreferredAddress(address);

        createdPerson.setGender(newGender);
        createdPerson.setBirthdate(newBirthdate.toDate());
        createdPerson.setBirthdateEstimated(newBirthdateEstimated);
        createdPerson.setDead(newDead);

        Person updated;

        synchronized (lock) {
            assertNotNull(personAdapter.updatePerson(DEFAULT_CONFIG_NAME, createdPerson));
            lock.wait(60000);
        }

        updated = personAdapter.getPersonByUuid(DEFAULT_CONFIG_NAME, createdPerson.getUuid());

        assertNotNull(updated);
        assertEquals(newFirstName, updated.getPreferredName().getGivenName());
        assertEquals(newMiddleName, updated.getPreferredName().getMiddleName());
        assertEquals(newLastName, updated.getPreferredName().getFamilyName());

        // So far OpenMRS module stores only one field of createdPerson's address, which is 'address1'.
        // However while retrieving createdPerson from OpenMRS server all createdPerson's address fields are put
        // into one string. That's why it is checked if address field contains new fields value.
        assertTrue(updated.getPreferredAddress().getFullAddressString().contains(newAddress1));
        assertTrue(updated.getPreferredAddress().getFullAddressString().contains(newAddress2));
        assertTrue(updated.getPreferredAddress().getFullAddressString().contains(newAddress3));
        assertTrue(updated.getPreferredAddress().getFullAddressString().contains(newAddress4));
        assertTrue(updated.getPreferredAddress().getFullAddressString().contains(newAddress5));
        assertTrue(updated.getPreferredAddress().getFullAddressString().contains(newAddress6));
        assertTrue(updated.getPreferredAddress().getFullAddressString().contains(newCityVillage));
        assertTrue(updated.getPreferredAddress().getFullAddressString().contains(newCountry));
        assertTrue(updated.getPreferredAddress().getFullAddressString().contains(newCountyDistrict));
        assertTrue(updated.getPreferredAddress().getFullAddressString().contains(newLatitude));
        assertTrue(updated.getPreferredAddress().getFullAddressString().contains(newLongitude));
        assertTrue(updated.getPreferredAddress().getFullAddressString().contains(newStateProvince));
        assertTrue(updated.getPreferredAddress().getFullAddressString().contains(newPostalCode));
        assertTrue(updated.getPreferredAddress().getFullAddressString().contains(newStartDate.toDate().toString()));
        assertTrue(updated.getPreferredAddress().getFullAddressString().contains(newEndDate.toDate().toString()));

        assertEquals(newGender, updated.getGender());
        assertEquals(newBirthdate.toDate(), updated.getBirthdate());
        assertEquals(newBirthdateEstimated, updated.getBirthdateEstimated());
        assertEquals(newDead, updated.getDead());
    }

    @Test
    public void shouldDeletePerson() throws InterruptedException {

        synchronized (lock) {
            personAdapter.deletePerson(DEFAULT_CONFIG_NAME, createdPerson.getUuid());
            assertNull(personAdapter.getPersonByUuid(DEFAULT_CONFIG_NAME, createdPerson.getUuid()));

            lock.wait(60000);
        }

        assertTrue(mrsListener.created);
        assertFalse(mrsListener.updated);
        assertTrue(mrsListener.deleted);
    }

    @Test
    public void shouldGetById() throws InterruptedException {

        Person fetched = personAdapter.getPersonByUuid(DEFAULT_CONFIG_NAME, createdPerson.getUuid());

        assertNotNull(fetched);
        assertEquals(createdPerson.getUuid(), fetched.getUuid());
    }

    @After
    public void tearDown() throws InterruptedException {

        deletePerson(createdPerson);

        eventListenerRegistry.clearListenersForBean("mrsTestListener");
    }

    private Person preparePerson() throws ParseException {
        Person person = new Person();

        Person.Name name = new Person.Name();
        name.setGivenName("John");
        name.setMiddleName("John");
        name.setFamilyName("Smith");
        person.setPreferredName(name);
        person.setNames(Collections.singletonList(name));

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date startDate = dateFormat.parse("2016-03-01T00:00:00.000+0000");
        Date endDate = dateFormat.parse("2100-03-01T00:00:00.000+0000");

        Person.Address address = new Person.Address("10 Fifth Avenue", "line 2", "line 3", "line 4", "line 5", "line 6",
                "BestCity", "Gondor", "Middle-earth", "00-000", "Ithilien", "30.00", "76.234", startDate, endDate);
        person.setPreferredAddress(address);
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
