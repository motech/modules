package org.motechproject.openmrs19.resource.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.motechproject.openmrs19.config.Config;
import org.motechproject.openmrs19.domain.Attribute;
import org.motechproject.openmrs19.domain.AttributeTypeListResult;
import org.motechproject.openmrs19.domain.Concept;
import org.motechproject.openmrs19.domain.Person;
import org.motechproject.openmrs19.resource.PersonResource;
import org.motechproject.openmrs19.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

@Component
public class PersonResourceImpl extends BaseResource implements PersonResource {

    @Autowired
    public PersonResourceImpl(RestOperations restOperations) {
        super(restOperations);
    }

    @Override
    public Person getPersonById(Config config, String uuid) {
        String responseJson = getJson(config, "/person/{uuid}?v=full", uuid);
        return (Person) JsonUtils.readJson(responseJson, Person.class);
    }

    @Override
    public Person createPerson(Config config, Person person) {
        String requestJson = buildGsonWithConceptAdapter().toJson(person);
        String responseJson = postForJson(config, requestJson, "/person");
        return (Person) JsonUtils.readJson(responseJson, Person.class);
    }

    @Override
    public void createPersonAttribute(Config config, String personUuid, Attribute attribute) {
        String requestJson = buildGsonWithAttributeTypeAdapter().toJson(attribute);
        postWithEmptyResponseBody(config, requestJson, "/person/{uuid}/attribute", personUuid);
    }

    @Override
    public AttributeTypeListResult queryPersonAttributeTypeByName(Config config, String name) {
        String responseJson = getJson(config, "/personattributetype?q={name}", name);
        return (AttributeTypeListResult) JsonUtils.readJson(responseJson, AttributeTypeListResult.class);
    }

    @Override
    public void deleteAttribute(Config config, String uuid, Attribute attribute) {
        delete(config, "/person/{parentUuid}/attribute/{uuid}?purge", uuid, attribute.getUuid());
    }

    @Override
    public Person updatePerson(Config config, Person person) {
        String requestJson = buildGsonWithUpdatePersonAdapter().toJson(person);
        String responseJson = postForJson(config, requestJson, "/person/{uuid}?v=full", person.getUuid());
        return (Person) JsonUtils.readJson(responseJson, Person.class);
    }

    @Override
    public void updatePersonName(Config config, String uuid, Person.Name name) {
        String requestJson = buildGson().toJson(name);
        postWithEmptyResponseBody(config, requestJson, "/person/{personUuid}/name/{nameUuid}", uuid, name.getUuid());
    }

    @Override
    public void updatePersonAddress(Config config, String uuid, Person.Address address) {
        String requestJson = buildGson().toJson(address);
        postWithEmptyResponseBody(config, requestJson, "/person/{personUuid}/address/{addressUuid}", uuid, address.getUuid());
    }

    @Override
    public void deletePerson(Config config, String personUuid) {
        delete(config, "/person/{uuid}?purge", personUuid);
    }

    private Gson buildGsonWithConceptAdapter() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .registerTypeAdapter(Concept.class, new Concept.ConceptSerializer())
                .create();
    }

    private Gson buildGsonWithUpdatePersonAdapter() {
        return new GsonBuilder()
                .registerTypeAdapter(Person.class, new Person.PersonUpdateSerializer())
                .create();
    }

    private Gson buildGsonWithAttributeTypeAdapter() {
        return new GsonBuilder()
                .registerTypeAdapter(Attribute.AttributeType.class, new Attribute.AttributeTypeSerializer())
                .create();
    }

    private Gson buildGson() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .create();
    }
}
