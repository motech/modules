package org.motechproject.openmrs19.service;

import org.motechproject.openmrs19.domain.OpenMRSFacility;

import java.util.List;

/**
 * Interface for fetching and storing facility details.
 */
public interface OpenMRSFacilityService {
    /**
     * Creates the given facility on the MRS System.
     *
     * @param facility  object to be created
     * @return the saved instance of the facility
     */
    OpenMRSFacility createFacility(OpenMRSFacility facility);

    /**
     * Fetches page with given number with size defined in {@code pageSize}.
     *
     * @param page  the number of the page
     * @param pageSize  the size of the page
     * @return  the list of facilities on the given page
     */
    List<? extends OpenMRSFacility> getFacilities(int page, int pageSize);

    /**
     * Gets all the facilities in the MRS system.
     *
     * @return  the list of all facilities
     */
    List<? extends OpenMRSFacility> getAllFacilities();

    /**
     * Fetches all facilities that have the given location name.
     *
     * @param locationName  value to be used to search
     * @return list of matched facilities
     */
    List<? extends OpenMRSFacility> getFacilities(String locationName);

    /**
     * Fetches facility by facility ID (not the MOTECH ID of the facility).
     *
     * @param facilityId ID of the facility to be fetched
     * @return Facility with the given ID
     */
    OpenMRSFacility getFacility(String facilityId);

    /**
     * Deletes facility by facility ID (not the MOTECH ID of the facility).
     *
     * @param facilityId ID of the facility to be deleted
     */
    void deleteFacility(String facilityId);

    /**
     * Updates the details of the facility provided, based on facility ID. If no match is found
     * for the facility ID, returns null.
     *
     * @param facility  facility object with the fields to update
     * @return the updated facility object; null if no facility object with matching ID is found
     */
    OpenMRSFacility updateFacility(OpenMRSFacility facility);
}
