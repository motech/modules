package org.motechproject.openmrs19.service.impl;

import org.apache.commons.lang.Validate;
import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.openmrs19.config.Config;
import org.motechproject.openmrs19.domain.Attribute;
import org.motechproject.openmrs19.domain.Person;
import org.motechproject.openmrs19.exception.OpenMRSException;
import org.motechproject.openmrs19.helper.EventHelper;
import org.motechproject.openmrs19.resource.PersonResource;
import org.motechproject.openmrs19.service.EventKeys;
import org.motechproject.openmrs19.service.OpenMRSConfigService;
import org.motechproject.openmrs19.service.OpenMRSPersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;
import java.util.List;

@Service("personService")
public class OpenMRSPersonServiceImpl implements OpenMRSPersonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenMRSPersonServiceImpl.class);

    private final OpenMRSConfigService configService;

    private final PersonResource personResource;

    private final EventRelay eventRelay;

    @Autowired
    public OpenMRSPersonServiceImpl(PersonResource personResource, EventRelay eventRelay,
                                    OpenMRSConfigService configService) {
        this.personResource = personResource;
        this.eventRelay = eventRelay;
        this.configService = configService;
    }

    @Override
    public Person createPerson(String configName, Person person) {
        Validate.notNull(person, "Person cannot be null");

        try {
            Config config = configService.getConfigByName(configName);
            Person saved = personResource.createPerson(config, person);
            saveAttributesForPerson(config, saved);
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.CREATED_NEW_PERSON_SUBJECT, EventHelper.personParameters(saved)));

            return saved;
        } catch (HttpClientErrorException e) {
            throw new OpenMRSException("Failed to create person for: " + person.getDisplay(), e);
        }
    }

    @Override
    public Person getPersonByUuid(String configName, String uuid) {
        try {
            Config config = configService.getConfigByName(configName);
            return personResource.getPersonById(config, uuid);
        } catch (HttpClientErrorException e) {
            LOGGER.error("Failed to retrieve person with uuid: " + uuid);
            return null;
        }
    }

    @Override
    public Person createPerson(String configName, String givenName, String familyName, DateTime birthDate,
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

        return createPerson(configName, person);
    }

    @Override
    public Person updatePerson(String configName, Person person) {
        Validate.notEmpty(person.getUuid(), "Person uuid cannot be null");
        try {
            Config config = configService.getConfigByName(configName);

            Person fetchedPerson = personResource.getPersonById(config, person.getUuid());

            //Updating address and name of person must be done separately.
            Person.Address addressForUpdate = fetchedPerson.getPreferredAddress();
            if (addressForUpdate != null) {
                person.getPreferredAddress().setUuid(addressForUpdate.getUuid());
            }
            personResource.updatePersonAddress(config, person.getUuid(), person.getPreferredAddress());

            Person.Name nameForUpdate = fetchedPerson.getPreferredName();
            person.getPreferredName().setUuid(nameForUpdate.getUuid());
            personResource.updatePersonName(config, person.getUuid(), person.getPreferredName());

            Person updated = personResource.updatePerson(config, person);
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.UPDATED_PERSON_SUBJECT, EventHelper.personParameters(updated)));

            return updated;
        } catch (HttpClientErrorException e) {
            throw new OpenMRSException("Failed to update a person in OpenMRS with id: " + person.getUuid(), e);
        }
    }

    @Override
    public void deletePerson(String configName, String uuid) {
        try {
            Config config = configService.getConfigByName(configName);
            personResource.deletePerson(config, uuid);
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.DELETED_PERSON_SUBJECT, EventHelper.personParameters(new Person(uuid))));
        } catch (HttpClientErrorException e) {
            throw new OpenMRSException("Failed to remove person with UUID: " + uuid, e);
        }
    }

    private void saveAttributesForPerson(Config config, Person person) {
        for (Attribute attribute : person.getAttributes()) {
            try {
                personResource.createPersonAttribute(config, person.getUuid(), attribute);
            } catch (HttpClientErrorException e) {
                LOGGER.warn("Unable to add attribute to person with id: " + person.getUuid());
            }
        }
    }
}
