package org.motechproject.commcare.service;

import org.motechproject.commcare.domain.CommcareLocation;

import java.util.List;

/**
 * A service to perform queries against CommCareHQ location APIs.
 */
public interface CommcareLocationService {

    /**
     * Retrieves the CommCare location, based on the id and provided configuration name. If name is not set,
     * the default configuration will be used.
     *
     * @param id id of the location
     * @param configName name of the configuration
     * @return location, retrieved from the CommCare server
     */
    CommcareLocation getCommcareLocationById(String id, String configName);

    /**
     * Retrieves the CommCare location, based on the id. It uses default configuration.
     *
     * @param id id of the location
     * @return location, retrieved from the CommCare server
     */
    CommcareLocation getCommcareLocationById(String id);

    /**
     * Retrieves a list of locations, based on provided parameters and configuration name.
     *
     * @param pageSize amount of instances to fetch per page
     * @param pageNumber the page number
     * @param configName name of the configuration
     * @return a list of loctions, retrieved from the CommCare server
     */
    List<CommcareLocation> getCommcareLocations(Integer pageSize, Integer pageNumber, String configName);

    /**
     * Retrieves a list of locations, based on provided parameters. It uses default configuration.
     *
     * @param pageSize amount of instances to fetch per page
     * @param pageNumber the page number
     * @return a list of loctions, retrieved from the CommCare server
     */
    List<CommcareLocation> getCommcareLocations(Integer pageSize, Integer pageNumber);

}
