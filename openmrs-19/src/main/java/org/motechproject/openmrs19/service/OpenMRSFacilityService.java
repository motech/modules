package org.motechproject.openmrs19.service;

import org.motechproject.openmrs19.domain.OpenMRSFacility;

import java.util.List;

/**
 * Interface for handling facilities on the OpenMRS server.
 */
public interface OpenMRSFacilityService {

    /**
     * Creates the given {@code facility} on the OpenMRS server.
     *
     * @param facility  the facility to be created
     * @return the created facility
     */
    OpenMRSFacility createFacility(OpenMRSFacility facility);

    /**
     * Returns a list of the facilities. The returned list will contain maximum of {@code pageSize} facilities fetched
     * from the page with the given {@code page} number. The list might contain less entries if the given {@code page}
     * is the last one and there is less than {@code pageSize} facilities on it.
     *
     * @param page  the number of the page
     * @param pageSize  the size of the page
     * @return  the list of facilities on the given page
     */
    List<? extends OpenMRSFacility> getFacilities(int page, int pageSize);

    /**
     * Returns a list of all the facilities on the OpenMRS server.
     *
     * @return the list of all facilities
     */
    List<? extends OpenMRSFacility> getAllFacilities();

    /**
     * Returns a list of facilities that have the given {@code locationName}.
     *
     * @param locationName  the name of the location
     * @return the list of matching facilities
     */
    List<? extends OpenMRSFacility> getFacilities(String locationName);

    /**
     * Returns the facility with the given {@code uuid}.
     *
     * @param uuid  the UUID of the facility
     * @return the facility with the given UUID
     */
    OpenMRSFacility getFacilityByUuid(String uuid);

    /**
     * Deletes the facility with the given {@code uuid} from the OpenMRS server.
     *
     * @param uuid the UUID of the facility
     */
    void deleteFacility(String uuid);

    /**
     * Updates the facility with the information stored in the given {@code facility}.
     *
     * @param facility  the facility to be used as an update source
     * @return the updated facility, null if a facility with the UUID given in the {@code facility} doesn't exist
     */
    OpenMRSFacility updateFacility(OpenMRSFacility facility);
}
