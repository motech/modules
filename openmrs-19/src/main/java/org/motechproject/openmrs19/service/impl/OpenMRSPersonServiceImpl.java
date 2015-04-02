package org.motechproject.openmrs19.service.impl;

import org.apache.commons.lang.Validate;
import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.openmrs19.domain.OpenMRSAttribute;
import org.motechproject.openmrs19.domain.OpenMRSPerson;
import org.motechproject.openmrs19.exception.HttpException;
import org.motechproject.openmrs19.exception.OpenMRSException;
import org.motechproject.openmrs19.helper.EventHelper;
import org.motechproject.openmrs19.resource.PersonResource;
import org.motechproject.openmrs19.resource.model.Attribute;
import org.motechproject.openmrs19.resource.model.Attribute.AttributeType;
import org.motechproject.openmrs19.resource.model.AttributeTypeListResult;
import org.motechproject.openmrs19.resource.model.Concept;
import org.motechproject.openmrs19.resource.model.Person;
import org.motechproject.openmrs19.service.EventKeys;
import org.motechproject.openmrs19.service.OpenMRSPersonService;
import org.motechproject.openmrs19.util.ConverterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("personService")
public class OpenMRSPersonServiceImpl implements OpenMRSPersonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenMRSPersonServiceImpl.class);

    private final Map<String, String> attributeTypeUuidCache = new HashMap<>();

    private final PersonResource personResource;
    private final EventRelay eventRelay;

    @Autowired
    public OpenMRSPersonServiceImpl(PersonResource personResource, EventRelay eventRelay) {
        this.personResource = personResource;
        this.eventRelay = eventRelay;
    }

    @Override
    public OpenMRSPerson getByUuid(String uuid) {

        try {
            return ConverterUtils.toOpenMRSPerson(personResource.getPersonById(uuid));
        } catch (HttpException e) {
            LOGGER.error("Failed to retrieve person with uuid: " + uuid);
            return null;
        }
    }

    @Override
    public OpenMRSPerson createPerson(OpenMRSPerson openMRSPerson) {

        Validate.notNull(openMRSPerson, "Person cannot be null");

        Person converted = ConverterUtils.toPerson(openMRSPerson, true);

        try {
            OpenMRSPerson saved;

            saved = ConverterUtils.toOpenMRSPerson(personResource.createPerson(converted));
            saveAttributesForPerson(saved);
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.CREATED_NEW_PERSON_SUBJECT, EventHelper.personParameters(saved)));

            return saved;

        } catch (HttpException e) {
            throw new OpenMRSException("Failed to create person for: " + openMRSPerson.getFullName(), e);
        }
    }

    @Override
    public OpenMRSPerson addPerson(String firstName, String lastName, DateTime dateOfBirth,
                                   String gender, String address, List<OpenMRSAttribute> attributes)  {

        OpenMRSPerson person = new OpenMRSPerson();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setDateOfBirth(dateOfBirth);
        person.setGender(gender);
        person.setAddress(address);
        person.setAttributes(ConverterUtils.createAttributeList(attributes));

        return createPerson(person);
    }

    @Override
    public OpenMRSPerson updatePerson(OpenMRSPerson openMRSPerson) {

        Person converted = ConverterUtils.toPerson(openMRSPerson, true);

        try {
            OpenMRSPerson updated = ConverterUtils.toOpenMRSPerson(personResource.updatePerson(converted));
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.UPDATED_PERSON_SUBJECT, EventHelper.personParameters(updated)));

            return updated;

        } catch (HttpException e) {
            throw new OpenMRSException("Failed to update a person in OpenMRS with id: " + openMRSPerson.getPersonId(), e);
        }
    }

    public void savePersonCauseOfDeath(String patientId, Date dateOfDeath, Concept causeOfDeath) {
        Person person = new Person();
        person.setUuid(patientId);
        person.setDead(true);
        person.setDeathDate(dateOfDeath);
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
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.DELETED_PERSON_SUBJECT, EventHelper.personParameters(new OpenMRSPerson(uuid))));
        } catch (HttpException e) {
            throw new OpenMRSException("Failed to remove person with UUID: " + uuid, e);
        }
    }

    public void saveAttributesForPerson(OpenMRSPerson person) {
        for (OpenMRSAttribute attribute : person.getAttributes()) {
            Attribute attr = new Attribute();
            attr.setValue(attribute.getValue());
            attr.setAttributeType(getAttributeTypeUuid(attribute.getName()));

            try {
                personResource.createPersonAttribute(person.getPersonId(), attr);
            } catch (HttpException e) {
                LOGGER.warn("Unable to add attribute to person with id: " + person.getPersonId());
            }
        }
    }

    public void deleteAllAttributes(OpenMRSPerson person) {
        Person saved;
        try {
            saved = personResource.getPersonById(person.getPersonId());
        } catch (HttpException e) {
            throw new OpenMRSException("Failed to retrieve person when deleting attributes with uuid: " + person.getPersonId(), e);
        }

        List<Attribute> attributes = saved.getAttributes();
        for (Attribute attr : attributes) {
            try {
                personResource.deleteAttribute(person.getPersonId(), attr);
            } catch (HttpException e) {
                LOGGER.warn("Failed to delete attribute with name: " + attr.getName());
            }
        }
    }

    private AttributeType getAttributeTypeUuid(String name) {
        if (!attributeTypeUuidCache.containsKey(name)) {
            AttributeTypeListResult result;
            try {
                result = personResource.queryPersonAttributeTypeByName(name);
            } catch (HttpException e) {
                throw new OpenMRSException("HTTP request failed to get attribute type uuid for attribute with name: " + name, e);
            }

            if (result.getResults().size() == 0) {
                throw new OpenMRSException("No attribute with name: " + name + " found in OpenMRS");
            } else if (result.getResults().size() > 1) {
                LOGGER.warn("Found more than 1 attribute with name: " + name);
            }
            attributeTypeUuidCache.put(name, result.getResults().get(0).getUuid());
        }

        AttributeType type = new AttributeType();
        type.setUuid(attributeTypeUuidCache.get(name));

        return type;
    }
}
