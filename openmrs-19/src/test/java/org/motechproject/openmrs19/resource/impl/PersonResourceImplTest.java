package org.motechproject.openmrs19.resource.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.motechproject.openmrs19.exception.HttpException;
import org.motechproject.openmrs19.resource.model.Attribute;
import org.motechproject.openmrs19.resource.model.Attribute.AttributeType;
import org.motechproject.openmrs19.resource.model.AttributeTypeListResult;
import org.motechproject.openmrs19.resource.model.Person;
import org.motechproject.openmrs19.resource.model.Person.PreferredAddress;
import org.motechproject.openmrs19.resource.model.Person.PreferredName;

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
    public void shouldGetAllAdressFields() throws HttpException, IOException {
        Mockito.when(getClient().getJson(Mockito.any(URI.class)))
                .thenReturn(readJsonFromFile("json/person-response.json"));

        String address1 = "5 Main St.";
        String address2 = "5/4";
        String cityVillage = "Utopia";
        String stateProvince = "testProvince";
        String country = "Neverland";
        String postalCode = "69-111";
        String countyDistrict = null;
        String address3 = null;
        String address4 = null;
        String address5 = null;
        String address6 = null;
        String startDate = "2016-03-01T00:00:00.000+0000";
        String endDate = null;
        String latitude = "47.613879";
        String longitude = "-122.342436";

        Person person = impl.getPersonById("PPP");

        assertEquals(address1, person.getPreferredAddress().getAddress1());
        assertEquals(address2, person.getPreferredAddress().getAddress2());
        assertEquals(cityVillage, person.getPreferredAddress().getCityVillage());
        assertEquals(stateProvince, person.getPreferredAddress().getStateProvince());
        assertEquals(country, person.getPreferredAddress().getCountry());
        assertEquals(postalCode, person.getPreferredAddress().getPostalCode());
        assertEquals(countyDistrict, person.getPreferredAddress().getCountyDistrict());
        assertEquals(address3, person.getPreferredAddress().getAddress3());
        assertEquals(address4, person.getPreferredAddress().getAddress4());
        assertEquals(address5, person.getPreferredAddress().getAddress5());
        assertEquals(address6, person.getPreferredAddress().getAddress6());
        assertEquals(startDate, person.getPreferredAddress().getStartDate());
        assertEquals(endDate, person.getPreferredAddress().getEndDate());
        assertEquals(latitude, person.getPreferredAddress().getLatitude());
        assertEquals(longitude, person.getPreferredAddress().getLongitude());
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

        PreferredName name = new PreferredName();
        name.setGivenName("John");
        name.setMiddleName("E");
        name.setFamilyName("Doe");
        person.setNames(asList(name));

        PreferredAddress addr = new PreferredAddress();
        addr.setAddress1("5 Main St");
        person.setAddresses(asList(addr));
        return person;
    }

    @Test
    public void shouldCreateAttribute() throws HttpException, IOException {
        AttributeType at = new AttributeType();
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

        assertEquals(asList("8d871afc-c2cc-11de-8d13-0010c6dffd0f"), extract(result.getResults(), on(AttributeType.class).getUuid()));
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
        PreferredName name = new PreferredName();
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
        PreferredAddress addr = new PreferredAddress();
        addr.setAddress1("Test");
        addr.setUuid("AAA");

        impl.updatePersonAddress("CCC", addr);

        ArgumentCaptor<String> sentJson = ArgumentCaptor.forClass(String.class);
        Mockito.verify(getClient()).postWithEmptyResponseBody(Mockito.any(URI.class), sentJson.capture());

        String expectedJson = "{\"address1\":\"Test\"}";
        PreferredAddress expectedObj = getGson().fromJson(expectedJson, PreferredAddress.class);
        PreferredAddress sentObject = getGson().fromJson(sentJson.getValue(), PreferredAddress.class);

        assertEquals(expectedObj, sentObject);
    }
}
