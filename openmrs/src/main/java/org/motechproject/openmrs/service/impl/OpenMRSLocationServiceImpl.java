package org.motechproject.openmrs.service.impl;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.Location;
import org.motechproject.openmrs.exception.OpenMRSException;
import org.motechproject.openmrs.helper.EventHelper;
import org.motechproject.openmrs.resource.LocationResource;
import org.motechproject.openmrs.service.EventKeys;
import org.motechproject.openmrs.service.OpenMRSConfigService;
import org.motechproject.openmrs.service.OpenMRSLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;
import java.util.List;

@Component("locationService")
public class OpenMRSLocationServiceImpl implements OpenMRSLocationService {

    private static final Logger LOGGER = Logger.getLogger(OpenMRSLocationServiceImpl.class);

    private final OpenMRSConfigService configService;

    private final LocationResource locationResource;

    private final EventRelay eventRelay;

    @Autowired
    public OpenMRSLocationServiceImpl(LocationResource locationResource, EventRelay eventRelay,
                                      OpenMRSConfigService configService) {
        this.locationResource = locationResource;
        this.configService = configService;
        this.eventRelay = eventRelay;
    }

    @Override
    public List<Location> getAllLocations(String configName) {
        List<Location> locations;

        try {
            Config config = configService.getConfigByName(configName);
            locations = locationResource.getAllLocations(config).getResults();
        } catch (HttpClientErrorException e) {
            LOGGER.error("Failed to retrieve all locations");
            return Collections.emptyList();
        }

        return locations;
    }

    @Override
    public List<Location> getLocations(String configName, int page, int pageSize) {
        List<Location> locations;

        try {
            Config config = configService.getConfigByName(configName);
            locations = locationResource.getLocations(config, page, pageSize).getResults();
        } catch (HttpClientErrorException e) {
            LOGGER.error("Failed to retrieve locations");
            return Collections.emptyList();
        }

        return locations;
    }

    @Override
    public List<Location> getLocations(String configName, String locationName) {
        Validate.notEmpty(locationName, "Location name cannot be empty");

        List<Location> locations;
        try {
            Config config = configService.getConfigByName(configName);
            locations = locationResource.queryForLocationByName(config, locationName).getResults();
        } catch (HttpClientErrorException e) {
            throw new OpenMRSException(String.format("Could not get Locations for name %s. %s %s", locationName, e.getMessage(), e.getResponseBodyAsString()), e);
        }

        return locations;
    }

    @Override
    public Location getLocationByUuid(String configName, String uuid) {
        Validate.notEmpty(uuid, "Location id cannot be empty");

        try {
            Config config = configService.getConfigByName(configName);
            return locationResource.getLocationById(config, uuid);
        } catch (HttpClientErrorException e) {
            LOGGER.error("Failed to fetch information about location with uuid: " + uuid);
            return null;
        }
    }

    @Override
    public Location createLocation(String configName, Location location) {
        Validate.notNull(location, "Location cannot be null");

        try {
            Config config = configService.getConfigByName(configName);
            Location saved = locationResource.createLocation(config, location);
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.CREATED_NEW_LOCATION_SUBJECT, EventHelper.locationParameters(saved)));

            return saved;
        } catch (HttpClientErrorException e) {
            LOGGER.error("Could not create location with name: " + location.getName());
            return null;
        }
    }

    @Override
    public void deleteLocation(String configName, String uuid) {

        try {
            Config config = configService.getConfigByName(configName);
            Location locationToRemove = locationResource.getLocationById(config, uuid);
            locationResource.deleteLocation(config, uuid);
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.DELETED_LOCATION_SUBJECT, EventHelper.locationParameters(locationToRemove)));
        } catch (HttpClientErrorException e) {
            LOGGER.error("Failed to remove location for: " + uuid);
        }
    }

    @Override
    public Location updateLocation(String configName, Location location) {

        Location locationToUpdate;
        Config config = configService.getConfigByName(configName);

        try {
            locationToUpdate = locationResource.getLocationById(config, location.getUuid());
        } catch (HttpClientErrorException e) {
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
            Location updatedLocation = locationResource.updateLocation(config, locationToUpdate);
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.UPDATED_LOCATION_SUBJECT, EventHelper.locationParameters(updatedLocation)));

            return updatedLocation;

        } catch (HttpClientErrorException e) {
            LOGGER.error("Failed to update location with uuid: " + location.getUuid());
            return null;
        }
    }
}
