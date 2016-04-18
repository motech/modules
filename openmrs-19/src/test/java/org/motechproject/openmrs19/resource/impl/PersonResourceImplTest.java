package org.motechproject.openmrs19.resource.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.motechproject.openmrs19.domain.Attribute;
import org.motechproject.openmrs19.domain.AttributeTypeListResult;
import org.motechproject.openmrs19.domain.Person;
import org.motechproject.openmrs19.exception.HttpException;

import java.io.IOException;
import java.net.URI;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PersonResourceImplTest extends AbstractResourceImplTest {
    private PersonResourceImpl impl;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        impl = new PersonResourceImpl(getClient(), getInstance());
    }

    @Test
    public void shouldGetPersonById() throws HttpException, IOException {
        Mockito.when(getClient().getJson(Mockito.any(URI.class)))
                .thenReturn(readJsonFromFile("json/person-response.json"));

        Person person = impl.getPersonById("PPP");

        assertNotNull(person);
    }

    @Test
    public void shouldGetAllAddressFields() throws HttpException, IOException {
        Mockito.when(getClient().getJson(Mockito.any(URI.class)))
                .thenReturn(readJsonFromFile("json/person-response.json"));

        Person person = impl.getPersonById("PPP");
        String fullAddressFields = "5 Main St.,5/4,Utopia,testProvince,Neverland,69-111,null,null,null,null,null,2016-03-01T00:00:00.000+0000,null,47.613879,-122.342436";

        assertEquals(fullAddressFields, person.getPreferredAddress().getFullAddressString());
    }

    @Test
    public void shouldCreatePerson() throws HttpException, IOException {
        Person person = buildPerson();
        impl.createPerson(person);

        ArgumentCaptor<String> sentJson = ArgumentCaptor.forClass(String.class);
        Mockito.verify(getClient()).postForJson(Mockito.any(URI.class), sentJson.capture());
        String expectedJson = readJsonFromFile("json/person-create.json");

        Person expectedObj = getGson().fromJson(expectedJson, Person.class);
        Person sentObject = getGson().fromJson(sentJson.getValue(), Person.class);

        assertEquals(expectedObj.getGender(), sentObject.getGender());
        assertEquals(expectedObj.getNames(), sentObject.getNames());
        assertEquals(expectedObj.getAddresses(), sentObject.getAddresses());
    }

    @Test
    public void shouldUpdatePerson() throws HttpException, IOException {
        Person person = buildPerson();
        impl.updatePerson(person);

        ArgumentCaptor<String> sentJson = ArgumentCaptor.forClass(String.class);
        Mockito.verify(getClient()).postForJson(Mockito.any(URI.class), sentJson.capture());
        String expectedJson = readJsonFromFile("json/person-create.json");

        Person expectedObj = getGson().fromJson(expectedJson, Person.class);
        Person sentObject = getGson().fromJson(sentJson.getValue(), Person.class);

        assertEquals(expectedObj.getGender(), sentObject.getGender());
        assertEquals(expectedObj.getNames(), sentObject.getNames());
        assertEquals(expectedObj.getAddresses(), sentObject.getAddresses());
    }

    private Person buildPerson() {
        Person person = new Person();
        person.setGender("M");

        Person.Name name = new Person.Name();
        name.setGivenName("John");
        name.setMiddleName("E");
        name.setFamilyName("Doe");
        person.setNames(asList(name));

        Person.Address addr = new Person.Address();
        addr.setAddress1("5 Main St");
        person.setAddresses(asList(addr));
        return person;
    }

    @Test
    public void shouldCreateAttribute() throws HttpException, IOException {
        Attribute.AttributeType at = new Attribute.AttributeType();
        at.setUuid("AAA");
        Attribute attr = new Attribute();
        attr.setValue("Motech");
        attr.setAttributeType(at);

        impl.createPersonAttribute("PPP", attr);

        ArgumentCaptor<String> sentJson = ArgumentCaptor.forClass(String.class);
        Mockito.verify(getClient()).postForJson(Mockito.any(URI.class), sentJson.capture());

        String expectedJson = readJsonFromFile("json/person-attribute-create.json");
        assertEquals(stringToJsonElement(expectedJson), stringToJsonElement(sentJson.getValue()));
    }

    @Test
    public void shouldGetAttributeTypeByName() throws HttpException, IOException {
        Mockito.when(getClient().getJson(Mockito.any(URI.class))).thenReturn(
                readJsonFromFile("json/person-attribute-type-response.json"));
        AttributeTypeListResult result = impl.queryPersonAttributeTypeByName("Citizenship");

        assertEquals(asList("8d871afc-c2cc-11de-8d13-0010c6dffd0f"), extract(result.getResults(), on(Attribute.AttributeType.class).getUuid()));
    }

    @Test
    public void shouldNotIncludeUuidOnPersonUpdate() throws HttpException, IOException {
        Person person = new Person();
        person.setUuid("AAA");
        person.setGender("F");

        impl.updatePerson(person);

        ArgumentCaptor<String> sentJson = ArgumentCaptor.forClass(String.class);
        Mockito.verify(getClient()).postForJson(Mockito.any(URI.class), sentJson.capture());

        String expectedJson = readJsonFromFile("json/person-update.json");
        Person expectedObj = getGson().fromJson(expectedJson, Person.class);
        Person sentObject = getGson().fromJson(sentJson.getValue(), Person.class);

        assertEquals(expectedObj.getGender(), sentObject.getGender());
        assertEquals(expectedObj.getNames(), sentObject.getNames());
        assertEquals(expectedObj.getAddresses(), sentObject.getAddresses());
    }

    @Test
    public void shouldNotIncludeUuidInPersonNameUpdate() throws HttpException, IOException {
        Person.Name name = new Person.Name();
        name.setUuid("AAA");
        name.setGivenName("Motech");
        name.setMiddleName("E");
        name.setGivenName("Test");

        impl.updatePersonName("CCC", name);

        ArgumentCaptor<String> sentJson = ArgumentCaptor.forClass(String.class);
        Mockito.verify(getClient()).postWithEmptyResponseBody(Mockito.any(URI.class), sentJson.capture());

        String expectedJson = readJsonFromFile("json/person-name-update.json");
        Person expectedObj = getGson().fromJson(expectedJson, Person.class);
        Person sentObject = getGson().fromJson(sentJson.getValue(), Person.class);

        assertEquals(expectedObj.getGender(), sentObject.getGender());
        assertEquals(expectedObj.getNames(), sentObject.getNames());
        assertEquals(expectedObj.getAddresses(), sentObject.getAddresses());
    }

    @Test
    public void shouldNotIncludeUuidInPersonAddressUpdate() throws HttpException, IOException {
        Person.Address addr = new Person.Address();
        addr.setAddress1("Test");
        addr.setUuid("AAA");

        impl.updatePersonAddress("CCC", addr);

        ArgumentCaptor<String> sentJson = ArgumentCaptor.forClass(String.class);
        Mockito.verify(getClient()).postWithEmptyResponseBody(Mockito.any(URI.class), sentJson.capture());

        String expectedJson = "{\"address1\":\"Test\"}";
        Person.Address expectedObj = getGson().fromJson(expectedJson, Person.Address.class);
        Person.Address sentObject = getGson().fromJson(sentJson.getValue(), Person.Address.class);

        assertEquals(expectedObj, sentObject);
    }
}
