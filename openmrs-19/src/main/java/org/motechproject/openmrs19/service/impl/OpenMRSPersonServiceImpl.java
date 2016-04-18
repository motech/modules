package org.motechproject.openmrs19.service.impl;

import org.apache.commons.lang.Validate;
import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.openmrs19.domain.Attribute;
import org.motechproject.openmrs19.domain.Concept;
import org.motechproject.openmrs19.domain.Person;
import org.motechproject.openmrs19.exception.HttpException;
import org.motechproject.openmrs19.exception.OpenMRSException;
import org.motechproject.openmrs19.helper.EventHelper;
import org.motechproject.openmrs19.resource.PersonResource;
import org.motechproject.openmrs19.service.EventKeys;
import org.motechproject.openmrs19.service.OpenMRSPersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service("personService")
public class OpenMRSPersonServiceImpl implements OpenMRSPersonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenMRSPersonServiceImpl.class);

    private final PersonResource personResource;
    private final EventRelay eventRelay;

    @Autowired
    public OpenMRSPersonServiceImpl(PersonResource personResource, EventRelay eventRelay) {
        this.personResource = personResource;
        this.eventRelay = eventRelay;
    }

    @Override
    public Person getPersonByUuid(String uuid) {
        try {
            return personResource.getPersonById(uuid);
        } catch (HttpException e) {
            LOGGER.error("Failed to retrieve person with uuid: " + uuid);
            return null;
        }
    }

    @Override
    public Person createPerson(Person person) {
        Validate.notNull(person, "Person cannot be null");

        try {
            Person saved = personResource.createPerson(person);
            saveAttributesForPerson(saved);
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.CREATED_NEW_PERSON_SUBJECT, EventHelper.personParameters(saved)));

            return saved;
        } catch (HttpException e) {
            throw new OpenMRSException("Failed to create person for: " + person.getDisplay(), e);
        }
    }

    @Override
    public Person createPerson(String givenName, String familyName, DateTime birthDate,
                               String gender, String address, List<Attribute> attributes)  {
        Person person = new Person();

        person.setBirthdate(birthDate.toDate());
        person.setGender(gender);
        person.setAttributes(attributes);

        Person.Name name = new Person.Name();
        name.setGivenName(givenName);
        name.setFamilyName(familyName);
        person.setNames(Collections.singletonList(name));

        Person.Address personAddress = new Person.Address();
        personAddress.setAddress1(address);
        person.setAddresses(Collections.singletonList(personAddress));

        return createPerson(person);
    }

    @Override
    public Person updatePerson(Person person) {
        try {
            Person updated = personResource.updatePerson(person);
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.UPDATED_PERSON_SUBJECT, EventHelper.personParameters(updated)));

            return updated;
        } catch (HttpException e) {
            throw new OpenMRSException("Failed to update a person in OpenMRS with id: " + person.getUuid(), e);
        }
    }

    public void savePersonCauseOfDeath(String patientId, Date deathDate, Concept causeOfDeath) {
        Person person = new Person();
        person.setUuid(patientId);
        person.setDead(true);
        person.setDeathDate(deathDate);
        person.setCauseOfDeath(causeOfDeath);

        try {
            personResource.updatePerson(person);
        } catch (HttpException e) {
            throw new OpenMRSException("Failed to save cause of death observation for patient id: " + patientId, e);
        }
    }

    @Override
    public void deletePerson(String uuid) {
        try {
            personResource.deletePerson(uuid);
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.DELETED_PERSON_SUBJECT, EventHelper.personParameters(new Person(uuid))));
        } catch (HttpException e) {
            throw new OpenMRSException("Failed to remove person with UUID: " + uuid, e);
        }
    }

    public void saveAttributesForPerson(Person person) {
        for (Attribute attribute : person.getAttributes()) {
            try {
                personResource.createPersonAttribute(person.getUuid(), attribute);
            } catch (HttpException e) {
                LOGGER.warn("Unable to add attribute to person with id: " + person.getUuid());
            }
        }
    }

    public void deleteAllAttributes(Person person) {
        Person saved;
        try {
            saved = personResource.getPersonById(person.getUuid());
        } catch (HttpException e) {
            throw new OpenMRSException("Failed to retrieve person when deleting attributes with uuid: " + person.getUuid(), e);
        }

        for (Attribute attribute : saved.getAttributes()) {
            try {
                personResource.deleteAttribute(person.getUuid(), attribute);
            } catch (HttpException e) {
                LOGGER.warn("Failed to delete attribute with name: " + attribute.getName());
            }
        }
    }
}
