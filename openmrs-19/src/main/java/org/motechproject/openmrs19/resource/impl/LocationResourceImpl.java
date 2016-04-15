package org.motechproject.openmrs19.resource.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.Validate;
import org.motechproject.openmrs19.config.Config;
import org.motechproject.openmrs19.domain.Concept;
import org.motechproject.openmrs19.domain.Location;
import org.motechproject.openmrs19.domain.LocationListResult;
import org.motechproject.openmrs19.resource.LocationResource;
import org.motechproject.openmrs19.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

@Component
public class LocationResourceImpl extends BaseResource implements LocationResource {

    private static final String GET_LOCATIONS_PATH = "/location?v=full&limit={pageSize}&startIndex={startIndex}";

    @Autowired
    public LocationResourceImpl(RestOperations restOperations) {
        super(restOperations);
    }

    @Override
    public LocationListResult getAllLocations(Config config) {
        String responseJson = getJson(config, "/location?v=full");
        return (LocationListResult) JsonUtils.readJson(responseJson, LocationListResult.class);
    }

    @Override
    public LocationListResult getLocations(Config config, int page, int pageSize) {
        Validate.isTrue(page > 0, "Page number must be a positive value!");
        Validate.isTrue(pageSize > 0, "Page size must be a positive value!");

        String responseJson = getJson(config, GET_LOCATIONS_PATH, pageSize, (page - 1) * pageSize);

        return (LocationListResult) JsonUtils.readJson(responseJson, LocationListResult.class);
    }

    @Override
    public LocationListResult queryForLocationByName(Config config, String locationName) {
        String responseJson = getJson(config, "/location?q={name}&v=full", locationName);
        return (LocationListResult) JsonUtils.readJson(responseJson, LocationListResult.class);
    }

    @Override
    public Location getLocationById(Config config, String uuid) {
        String responseJson = getJson(config, "/location/{uuid}", uuid);
        return (Location) JsonUtils.readJson(responseJson, Location.class);
    }

    @Override
    public Location createLocation(Config config, Location location) {
        String jsonRequest = getGson().toJson(location);
        String jsonResponse = postForJson(config, jsonRequest, "/location");
        return (Location) JsonUtils.readJson(jsonResponse, Location.class);
    }

    @Override
    public Location updateLocation(Config config, Location location) {
        String jsonRequest = getGsonWithAdapters().toJson(location);
        String responseJson = postForJson(config, jsonRequest, "/location/{uuid}", location.getUuid());
        return (Location) JsonUtils.readJson(responseJson, Location.class);
    }

    @Override
    public void deleteLocation(Config config, String uuid) {
        delete(config, "/location/{uuid}?purge", uuid);
    }

    private Gson getGson() {
        return new GsonBuilder().create();
    }

    private Gson getGsonWithAdapters() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(Concept.class, new Concept.ConceptSerializer())
                .create();
    }
}
