package org.motechproject.openmrs.resource.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.httpclient.HttpClient;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.Attribute;
import org.motechproject.openmrs.domain.AttributeListResult;
import org.motechproject.openmrs.domain.AttributeTypeListResult;
import org.motechproject.openmrs.domain.Concept;
import org.motechproject.openmrs.domain.Person;
import org.motechproject.openmrs.resource.PersonResource;
import org.motechproject.openmrs.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PersonResourceImpl extends BaseResource implements PersonResource {

    @Autowired
    public PersonResourceImpl(RestOperations restOperations, HttpClient httpClient) {
        super(restOperations, httpClient);
    }

    @Override
    public Person getPersonById(Config config, String uuid) {
        String responseJson = getJson(config, "/person/{uuid}?v=full", uuid);
        return (Person) JsonUtils.readJsonWithAdapters(responseJson, Person.class, createAttributeAdapter());
    }

    @Override
    public Person createPerson(Config config, Person person) {
        String requestJson = buildGsonWithConceptAndAttributeAdapter().toJson(person);
        String responseJson = postForJson(config, requestJson, "/person");
        return (Person) JsonUtils.readJsonWithAdapters(responseJson, Person.class, createAttributeAdapter());
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
    public AttributeListResult queryPersonAttributeByPersonUuid(Config config, String uuid) {
        String responseJson = getJson(config, "/person/{uuid}/attribute", uuid);
        return (AttributeListResult) JsonUtils.readJsonWithAdapters(responseJson, AttributeListResult.class, createAttributeAdapter());
    }

    @Override
    public Attribute.AttributeType queryPersonAttributeTypeByUuid(Config config, String uuid) {
        String responseJson = getJson(config, "/personattributetype/{uuid}", uuid);
        return (Attribute.AttributeType) JsonUtils.readJson(responseJson, Attribute.AttributeType.class);
    }

    @Override
    public void deleteAttribute(Config config, String uuid, Attribute attribute) {
        delete(config, "/person/{parentUuid}/attribute/{uuid}?purge", uuid, attribute.getUuid());
    }

    @Override
    public Person updatePerson(Config config, Person person) {
        String requestJson = buildGsonWithUpdatePersonAdapter().toJson(person);
        String responseJson = postForJson(config, requestJson, "/person/{uuid}?v=full", person.getUuid());
        return (Person) JsonUtils.readJsonWithAdapters(responseJson, Person.class, createAttributeAdapter());
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
    public void updatePersonAttribute(Config config, String uuid, Attribute attribute) {
        String requestJson = buildGsonWithAttributeTypeAdapter().toJson(attribute);
        postWithEmptyResponseBody(config, requestJson, "/person/{parentUuid}/attribute/{attributeUuid}", uuid,
                attribute.getUuid());
    }

    @Override
    public void deletePerson(Config config, String personUuid) {
        delete(config, "/person/{uuid}?purge", personUuid);
    }

    @Override
    public void checkPersonAttributeTypes(Config config, Person person) {
        List<Attribute> checkedAttributes = new ArrayList<>();

        if (!person.getAttributes().isEmpty()) {
            for (Attribute attribute : person.getAttributes()) {
                Attribute.AttributeType attributeType = queryPersonAttributeTypeByUuid(config,
                        attribute.getAttributeType().getUuid());
                if (!attributeType.getFormat().contains("java.lang")) {
                    Attribute changedAttribute = new Attribute();
                    changedAttribute.setHydratedObject(attribute.getValue());
                    changedAttribute.setAttributeType(attribute.getAttributeType());

                    checkedAttributes.add(changedAttribute);
                } else {
                    checkedAttributes.add(attribute);
                }
            }
            person.setAttributes(checkedAttributes);
        }
    }

    private Gson buildGsonWithConceptAndAttributeAdapter() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .registerTypeAdapter(Concept.class, new Concept.ConceptSerializer())
                .registerTypeAdapter(Attribute.class, new Attribute.AttributeSerializer())
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

    private Map<Type, Object> createAttributeAdapter() {
        Map<Type, Object> attributeAdapter = new HashMap<>();
        attributeAdapter.put(Attribute.class, new Attribute.AttributeSerializer());

        return attributeAdapter;
    }
}
