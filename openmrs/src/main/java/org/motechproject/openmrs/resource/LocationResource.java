package org.motechproject.openmrs.resource;

import org.motechproject.openmrs.domain.Location;
import org.motechproject.openmrs.domain.LocationListResult;
import org.motechproject.openmrs.config.Config;

/**
 * Interface for locations management.
 */
public interface LocationResource {

    /**
     * Returns {@code LocationListResult} of all locations stored on the OpenMRS server. The given {@code config} will
     * be used while performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @return  the list of all locations
     */
    LocationListResult getAllLocations(Config config);

    /**
     * Returns {@code LocationListResult} of all locations matching given name. The given {@code config} will be used
     * while performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param locationName  the name to be matched
     * @return  the list of matching locations
     */
    LocationListResult queryForLocationByName(Config config, String locationName);

    /**
     * Gets location by its UUID. The given {@code config} will be used while performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param uuid  the UUID of the location
     * @return  the location with given UUID
     */
    Location getLocationById(Config config, String uuid);

    /**
     * Create given location on the OpenMRS server. The given {@code config} will be used while performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param location  the location to be create
     * @return  the saved location
     */
    Location createLocation(Config config, Location location);

    /**
     * Updates location with given data. The given {@code config} will be used while performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param location  the update source
     * @return  the updated location
     */
    Location updateLocation(Config config, Location location);

    /**
     * Deletes the location with the given UUID. The given {@code config} will be used while performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param uuid  the uuid of the location
     */
    void deleteLocation(Config config, String uuid);

    /**
     * Fetches page with given number with size defined in {@code pageSize}. Page numeration starts with 1. The given
     * {@code config} will be used while performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param page  the number of the page
     * @param pageSize  the size of the page
     * @return  the list of locations on the given page
     */
    LocationListResult getLocations(Config config, int page, int pageSize);
}
