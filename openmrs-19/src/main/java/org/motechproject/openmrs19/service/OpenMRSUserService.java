package org.motechproject.openmrs19.service;

import org.motechproject.openmrs19.domain.OpenMRSUser;
import org.motechproject.openmrs19.exception.UserAlreadyExistsException;
import org.motechproject.openmrs19.exception.UserDeleteException;

import java.util.List;

/**
 * Interface for handling users on the OpenMRS server.
 */
public interface OpenMRSUserService {

    /**
     * Key to obtain {@link OpenMRSUser} from {@link #createUser(OpenMRSUser)} and {@link #updateUser(OpenMRSUser)}
     */
    String USER_KEY = "mrsUser";

    /**
     * Key to obtain password from {@link #createUser(OpenMRSUser)} and {@link #updateUser(OpenMRSUser)}
     */
    String PASSWORD_KEY = "password";

    /**
     * Changes the password of the current user.
     *
     * @param currentPassword  the current password
     * @param newPassword  the new password
     */
    void changeCurrentUserPassword(String currentPassword, String newPassword);

    /**
     * Creates the given {@code user} on the OpenMRS server.
     *
     * @param user  the user to be created
     * @return  the created user
     * @throws UserAlreadyExistsException if the user already exists
     */
    OpenMRSUser createUser(OpenMRSUser user) throws UserAlreadyExistsException;

    /**
     * Resets the password of the user with the given {@code username}.
     *
     * @param username  the name of the user
     * @return the new password, null if the user doesn't exist
     */
    String setNewPasswordForUser(String username);

    /**
     * Returns a list of all users on the OpenMRS server.
     *
     * @return the list of all users
     */
    List<OpenMRSUser> getAllUsers();

    /**
     * Return the user with the given {@code uuid}.
     *
     * @param uuid  the UUID of the user
     * @return the user with the given UUID, null if the user doesn't exist
     */
    OpenMRSUser getUserByUuid(String uuid);

    /**
     * Returns the user with the given {@code userName}.
     *
     * @param userName  the username of the user
     * @return the user with the given username, null if the user doesn't exist
     */
    OpenMRSUser getUserByUserName(String userName);

    /**
     * Updates the user with the information stored in the given {@code user}.
     *
     * @param user  the user to be used as an update source
     * @return the update user, null if the user doesn't exist
     */
    OpenMRSUser updateUser(OpenMRSUser user);

    /**
     * Deletes the user with the given {@code uuid}. If the provider with the given {@code uuid} doesn't exist an
     * error will be logged.
     *
     * @param uuid  the UUID of the user
     * @throws UserDeleteException if there were problems while deleting user
     */
    void deleteUser(String uuid) throws UserDeleteException;
}
