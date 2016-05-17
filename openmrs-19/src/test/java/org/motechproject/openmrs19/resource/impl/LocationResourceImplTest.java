package org.motechproject.openmrs19.resource.impl;

import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.motechproject.openmrs19.domain.Location;
import org.motechproject.openmrs19.domain.LocationListResult;
import org.mockito.Captor;
import org.mockito.Mock;
import org.motechproject.openmrs19.config.Config;
import org.motechproject.openmrs19.config.ConfigDummyData;
import org.motechproject.openmrs19.util.JsonUtils;
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

public class LocationResourceImplTest extends AbstractResourceImplTest {

    private static final String LOCATION_LIST_RESPONSE_JSON = "json/location/location-list-response.json";
    private static final String LOCATION_RESPONSE_JSON = "json/location/location-response.json";
    private static final String CREATE_LOCATION_JSON = "json/location/location-create.json";

    @Mock
    private RestOperations restOperations;

    @Captor
    private ArgumentCaptor<HttpEntity<String>> requestCaptor;

    private LocationResourceImpl locationResource;

    private Config config;

    @Before
    public void setUp() {
        initMocks(this);
        locationResource = new LocationResourceImpl(restOperations);
        config = ConfigDummyData.prepareConfig("one");
    }

    @Test
    public void shouldCreateLocation() throws Exception {
        Location location = prepareLocation();
        URI uri = config.toInstancePath("/location");

        when(restOperations.exchange(eq(uri), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponse(LOCATION_RESPONSE_JSON));

        Location created = locationResource.createLocation(config, location);

        verify(restOperations).exchange(eq(uri), eq(HttpMethod.POST), requestCaptor.capture(), eq(String.class));

        assertThat(created, equalTo(location));
        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForPost(config)));
        assertThat(JsonUtils.readJson(requestCaptor.getValue().getBody(), JsonObject.class),
                equalTo(readFromFile(CREATE_LOCATION_JSON, JsonObject.class)));
    }

    @Test
    public void shouldUpdateLocation() throws Exception {
        Location location = prepareLocation();
        URI url = config.toInstancePathWithParams("/location/{uuid}", location.getUuid());

        when(restOperations.exchange(eq(url), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponse(LOCATION_RESPONSE_JSON));

        Location updated = locationResource.updateLocation(config, location);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.POST), requestCaptor.capture(), eq(String.class));

        assertThat(updated, equalTo(location));
        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForPost(config)));
        assertThat(JsonUtils.readJson(requestCaptor.getValue().getBody(), JsonObject.class),
                equalTo(readFromFile(CREATE_LOCATION_JSON, JsonObject.class)));
    }

    @Test
    public void shouldGetAllLocations() throws Exception {
        URI url = config.toInstancePath("/location?v=full");

        when(restOperations.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponse(LOCATION_LIST_RESPONSE_JSON));

        LocationListResult result = locationResource.getAllLocations(config);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.GET), requestCaptor.capture(), eq(String.class));

        assertThat(result, equalTo(readFromFile(LOCATION_LIST_RESPONSE_JSON, LocationListResult.class)));
        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForGet(config)));
        assertThat(requestCaptor.getValue().getBody(), nullValue());
    }

    @Test
    public void shouldGetLocationById() throws Exception {
        String locationId = "LLL";
        URI url = config.toInstancePathWithParams("/location/{uuid}", locationId);

        when(restOperations.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponse(LOCATION_RESPONSE_JSON));

        Location location = locationResource.getLocationById(config, locationId);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.GET), requestCaptor.capture(), eq(String.class));

        assertThat(location, equalTo(readFromFile(LOCATION_RESPONSE_JSON, Location.class)));
        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForGet(config)));
        assertThat(requestCaptor.getValue().getBody(), nullValue());
    }

    @Test
    public void shouldQueryForLocationByName() throws Exception {
        String query = "Test";
        URI url = config.toInstancePathWithParams("/location?q={name}&v=full", query);

        when(restOperations.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponse(LOCATION_LIST_RESPONSE_JSON));

        LocationListResult result = locationResource.queryForLocationByName(config, query);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.GET), requestCaptor.capture(), eq(String.class));

        assertThat(result, equalTo(readFromFile(LOCATION_LIST_RESPONSE_JSON, LocationListResult.class)));
        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForGet(config)));
        assertThat(requestCaptor.getValue().getBody(), nullValue());
    }

    private Location prepareLocation() throws Exception {
        return (Location) readFromFile(LOCATION_RESPONSE_JSON, Location.class);
    }
}
