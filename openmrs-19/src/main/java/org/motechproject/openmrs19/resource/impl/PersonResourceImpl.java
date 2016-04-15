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
        String requestJson = buildGsonForAttribute().toJson(attribute);
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
        String requestJson = buildGsonWithConceptAdapter().toJson(person);
        String responseJson = postForJson(config, requestJson, "/person/{uuid}?v=full", person.getUuid());
        return (Person) JsonUtils.readJson(responseJson, Person.class);
    }

    @Override
    public void updatePersonName(Config config, String uuid, Person.Name name) {
        String requestJson = buildGsonWithPreferredNameAdapter().toJson(name);
        postWithEmptyResponseBody(config, requestJson, "/person/{personUuid}/name/{nameUuid}", uuid, name.getUuid());
    }

    @Override
    public void updatePersonAddress(Config config, String uuid, Person.Address address) {
        String requestJson = buildGsonWithPreferredAddressAdapter().toJson(address);
        postWithEmptyResponseBody(config, requestJson, "/person/{personUuid}/address/{addressUuid}", uuid, address.getUuid());
    }

    @Override
    public void deletePerson(Config config, String personUuid) {
        delete(config, "/person/{uuid}?purge", personUuid);
    }

    private Gson buildGsonWithConceptAdapter() {
        GsonBuilder builder = new GsonBuilder();

        builder.excludeFieldsWithoutExposeAnnotation();
        builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        builder.registerTypeAdapter(Concept.class, new Concept.ConceptSerializer());

        return builder.create();
    }

    private Gson buildGsonForAttribute() {
        return new GsonBuilder()
                .registerTypeAdapter(Attribute.AttributeType.class, new Attribute.AttributeTypeSerializer())
                .create();
    }

    private Gson buildGsonWithPreferredNameAdapter() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }

    private Gson buildGsonWithPreferredAddressAdapter() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }
}
