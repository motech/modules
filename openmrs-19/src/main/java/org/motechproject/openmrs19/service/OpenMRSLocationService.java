package org.motechproject.openmrs19.service;

import org.motechproject.openmrs19.domain.Location;

import java.util.List;

/**
 * Interface for handling locations on the OpenMRS server.
 */
public interface OpenMRSLocationService {

    /**
     * Creates the given {@code location} on the OpenMRS server.
     *
     * @param location  the location to be created
     * @return the created location
     */
    Location createLocation(Location location);

    /**
     * Returns a list of the locations. The returned list will contain maximum of {@code pageSize} locations fetched
     * from the page with the given {@code page} number. The list might contain less entries if the given {@code page}
     * is the last one and there is less than {@code pageSize} locations on it.
     *
     * @param page  the number of the page
     * @param pageSize  the size of the page
     * @return  the list of locations on the given page
     */
    List<Location> getLocations(int page, int pageSize);

    /**
     * Returns a list of all the locations on the OpenMRS server.
     *
     * @return the list of all locations
     */
    List<Location> getAllLocations();

    /**
     * Returns a list of locations that have the given {@code locationName}.
     *
     * @param locationName  the name of the location
     * @return the list of matching locations
     */
    List<Location> getLocations(String locationName);

    /**
     * Returns the location with the given {@code uuid}.
     *
     * @param uuid  the UUID of the location
     * @return the location with the given UUID
     */
    Location getLocationByUuid(String uuid);

    /**
     * Deletes the location with the given {@code uuid} from the OpenMRS server.
     *
     * @param uuid the UUID of the location
     */
    void deleteLocation(String uuid);

    /**
     * Updates the location with the information stored in the given {@code location}.
     *
     * @param location  the location to be used as an update source
     * @return the updated location, null if a location with the UUID given in the {@code location} doesn't exist
     */
    Location updateLocation(Location location);
}
