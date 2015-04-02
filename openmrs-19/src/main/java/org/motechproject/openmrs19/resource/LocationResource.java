package org.motechproject.openmrs19.resource;

import org.motechproject.openmrs19.exception.HttpException;
import org.motechproject.openmrs19.resource.model.Location;
import org.motechproject.openmrs19.resource.model.LocationListResult;

/**
 * Interface for locations management.
 */
public interface LocationResource {

    /**
     * Returns {@code LocationListResult} of all locations stored on the OpenMRS server.
     *
     * @return  the list of all locations
     * @throws HttpException  when there were problems while fetching locations
     */
    LocationListResult getAllLocations() throws HttpException;

    /**
     * Returns {@code LocationListResult} of all locations matching given name.
     *
     * @param locationName  the name to be matched
     * @return  the list of matching locations
     * @throws HttpException  when there were problems while fetching locations
     */
    LocationListResult queryForLocationByName(String locationName) throws HttpException;

    /**
     * Gets location by its UUID.
     *
     * @param uuid  the UUID of the location
     * @return  the location with given UUID
     * @throws HttpException  when there were problems while fetching location
     */
    Location getLocationById(String uuid) throws HttpException;

    /**
     * Create given location on the OpenMRS server.
     *
     * @param location  the location to be create
     * @return  the saved location
     * @throws HttpException  when there were problems while creating location
     */
    Location createLocation(Location location) throws HttpException;

    /**
     * Updates location with given data.
     * @param location  the update source
     * @return  the updated location
     * @throws HttpException  when there were problems while updating location
     */
    Location updateLocation(Location location) throws HttpException;

    /**
     * Deletes the location with the given UUID.
     *
     * @param uuid  the uuid of the location
     * @throws HttpException  when there were problems while deleting location
     */
    void deleteLocation(String uuid) throws HttpException;

    /**
     * Fetches page with given number with size defined in {@code pageSize}. Page numeration starts with 1.
     *
     * @param page  the number of the page
     * @param pageSize  the size of the page
     * @return  the list of locations on the given page
     * @throws HttpException  when there were problems while fetching locations
     */
    LocationListResult getLocations(int page, int pageSize) throws HttpException;
}
