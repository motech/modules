package org.motechproject.openmrs19.resource;

import org.motechproject.openmrs19.domain.RoleListResult;
import org.motechproject.openmrs19.domain.User;
import org.motechproject.openmrs19.domain.UserListResult;
import org.motechproject.openmrs19.exception.HttpException;

/**
 * Interface for users management.
 */
public interface UserResource {

    /**
     * Returns {@code UserListResult} of all the users stored on the OpenMRS server.
     *
     * @return  the list of all users
     * @throws HttpException  when there were problems while fetching users
     */
    UserListResult getAllUsers() throws HttpException;

    /**
     * Returns {@code UserListResult}  of all the users matching the given username.
     *
     * @param username  the username to be matched
     * @return  the list of matching users
     * @throws HttpException  when there were problems while fetching users
     */
    UserListResult queryForUsersByUsername(String username) throws HttpException;

    /**
     * Creates the given user on the OpenMRS server.
     *
     * @param user  the user to be created
     * @return  the saved user
     * @throws HttpException  when there were problems while creating user
     */
    User createUser(User user) throws HttpException;

    /**
     * Updates the user with the given data.
     *
     * @param user  the update source
     * @return  the updated user
     * @throws HttpException  when there were problems while updating user.
     */
    User updateUser(User user) throws HttpException;

    /**
     * Returns {@code RoleListResult} of all the roles stored on the OpenMRS server.
     *
     * @return  the list of all roles
     * @throws HttpException  when there were problems while fetching roles
     */
    RoleListResult getAllRoles() throws HttpException;

    /**
     * Deletes the user with the given UUID from the OpenMRS server.
     *
     * @param uuid  the UUID of the user
     * @throws HttpException  when there were problems while deleting user
     */
    void deleteUser(String uuid) throws HttpException;

    /**
     * Gets user by its UUID.
     *
     * @param uuid  the UUID of the user
     * @return  the user with the given UUID
     * @throws HttpException  when there were problems while fetching user
     */
    User getUserById(String uuid) throws HttpException;
}
