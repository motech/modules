package org.motechproject.commcare.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.commcare.client.CommCareAPIHttpClient;
import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.domain.CommcareLocation;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.util.ConfigsUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CommcareLocationServiceImplTest {

    private CommcareLocationServiceImpl locationService;

    private int pageSize = 5;
    private int pageNumber = 1;

    @Mock
    private CommCareAPIHttpClient commcareHttpClient;

    @Mock
    private CommcareConfigService configService;

    private Config config;

    @Before
    public void setUp() {
        initMocks(this);

        config = ConfigsUtils.prepareConfigOne();

        when(configService.getByName(config.getName())).thenReturn(config);
        when(commcareHttpClient.locationsRequest(config.getAccountConfig(), pageSize, pageNumber)).thenReturn(locationsResponse());
        when(commcareHttpClient.locationRequest(config.getAccountConfig(), "37825")).thenReturn(singleLocationResponse());

        locationService = new CommcareLocationServiceImpl(commcareHttpClient, configService);
    }

    @Test
    public void shouldReturnCorrectListOfLocations() {
        List<CommcareLocation> locations = locationService.getCommcareLocations(pageSize, pageNumber, config.getName());

        assertEquals(pageSize, locations.size());

        List<Long> locationIds = Arrays.asList(37823L, 37824L, 37825L, 37826L, 37827L);
        for (CommcareLocation location : locations) {
            assertTrue(locationIds.contains(location.getId()));
        }
    }

    @Test
    public void shouldReturnCorrectLocationById() {
        CommcareLocation location = locationService.getCommcareLocationById("37825", config.getName());

        assertNotNull(location);

        assertEquals(37825, location.getId());
        assertEquals("2015-04-14T17:53:17.842389", location.getCreatedAt());
        assertEquals(null, location.getExternalId());
        assertEquals("commcare-demo", location.getDomain());
        assertEquals("2015-04-21T18:36:47.992876", location.getLastModified());
        assertEquals(0, location.getLocationData().size());
        assertEquals("https://www.commcarehq.org/a/commcare-demo/api/v0.5/location_type/1130/", location.getLocationType());
    }

    @Test
    public void shouldReturnNullIfLocationDoesNotExist() {
        CommcareLocation location = locationService.getCommcareLocationById("1234567", config.getName());

        assertNull(location);
    }

    private String locationsResponse() {
        return "{\"meta\": {\"limit\": 5, \"next\": null, \"offset\": 0, \"previous\": null, \"total_count\": 15}, \"objects\": [{\"created_at\": \"2015-04-14T17:52:18.157588\", \"domain\": \"commcare-demo\", \"external_id\": null, \"id\": 37823, \"last_modified\": \"2015-04-14T17:52:18.885731\", \"latitude\": null, \"location_data\": {}, \"location_id\": \"3a6bf8822baab254d2cdb642bd3adc43\", \"location_type\": \"https://www.commcarehq.org/a/commcare-demo/api/v0.5/location_type/1128/\", \"longitude\": null, \"name\": \"Ouest\", \"parent\": null, \"resource_uri\": \"https://www.commcarehq.org/a/commcare-demo/api/v0.5/location/37823/\", \"site_code\": \"ouest\"}, {\"created_at\": \"2015-04-14T17:52:49.629206\", \"domain\": \"commcare-demo\", \"external_id\": null, \"id\": 37824, \"last_modified\": \"2015-04-14T17:52:50.117631\", \"latitude\": null, \"location_data\": {}, \"location_id\": \"56f5d374bbc2fda225af20f13395190d\", \"location_type\": \"https://www.commcarehq.org/a/commcare-demo/api/v0.5/location_type/1129/\", \"longitude\": null, \"name\": \"Hospital De Fermathe\", \"parent\": \"https://www.commcarehq.org/a/commcare-demo/api/v0.5/location/37823/\", \"resource_uri\": \"https://www.commcarehq.org/a/commcare-demo/api/v0.5/location/37824/\", \"site_code\": \"hospital_de_fermathe\"}, {\"created_at\": \"2015-04-14T17:53:17.842389\", \"domain\": \"commcare-demo\", \"external_id\": null, \"id\": 37825, \"last_modified\": \"2015-04-21T18:36:47.992876\", \"latitude\": null, \"location_data\": {}, \"location_id\": \"56f5d374bbc2fda225af20f133c4e1a5\", \"location_type\": \"https://www.commcarehq.org/a/commcare-demo/api/v0.5/location_type/1130/\", \"longitude\": null, \"name\": \"Kenscoff\", \"parent\": \"https://www.commcarehq.org/a/commcare-demo/api/v0.5/location/37824/\", \"resource_uri\": \"https://www.commcarehq.org/a/commcare-demo/api/v0.5/location/37825/\", \"site_code\": \"kenscoff\"}, {\"created_at\": \"2015-04-14T17:53:43.649747\", \"domain\": \"commcare-demo\", \"external_id\": null, \"id\": 37826, \"last_modified\": \"2015-04-14T17:53:44.048862\", \"latitude\": null, \"location_data\": {}, \"location_id\": \"3a6bf8822baab254d2cdb642bd3ace9e\", \"location_type\": \"https://www.commcarehq.org/a/commcare-demo/api/v0.5/location_type/1131/\", \"longitude\": null, \"name\": \"Bolothe\", \"parent\": \"https://www.commcarehq.org/a/commcare-demo/api/v0.5/location/37825/\", \"resource_uri\": \"https://www.commcarehq.org/a/commcare-demo/api/v0.5/location/37826/\", \"site_code\": \"bolothe\"}, {\"created_at\": \"2015-04-14T17:54:32.074666\", \"domain\": \"commcare-demo\", \"external_id\": null, \"id\": 37827, \"last_modified\": \"2015-04-14T17:54:32.433648\", \"latitude\": null, \"location_data\": {}, \"location_id\": \"0db36386c60e7ac77bf44c463722a90e\", \"location_type\": \"https://www.commcarehq.org/a/commcare-demo/api/v0.5/location_type/1131/\", \"longitude\": null, \"name\": \"Hospital De Fermathe\", \"parent\": \"https://www.commcarehq.org/a/commcare-demo/api/v0.5/location/37825/\", \"resource_uri\": \"https://www.commcarehq.org/a/commcare-demo/api/v0.5/location/37827/\", \"site_code\": \"hospital_de_fermathe1\"}]}";
    }

    private String singleLocationResponse() {
        return "{\"created_at\": \"2015-04-14T17:53:17.842389\", \"domain\": \"commcare-demo\", \"external_id\": null, \"id\": 37825, \"last_modified\": \"2015-04-21T18:36:47.992876\", \"latitude\": null, \"location_data\": {}, \"location_id\": \"56f5d374bbc2fda225af20f133c4e1a5\", \"location_type\": \"https://www.commcarehq.org/a/commcare-demo/api/v0.5/location_type/1130/\", \"longitude\": null, \"name\": \"Kenscoff\", \"parent\": \"https://www.commcarehq.org/a/commcare-demo/api/v0.5/location/37824/\", \"resource_uri\": \"https://www.commcarehq.org/a/commcare-demo/api/v0.5/location/37825/\", \"site_code\": \"kenscoff\"}";
    }
}
