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
    private Person responsePerson;

    @Before
    public void initialize() throws InterruptedException, ParseException {
        mrsListener = new MrsListener();
        eventListenerRegistry.registerListener(mrsListener, Arrays.asList(EventKeys.CREATED_NEW_PERSON_SUBJECT,
            EventKeys.UPDATED_PERSON_SUBJECT, EventKeys.DELETED_PERSON_SUBJECT));

        person = preparePerson();
        responsePerson = savePerson(person);
    }

    @Test
    public void shouldCreatePerson() {

        assertNotNull(responsePerson.getUuid());
        assertEquals(mrsListener.eventParameters.get(EventKeys.PERSON_ID), responsePerson.getUuid());
        assertEquals(mrsListener.eventParameters.get(EventKeys.PERSON_GIVEN_NAME), responsePerson.getPreferredName().getGivenName());
        assertEquals(mrsListener.eventParameters.get(EventKeys.PERSON_FAMILY_NAME), responsePerson.getPreferredName().getFamilyName());

        assertTrue(mrsListener.created);
        assertFalse(mrsListener.deleted);
        assertFalse(mrsListener.updated);
    }

    @Test
    public void shouldCreatePersonWithFields() {
        Person createdPerson = personAdapter.getPersonByUuid(responsePerson.getUuid());

        assertEquals(person.getPreferredName().getGivenName(), createdPerson.getPreferredName().getGivenName());
        assertEquals(person.getPreferredName().getMiddleName(), createdPerson.getPreferredName().getMiddleName());
        assertEquals(person.getPreferredName().getFamilyName(), createdPerson.getPreferredName().getFamilyName());

        Person.Address personAddress = person.getPreferredAddress();
        Person.Address createdPersonAddress = createdPerson.getPreferredAddress();

        assertEquals(personAddress.getAddress1(), createdPersonAddress.getAddress1());
        assertEquals(personAddress.getAddress2(), createdPersonAddress.getAddress2());
        assertEquals(personAddress.getAddress3(), createdPersonAddress.getAddress3());
        assertEquals(personAddress.getAddress4(), createdPersonAddress.getAddress4());
        assertEquals(personAddress.getAddress5(), createdPersonAddress.getAddress5());
        assertEquals(personAddress.getAddress6(), createdPersonAddress.getAddress6());
        assertEquals(personAddress.getCityVillage(), createdPersonAddress.getCityVillage());
        assertEquals(personAddress.getStateProvince(), createdPersonAddress.getStateProvince());
        assertEquals(personAddress.getCountry(), createdPersonAddress.getCountry());
        assertEquals(personAddress.getCountyDistrict(), createdPersonAddress.getCountyDistrict());
        assertEquals(personAddress.getPostalCode(), createdPersonAddress.getPostalCode());
        assertEquals(personAddress.getLatitude(), createdPersonAddress.getLatitude());
        assertEquals(personAddress.getLongitude(), createdPersonAddress.getLongitude());
        assertEquals(personAddress.getStartDate(), createdPersonAddress.getStartDate());
        assertEquals(personAddress.getEndDate(), createdPersonAddress.getEndDate());
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
        responsePerson.setNames(Collections.singletonList(name));

        Person.Address address = new Person.Address();
        address.setAddress1(newAddress);
        responsePerson.setAddresses(Collections.singletonList(address));

        responsePerson.setGender(newGender);

        Person updated;

        synchronized (lock) {
            assertNotNull(personAdapter.updatePerson(responsePerson));
            lock.wait(60000);
        }

        updated = personAdapter.getPersonByUuid(responsePerson.getUuid());

        assertNotNull(updated);
        assertEquals(newFirstName, updated.getPreferredName().getGivenName());
        assertEquals(newMiddleName, updated.getPreferredName().getMiddleName());
        assertEquals(newLastName, updated.getPreferredName().getFamilyName());
        // So far OpenMRS module stores only one field of responsePerson's address, which is 'address1'.
        // However while retrieving responsePerson from OpenMRS server all responsePerson's address fields are put
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
            personAdapter.deletePerson(responsePerson.getUuid());
            assertNull(personAdapter.getPersonByUuid(responsePerson.getUuid()));

            lock.wait(60000);
        }

        assertTrue(mrsListener.created);
        assertFalse(mrsListener.updated);
        assertTrue(mrsListener.deleted);
    }

    @Test
    public void shouldGetById() throws InterruptedException {

        Person fetched = personAdapter.getPersonByUuid(responsePerson.getUuid());

        assertNotNull(fetched);
        assertEquals(responsePerson.getUuid(), fetched.getUuid());
    }

    @After
    public void tearDown() throws InterruptedException {

        deletePerson(responsePerson);

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
            created = personAdapter.createPerson(person);
            assertNotNull(created);

            lock.wait(60000);
        }

        return created;
    }

    private void deletePerson(Person person) throws InterruptedException {

        personAdapter.deletePerson(person.getUuid());
        assertNull(personAdapter.getPersonByUuid(person.getUuid()));
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
