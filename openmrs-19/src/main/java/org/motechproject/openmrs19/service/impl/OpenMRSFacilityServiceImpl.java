package org.motechproject.openmrs19.service.impl;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.openmrs19.EventKeys;
import org.motechproject.openmrs19.domain.OpenMRSFacility;
import org.motechproject.openmrs19.helper.EventHelper;
import org.motechproject.openmrs19.service.OpenMRSFacilityService;
import org.motechproject.openmrs19.rest.HttpException;
import org.motechproject.openmrs19.resource.LocationResource;
import org.motechproject.openmrs19.resource.model.Location;
import org.motechproject.openmrs19.resource.model.LocationListResult;
import org.motechproject.openmrs19.util.ConverterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component("facilityService")
public class OpenMRSFacilityServiceImpl implements OpenMRSFacilityService {
    private static final Logger LOGGER = Logger.getLogger(OpenMRSFacilityServiceImpl.class);

    private final LocationResource locationResource;
    private final EventRelay eventRelay;

    @Autowired
    public OpenMRSFacilityServiceImpl(LocationResource locationResource, EventRelay eventRelay) {
        this.locationResource = locationResource;
        this.eventRelay = eventRelay;
    }

    @Override
    public List<? extends OpenMRSFacility> getFacilities() {
        LocationListResult result = null;
        try {
            result = locationResource.getAllLocations();
        } catch (HttpException e) {
            LOGGER.error("Failed to retrieve all facilities");
            return Collections.emptyList();
        }

        return mapLocationToMrsFacility(result.getResults());
    }

    private List<? extends OpenMRSFacility> mapLocationToMrsFacility(List<Location> facilities) {
        List<OpenMRSFacility> mrsFacilities = new ArrayList<>();
        for (Location location : facilities) {
            mrsFacilities.add(ConverterUtils.convertLocationToMrsLocation(location));
        }
        return mrsFacilities;
    }

    @Override
    public List<? extends OpenMRSFacility> getFacilities(String locationName) {
        Validate.notEmpty(locationName, "Location name cannot be empty");
        LocationListResult result = null;
        try {
            result = locationResource.queryForLocationByName(locationName);
        } catch (HttpException e) {
            LOGGER.error("Failed to retrieve all facilities by location name: " + locationName);
            return Collections.emptyList();
        }

        return mapLocationToMrsFacility(result.getResults());
    }

    @Override
    public OpenMRSFacility getFacility(String facilityId) {
        Validate.notEmpty(facilityId, "Facility id cannot be empty");
        Location location = null;
        try {
            location = locationResource.getLocationById(facilityId);
        } catch (HttpException e) {
            LOGGER.error("Failed to fetch information about location with uuid: " + facilityId);
            return null;
        }

        return ConverterUtils.convertLocationToMrsLocation(location);
    }

    @Override
    public OpenMRSFacility saveFacility(OpenMRSFacility facility) {
        Validate.notNull(facility, "Facility cannot be null");

        // The uuid cannot be included with the request, otherwise OpenMRS will
        // fail
        facility.setFacilityId(null);
        Location location = convertMrsFacilityToLocation(facility);
        Location saved = null;
        try {
            saved = locationResource.createLocation(location);
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.CREATED_NEW_FACILITY_SUBJECT, EventHelper.facilityParameters(facility)));
        } catch (HttpException e) {
            LOGGER.error("Could not create location with name: " + location.getName());
            return null;
        }

        return ConverterUtils.convertLocationToMrsLocation(saved);
    }

    @Override
    public void deleteFacility(String facilityId) {
        try {
            OpenMRSFacility facilityToRemove = ConverterUtils.convertLocationToMrsLocation(locationResource.getLocationById(facilityId));
            locationResource.removeLocation(facilityId);
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.DELETED_FACILITY_SUBJECT, EventHelper.facilityParameters(facilityToRemove)));
        } catch (HttpException e) {
            LOGGER.error("Failed to remove facility for: " + facilityId);
        }
    }

    @Override
    public OpenMRSFacility updateFacility(OpenMRSFacility facility) {
        Location location = null;
        OpenMRSFacility updatedLocation = null;
        try {
            location = locationResource.getLocationById(facility.getFacilityId());
        } catch (HttpException e) {
            LOGGER.error("Failed to fetch information about location with uuid: " + facility.getFacilityId());
            return null;
        }
        location.setAddress6(facility.getRegion());
        location.setDescription(facility.getName());
        location.setCountry(facility.getCountry());
        location.setCountyDistrict(facility.getCountyDistrict());
        location.setName(facility.getName());
        location.setStateProvince(facility.getStateProvince());
        try {
            locationResource.updateLocation(location);
            updatedLocation = ConverterUtils.convertLocationToMrsLocation(locationResource.getLocationById(facility.getFacilityId()));
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.UPDATED_FACILITY_SUBJECT, EventHelper.facilityParameters(updatedLocation)));
        } catch (HttpException e) {
            LOGGER.error("Failed to update location with uuid: " + facility.getFacilityId());
            return null;
        }
        return updatedLocation;
    }

    private Location convertMrsFacilityToLocation(OpenMRSFacility facility) {
        Location location = new Location();
        location.setAddress6(facility.getRegion());
        location.setDescription(facility.getName());
        location.setCountry(facility.getCountry());
        location.setCountyDistrict(facility.getCountyDistrict());
        location.setName(facility.getName());
        location.setStateProvince(facility.getStateProvince());
        location.setUuid(facility.getFacilityId());
        return location;
    }
}
