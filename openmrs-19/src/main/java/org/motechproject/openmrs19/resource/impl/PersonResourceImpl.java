package org.motechproject.openmrs19.resource.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.motechproject.openmrs19.OpenMrsInstance;
import org.motechproject.openmrs19.exception.HttpException;
import org.motechproject.openmrs19.resource.PersonResource;
import org.motechproject.openmrs19.resource.model.Attribute;
import org.motechproject.openmrs19.resource.model.Attribute.AttributeType;
import org.motechproject.openmrs19.resource.model.Attribute.AttributeTypeSerializer;
import org.motechproject.openmrs19.resource.model.AttributeTypeListResult;
import org.motechproject.openmrs19.resource.model.Concept;
import org.motechproject.openmrs19.resource.model.Person;
import org.motechproject.openmrs19.resource.model.Person.PreferredAddress;
import org.motechproject.openmrs19.resource.model.Person.PreferredName;
import org.motechproject.openmrs19.rest.RestClient;
import org.motechproject.openmrs19.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersonResourceImpl implements PersonResource {

    private final RestClient restClient;
    private final OpenMrsInstance openmrsInstance;

    @Autowired
    public PersonResourceImpl(RestClient restClient, OpenMrsInstance openmrsInstance) {
        this.restClient = restClient;
        this.openmrsInstance = openmrsInstance;
    }

    @Override
    public Person getPersonById(String uuid) throws HttpException {
        String responseJson = null;
        responseJson = restClient.getJson(openmrsInstance.toInstancePathWithParams("/person/{uuid}?v=full", uuid));
        return (Person) JsonUtils.readJson(responseJson, Person.class);
    }

    @Override
    public Person createPerson(Person person) throws HttpException {
        String requestJson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create().toJson(person);
        String responseJson;
        responseJson = restClient.postForJson(openmrsInstance.toInstancePath("/person"), requestJson);

        return (Person) JsonUtils.readJson(responseJson, Person.class);
    }

    @Override
    public void createPersonAttribute(String personUuid, Attribute attribute) throws HttpException {
        Gson gson = new GsonBuilder().registerTypeAdapter(AttributeType.class, new AttributeTypeSerializer()).create();
        String requestJson = gson.toJson(attribute);

        restClient.postForJson(openmrsInstance.toInstancePathWithParams("/person/{uuid}/attribute", personUuid),
                requestJson);
    }

    @Override
    public AttributeTypeListResult queryPersonAttributeTypeByName(String name) throws HttpException {
        String responseJson = null;
        responseJson = restClient.getJson(openmrsInstance.toInstancePathWithParams("/personattributetype?q={name}",
                name));

        AttributeTypeListResult result = (AttributeTypeListResult) JsonUtils.readJson(responseJson,
                AttributeTypeListResult.class);

        return result;
    }

    @Override
    public void deleteAttribute(String uuid, Attribute attribute) throws HttpException {
        String attributeUuid = attribute.getUuid();

        restClient.delete(openmrsInstance.toInstancePathWithParams("/person/{parentUuid}/attribute/{uuid}?purge",
                uuid, attributeUuid));

    }

    @Override
    public Person updatePerson(Person person) throws HttpException {
        Gson gson = new GsonBuilder().registerTypeAdapter(Concept.class, new Concept.ConceptUuidSerializer())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();
        // uuid cannot be set on an update call
        String personUuid = person.getUuid();
        person.setUuid(null);

        String requestJson = gson.toJson(person);
        String responseJson = restClient.postForJson(openmrsInstance.toInstancePathWithParams("/person/{uuid}?v=full", personUuid),
                requestJson);

        return (Person) JsonUtils.readJson(responseJson, Person.class);
    }

    @Override
    public void updatePersonName(String uuid, PreferredName name) throws HttpException {
        Gson gson = new GsonBuilder().create();
        // setting uuid and display to null so they are not included in request
        String nameUuid = name.getUuid();
        name.setDisplay(null);
        name.setUuid(null);

        String requestJson = gson.toJson(name);
        restClient.postWithEmptyResponseBody(
                openmrsInstance.toInstancePathWithParams("/person/{personUuid}/name/{nameUuid}", uuid, nameUuid),
                requestJson);
    }

    @Override
    public void updatePersonAddress(String uuid, PreferredAddress address) throws HttpException {
        Gson gson = new GsonBuilder().create();
        // setting uuid to null so it is not included in request
        String addrUuid = address.getUuid();
        address.setUuid(null);

        String requestJson = gson.toJson(address);
        restClient.postWithEmptyResponseBody(
                openmrsInstance.toInstancePathWithParams("/person/{personUuid}/address/{addressUuid}", uuid, addrUuid),
                requestJson);
    }

    @Override
    public void deletePerson(String personUuid) throws HttpException {
        restClient.delete(openmrsInstance.toInstancePathWithParams("/person/{uuid}?purge", personUuid));
    }
}
