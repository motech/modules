package org.motechproject.openmrs19.service;

import org.motechproject.openmrs19.domain.OpenMRSFacility;

import java.util.List;

/**
 * Interface for fetching and storing facility details.
 */
public interface OpenMRSFacilityService {
    /**
     * Saves the given facility in the MRS System.
     *
     * @param facility  object to be saved
     * @return the saved instance of the facility
     */
    OpenMRSFacility saveFacility(OpenMRSFacility facility);

    /**
     * Gets all the facilities in the MRS system.
     *
     * @return list of all available facilities
     */
    List<? extends OpenMRSFacility> getFacilities();

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
