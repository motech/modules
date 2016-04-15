package org.motechproject.openmrs19.resource.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.motechproject.openmrs19.domain.Location;
import org.motechproject.openmrs19.domain.LocationListResult;
import org.motechproject.openmrs19.exception.HttpException;

import java.io.IOException;
import java.net.URI;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LocationResourceImplTest extends AbstractResourceImplTest {

    private LocationResourceImpl impl;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        impl = new LocationResourceImpl(getClient(), getInstance());
    }

    @Test
    public void shouldCreateLocation() throws IOException, HttpException {
        Location loc = buildLocation();
        impl.createLocation(loc);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(getClient()).postForJson(Mockito.any(URI.class), captor.capture());

        String expectedJson = readJsonFromFile("json/location-create.json");

        Location expectedObj = getGson().fromJson(expectedJson, Location.class);
        Location sentObject = getGson().fromJson(captor.getValue(), Location.class);

        assertEquals(expectedObj.getAddress6(), sentObject.getAddress6());
        assertEquals(expectedObj.getName(), sentObject.getName());
        assertEquals(expectedObj.getStateProvince(), sentObject.getStateProvince());
        assertEquals(expectedObj.getCountry(), sentObject.getCountry());
        assertEquals(expectedObj.getCountyDistrict(), sentObject.getCountyDistrict());
        assertEquals(expectedObj.getDescription(), sentObject.getDescription());
    }

    @Test
    public void shouldUpdateLocation() throws IOException, HttpException {
        Location loc = buildLocation();
        impl.updateLocation(loc);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(getClient()).postForJson(Mockito.any(URI.class), captor.capture());

        String expectedJson = readJsonFromFile("json/location-create.json");

        Location expectedObj = getGson().fromJson(expectedJson, Location.class);
        Location sentObject = getGson().fromJson(captor.getValue(), Location.class);

        assertEquals(expectedObj.getAddress6(), sentObject.getAddress6());
        assertEquals(expectedObj.getName(), sentObject.getName());
        assertEquals(expectedObj.getStateProvince(), sentObject.getStateProvince());
        assertEquals(expectedObj.getCountry(), sentObject.getCountry());
        assertEquals(expectedObj.getCountyDistrict(), sentObject.getCountyDistrict());
        assertEquals(expectedObj.getDescription(), sentObject.getDescription());
    }

    private Location buildLocation() {
        Location loc = new Location();
        loc.setName("Location Name");
        loc.setStateProvince("Location State");
        loc.setCountry("Location Country");
        loc.setCountyDistrict("Location District");
        loc.setAddress6("Region");
        loc.setDescription("Location Name");

        return loc;
    }

    @Test
    public void shouldParseAllLocations() throws HttpException, IOException {
        Mockito.when(getClient().getJson(Mockito.any(URI.class))).thenReturn(
                readJsonFromFile("json/location-list-response.json"));

        LocationListResult result = impl.getAllLocations();

        assertEquals(asList("AAABBBCCC"), extract(result.getResults(), on(Location.class).getUuid()));
    }

    @Test
    public void shouldQueryForLocationByName() throws HttpException, IOException {
        Mockito.when(getClient().getJson(Mockito.any(URI.class))).thenReturn(
                readJsonFromFile("json/location-list-response.json"));

        LocationListResult result = impl.queryForLocationByName("Test");

        assertEquals(asList("AAABBBCCC"), extract(result.getResults(), on(Location.class).getUuid()));
    }

    @Test
    public void shouldParseSingleLocation() throws HttpException, IOException {
        Mockito.when(getClient().getJson(Mockito.any(URI.class))).thenReturn(
                readJsonFromFile("json/location-create.json"));

        Location location = impl.getLocationById("LLL");

        assertNotNull(location);
    }

}
