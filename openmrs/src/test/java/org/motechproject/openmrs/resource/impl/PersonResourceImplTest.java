package org.motechproject.openmrs.resource.impl;

import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.config.ConfigDummyData;
import org.motechproject.openmrs.domain.Attribute;
import org.motechproject.openmrs.domain.AttributeTypeListResult;
import org.motechproject.openmrs.domain.Person;
import org.motechproject.openmrs.util.JsonUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestOperations;

import java.net.URI;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PersonResourceImplTest extends AbstractResourceImplTest {

    private static final String PERSON_ATTRIBUTE_TYPE_RESPONSE_JSON = "json/person/person-attribute-type-response.json";
    private static final String CREATE_PERSON_ATTRIBUTE_JSON = "json/person/person-attribute-create.json";
    private static final String PERSON_NAME_UPDATE_JSON = "json/person/person-name-update.json";
    private static final String PERSON_RESPONSE_JSON = "json/person/person-response.json";
    private static final String CREATE_PERSON_JSON = "json/person/person-create.json";
    private static final String UPDATE_PERSON_ADDRESS_JSON = "json/person/person-address-update.json";

    @Mock
    private RestOperations restOperations;

    @Captor
    private ArgumentCaptor<HttpEntity<String>> requestCaptor;

    private PersonResourceImpl personResource;

    private Config config;

    @Before
    public void setUp() {
        initMocks(this);
        personResource = new PersonResourceImpl(restOperations);
        config = ConfigDummyData.prepareConfig("one");
    }


    @Test
    public void shouldCreatePerson() throws Exception {
        Person person = preparePerson();
        URI url = config.toInstancePath("/person");

        when(restOperations.exchange(eq(url), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponse(PERSON_RESPONSE_JSON));

        Person created = personResource.createPerson(config, person);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.POST), requestCaptor.capture(), eq(String.class));

        assertThat(created, equalTo(person));
        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForPost(config)));
        assertThat(JsonUtils.readJson(requestCaptor.getValue().getBody(), JsonObject.class),
                equalTo(readFromFile(CREATE_PERSON_JSON, JsonObject.class)));
    }

    @Test
    public void shouldUpdatePerson() throws Exception {
        Person person = preparePerson();
        URI url = config.toInstancePathWithParams("/person/{uuid}?v=full", person.getUuid());

        when(restOperations.exchange(eq(url), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponse(PERSON_RESPONSE_JSON));

        Person updated = personResource.updatePerson(config, person);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.POST), requestCaptor.capture(), eq(String.class));

        assertThat(updated, equalTo(preparePerson()));
        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForPost(config)));
        assertThat(JsonUtils.readJson(requestCaptor.getValue().getBody(), JsonObject.class),
                equalTo(readFromFile(CREATE_PERSON_JSON, JsonObject.class)));
    }

    @Test
    public void shouldGetPersonById() throws Exception {
        String personId = "PPP";
        URI url = config.toInstancePathWithParams("/person/{uuid}?v=full", personId);

        when(restOperations.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponse(PERSON_RESPONSE_JSON));

        Person person = personResource.getPersonById(config, personId);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.GET), requestCaptor.capture(), eq(String.class));

        assertThat(person, equalTo(preparePerson()));
        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForGet(config)));
        assertThat(requestCaptor.getValue().getBody(), nullValue());
    }

    @Test
    public void shouldCreatePersonAttribute() throws Exception {
        String personId = "PPP";
        Attribute attribute = prepareAttribute();
        URI url = config.toInstancePathWithParams("/person/{uuid}/attribute", personId);

        personResource.createPersonAttribute(config, personId, attribute);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.POST), requestCaptor.capture(), eq(String.class));

        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForPostWithoutResponse(config)));
        assertThat(JsonUtils.readJson(requestCaptor.getValue().getBody(), JsonObject.class),
                equalTo(readFromFile(CREATE_PERSON_ATTRIBUTE_JSON, JsonObject.class)));
    }

    @Test
    public void shouldQueryPersonAttributeTypeByName() throws Exception {
        String attributeTypeName = "Citizenship";
        URI url = config.toInstancePathWithParams("/personattributetype?q={name}", attributeTypeName);

        when(restOperations.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponse(PERSON_ATTRIBUTE_TYPE_RESPONSE_JSON));

        AttributeTypeListResult result = personResource.queryPersonAttributeTypeByName(config, attributeTypeName);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.GET), requestCaptor.capture(), eq(String.class));

        assertThat(result, equalTo(readFromFile(PERSON_ATTRIBUTE_TYPE_RESPONSE_JSON, AttributeTypeListResult.class)));
        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForGet(config)));
        assertThat(requestCaptor.getValue().getBody(), nullValue());
    }

    @Test
    public void shouldUpdatePersonName() throws Exception {
        String personId = "CCC";
        Person.Name name = prepareName();
        URI url = config.toInstancePathWithParams("/person/{personUuid}/name/{nameUuid}", personId, name.getUuid());

        personResource.updatePersonName(config, personId, name);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.POST), requestCaptor.capture(), eq(String.class));

        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForPostWithoutResponse(config)));
        assertThat(JsonUtils.readJson(requestCaptor.getValue().getBody(), JsonObject.class),
                equalTo(readFromFile(PERSON_NAME_UPDATE_JSON, JsonObject.class)));
    }

    @Test
    public void shouldUpdatePersonAddress() throws Exception {
        String personId = "CCC";
        Person.Address preferredAddress = prepareAddress();
        URI url = config.toInstancePathWithParams("/person/{personUuid}/address/{addressUuid}", personId,
                preferredAddress.getUuid());

        personResource.updatePersonAddress(config, personId, preferredAddress);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.POST), requestCaptor.capture(), eq(String.class));

        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForPostWithoutResponse(config)));
        assertThat(JsonUtils.readJson(requestCaptor.getValue().getBody(), JsonObject.class),
                equalTo(readFromFile(UPDATE_PERSON_ADDRESS_JSON, JsonObject.class)));
    }

    private Person preparePerson() throws Exception {
        return (Person) readFromFile(PERSON_RESPONSE_JSON, Person.class);
    }

    private Attribute prepareAttribute() {
        Attribute attribute = new Attribute();
        attribute.setValue("Motech");

        Attribute.AttributeType attributeType = new Attribute.AttributeType();
        attributeType.setUuid("AAA");
        attribute.setAttributeType(attributeType);

        return attribute;
    }

    private Person.Name prepareName() {
        Person.Name name = new Person.Name();
        name.setUuid("AAA");
        name.setGivenName("Motech");
        name.setMiddleName("E");
        name.setGivenName("Test");
        return name;
    }
    private Person.Address prepareAddress() throws Exception {
        return  (Person.Address) readFromFile(UPDATE_PERSON_ADDRESS_JSON, Person.Address.class);
    }
}
