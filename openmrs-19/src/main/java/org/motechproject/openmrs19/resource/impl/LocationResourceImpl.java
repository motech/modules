package org.motechproject.openmrs19.resource.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.Validate;
import org.motechproject.openmrs19.OpenMrsInstance;
import org.motechproject.openmrs19.domain.Location;
import org.motechproject.openmrs19.domain.LocationListResult;
import org.motechproject.openmrs19.exception.HttpException;
import org.motechproject.openmrs19.resource.LocationResource;
import org.motechproject.openmrs19.rest.RestClient;
import org.motechproject.openmrs19.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LocationResourceImpl implements LocationResource {

    private final RestClient restClient;
    private final OpenMrsInstance openmrsInstance;

    @Autowired
    public LocationResourceImpl(RestClient restClient, OpenMrsInstance openmrsInstance) {
        this.restClient = restClient;
        this.openmrsInstance = openmrsInstance;
    }

    @Override
    public LocationListResult getAllLocations() throws HttpException {
        String json = restClient.getJson(openmrsInstance.toInstancePath("/location?v=full"));
        return (LocationListResult) JsonUtils.readJson(json, LocationListResult.class);
    }

    @Override
    public LocationListResult getLocations(int page, int pageSize) throws HttpException {
        Validate.isTrue(page > 0, "Page number must be a positive value!");
        Validate.isTrue(pageSize > 0, "Page size must be a positive value!");

        int startIndex = (page - 1) * pageSize;
        String json = restClient.getJson(openmrsInstance
                .toInstancePathWithParams("/location?v=full&limit={pageSize}&startIndex={startIndex}", pageSize, startIndex));
        return (LocationListResult) JsonUtils.readJson(json, LocationListResult.class);
    }

    @Override
    public LocationListResult queryForLocationByName(String locationName) throws HttpException {
        String json = restClient.getJson(openmrsInstance.toInstancePathWithParams("/location?q={name}&v=full",
                locationName));
        return (LocationListResult) JsonUtils.readJson(json, LocationListResult.class);

    }

    @Override
    public Location getLocationById(String uuid) throws HttpException {
        String json = restClient.getJson(openmrsInstance.toInstancePathWithParams("/location/{uuid}", uuid));
        return (Location) JsonUtils.readJson(json, Location.class);
    }

    @Override
    public Location createLocation(Location location) throws HttpException {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String jsonRequest = gson.toJson(location);
        String jsonResponse = restClient.postForJson(openmrsInstance.toInstancePath("/location"), jsonRequest);
        return (Location) JsonUtils.readJson(jsonResponse, Location.class);
    }

    @Override
    public Location updateLocation(Location location) throws HttpException {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        String jsonRequest = gson.toJson(location);
        String responseJson = restClient.postForJson(openmrsInstance.toInstancePathWithParams("/location/{uuid}", location.getUuid()),
                jsonRequest);
        return (Location) JsonUtils.readJson(responseJson, Location.class);
    }

    @Override
    public void deleteLocation(String uuid) throws HttpException {
        restClient.delete(openmrsInstance.toInstancePathWithParams("/location/{uuid}?purge", uuid));
    }
}
