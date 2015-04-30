package org.motechproject.commcare.service;

import org.motechproject.commcare.domain.CommcareUser;

import java.util.List;

/**
 * A service to perform queries against CommCareHQ's user APIs. CommCareHQ
 * exposes one user API, therefore getting a user by Id simply filters the list
 * of all users returned by CommCareHQ.
 */
public interface CommcareUserService {

    /**
     * Queries CommCareHQ for a list of all users on the configured domain and filters the list to return the user that
     * matches the provided id.
     *
     * @param id  the id of the user to retrieve.
     * @param configName  the name of the configuration used for connecting to CommcareHQ, null means default configuration
     * @return  the CommcareUser object representing the information about the user from CommCareHQ, or null if that user
     *          did not exist on that domain.
     */
    CommcareUser getCommcareUserById(String id, String configName);

    /**
     * Same as {@link #getCommcareUserById(String, String) getCommcareUserById} but uses default Commcare configuration.
     */
    CommcareUser getCommcareUserById(String id);

    /**
     * Queries CommCareHQ for a list of users(located on the given page) on the configured domain.
     *
     * @param pageSize  the size of the page
     * @param pageNumber  the number of the page
     * @param configName  the name of the configuration used for connecting to CommcareHQ, null means default configuration
     * @return  the list of CommcareUsers that represent the information about each user located on the given page from
     *          CommCareHQ
     */
    List<CommcareUser> getCommcareUsers(Integer pageSize, Integer pageNumber, String configName);

    /**
     * Same as {@link #getCommcareUsers(Integer, Integer, String) getCommcareUsers} but uses default Commcare configuration.
     */
    List<CommcareUser> getCommcareUsers(Integer pageSize, Integer pageNumber);
}
