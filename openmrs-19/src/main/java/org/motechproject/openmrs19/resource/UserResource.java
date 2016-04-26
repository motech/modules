package org.motechproject.openmrs19.resource;

import org.motechproject.openmrs19.domain.RoleListResult;
import org.motechproject.openmrs19.domain.User;
import org.motechproject.openmrs19.domain.UserListResult;
import org.motechproject.openmrs19.config.Config;

/**
 * Interface for users management.
 */
public interface UserResource {

    /**
     * Returns {@code UserListResult} of all the users stored on the OpenMRS server. The given {@code config} will be
     * used while performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @return  the list of all users
     */
    UserListResult getAllUsers(Config config);

    /**
     * Returns {@code UserListResult}  of all the users matching the given username. The given {@code config} will be
     * used while performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param username  the username to be matched
     * @return  the list of matching users
     */
    UserListResult queryForUsersByUsername(Config config, String username);

    /**
     * Creates the given user on the OpenMRS server. The given {@code config} will be used while performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param user  the user to be created
     * @return  the saved user
     */
    User createUser(Config config, User user);

    /**
     * Updates the user with the given data. The given {@code config} will be used while performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param user  the update source
     * @return  the updated user
     */
    User updateUser(Config config, User user);

    /**
     * Returns {@code RoleListResult} of all the roles stored on the OpenMRS server. The given {@code config} will be
     * used while performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @return  the list of all roles
     */
    RoleListResult getAllRoles(Config config);

    /**
     * Deletes the user with the given UUID from the OpenMRS server. The given {@code config} will be used while
     * performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param uuid  the UUID of the user
     */
    void deleteUser(Config config, String uuid);

    /**
     * Gets user by its UUID. The given {@code config} will be used while performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param uuid  the UUID of the user
     * @return  the user with the given UUID
     */
    User getUserById(Config config, String uuid);
}
