package org.motechproject.openmrs19.service.impl;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.openmrs19.domain.Location;
import org.motechproject.openmrs19.exception.HttpException;
import org.motechproject.openmrs19.helper.EventHelper;
import org.motechproject.openmrs19.resource.LocationResource;
import org.motechproject.openmrs19.service.EventKeys;
import org.motechproject.openmrs19.service.OpenMRSLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component("locationService")
public class OpenMRSLocationServiceImpl implements OpenMRSLocationService {
    private static final Logger LOGGER = Logger.getLogger(OpenMRSLocationServiceImpl.class);

    private final LocationResource locationResource;
    private final EventRelay eventRelay;

    @Autowired
    public OpenMRSLocationServiceImpl(LocationResource locationResource, EventRelay eventRelay) {
        this.locationResource = locationResource;
        this.eventRelay = eventRelay;
    }

    @Override
    public List<? extends Location> getAllLocations() {

        List<Location> locations;

        try {
            locations = locationResource.getAllLocations().getResults();
        } catch (HttpException e) {
            LOGGER.error("Failed to retrieve all locations");
            return Collections.emptyList();
        }

        return locations;
    }

    @Override
    public List<? extends Location> getLocations(int page, int pageSize) {

        List<Location> locations;

        try {
            locations = locationResource.getLocations(page, pageSize).getResults();
        } catch (HttpException e) {
            LOGGER.error("Failed to retrieve locations");
            return Collections.emptyList();
        }

        return locations;
    }

    @Override
    public List<? extends Location> getLocations(String locationName) {

        Validate.notEmpty(locationName, "Location name cannot be empty");

        List<Location> locations;
        try {
            locations = locationResource.queryForLocationByName(locationName).getResults();
        } catch (HttpException e) {
            LOGGER.error("Failed to retrieve all locations by location name: " + locationName);
            return Collections.emptyList();
        }

        return locations;
    }

    @Override
    public Location getLocationByUuid(String uuid) {

        Validate.notEmpty(uuid, "Location id cannot be empty");

        try {
            return locationResource.getLocationById(uuid);
        } catch (HttpException e) {
            LOGGER.error("Failed to fetch information about location with uuid: " + uuid);
            return null;
        }
    }

    @Override
    public Location createLocation(Location location) {

        Validate.notNull(location, "Location cannot be null");

        // The uuid cannot be included with the request, otherwise OpenMRS will
        // fail
        location.setUuid(null);

        try {
            Location saved = locationResource.createLocation(location);
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.CREATED_NEW_LOCATION_SUBJECT, EventHelper.locationParameters(saved)));

            return saved;

        } catch (HttpException e) {
            LOGGER.error("Could not create location with name: " + location.getName());
            return null;
        }
    }

    @Override
    public void deleteLocation(String uuid) {

        try {
            Location locationToRemove = locationResource.getLocationById(uuid);
            locationResource.deleteLocation(uuid);
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.DELETED_LOCATION_SUBJECT, EventHelper.locationParameters(locationToRemove)));
        } catch (HttpException e) {
            LOGGER.error("Failed to remove location for: " + uuid);
        }
    }

    @Override
    public Location updateLocation(Location location) {

        Location locationToUpdate;

        try {
            locationToUpdate = locationResource.getLocationById(location.getUuid());
        } catch (HttpException e) {
            LOGGER.error("Failed to fetch information about location with uuid: " + location.getUuid());
            return null;
        }

        locationToUpdate.setAddress6(location.getAddress6());
        locationToUpdate.setDescription(location.getName());
        locationToUpdate.setCountry(location.getCountry());
        locationToUpdate.setCountyDistrict(location.getCountyDistrict());
        locationToUpdate.setName(location.getName());
        locationToUpdate.setStateProvince(location.getStateProvince());

        try {
            Location updatedLocation = locationResource.updateLocation(locationToUpdate);
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.UPDATED_LOCATION_SUBJECT, EventHelper.locationParameters(updatedLocation)));

            return updatedLocation;

        } catch (HttpException e) {
            LOGGER.error("Failed to update location with uuid: " + location.getUuid());
            return null;
        }
    }
}
