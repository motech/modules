package org.motechproject.openmrs19.service;

import org.motechproject.openmrs19.domain.User;
import org.motechproject.openmrs19.exception.UserAlreadyExistsException;
import org.motechproject.openmrs19.exception.UserDeleteException;

import java.util.List;

/**
 * Interface for handling users on the OpenMRS server.
 */
public interface OpenMRSUserService {

    /**
     * Key to obtain {@link User} from {@link #createUser(User)} and {@link #updateUser(User)}
     */
    String USER_KEY = "mrsUser";

    /**
     * Key to obtain password from {@link #createUser(User)} and {@link #updateUser(User)}
     */
    String PASSWORD_KEY = "password";

    /**
     * Changes the password of the current user. Configuration with the given {@code configName} will be used while
     * performing this action.
     *
     * @param configName  the name of the configuration
     * @param currentPassword  the current password
     * @param newPassword  the new password
     */
    void changeCurrentUserPassword(String configName, String currentPassword, String newPassword);

    /**
     * Changes the password of the current user. The default  configuration will be used while performing this
     * action.
     *
     * @param currentPassword  the current password
     * @param newPassword  the new password
     */
    void changeCurrentUserPassword(String currentPassword, String newPassword);

    /**
     * Creates the given {@code user} on the OpenMRS server. Configuration with the given {@code configName} will be
     * used while performing this action.
     *
     * @param configName  the name of the configuration
     * @param user  the user to be created
     * @return  the created user
     * @throws UserAlreadyExistsException if the user already exists
     */
    User createUser(String configName, User user) throws UserAlreadyExistsException;

    /**
     * Creates the given {@code user} on the OpenMRS server. The default  configuration will be used while
     * performing this action.
     *
     * @param user  the user to be created
     * @return  the created user
     * @throws UserAlreadyExistsException if the user already exists
     */
    User createUser(User user) throws UserAlreadyExistsException;

    /**
     * Resets the password of the user with the given {@code username}. Configuration with the given {@code configName}
     * will be used while performing this action.
     *
     * @param configName  the name of the configuration
     * @param username  the name of the user
     * @return the new password, null if the user doesn't exist
     */
    String setNewPasswordForUser(String configName, String username);

    /**
     * Resets the password of the user with the given {@code username}. The default  configuration will be used
     * while performing this action.
     *
     * @param username  the name of the user
     * @return the new password, null if the user doesn't exist
     */
    String setNewPasswordForUser(String username);

    /**
     * Returns a list of all users on the OpenMRS server. Configuration with the given {@code configName} will be used
     * while performing this action.
     *
     * @param configName  the name of the configuration
     * @return the list of all users
     */
    List<User> getAllUsers(String configName);

    /**
     * Returns a list of all users on the OpenMRS server. The default  configuration will be used while
     * performing this action.
     *
     * @return the list of all users
     */
    List<User> getAllUsers();

    /**
     * Return the user with the given {@code uuid}. Configuration with the given {@code configName} will be used while
     * performing this action.
     *
     * @param configName  the name of the configuration
     * @param uuid  the UUID of the user
     * @return the user with the given UUID, null if the user doesn't exist
     */
    User getUserByUuid(String configName, String uuid);

    /**
     * Return the user with the given {@code uuid}. The default  configuration will be used while performing this
     * action.
     *
     * @param uuid  the UUID of the user
     * @return the user with the given UUID, null if the user doesn't exist
     */
    User getUserByUuid(String uuid);

    /**
     * Returns the user with the given {@code userName}. Configuration with the given {@code configName} will be used
     * while performing this action.
     *
     * @param configName  the name of the configuration
     * @param userName  the username of the user
     * @return the user with the given username, null if the user doesn't exist
     */
    User getUserByUserName(String configName, String userName);

    /**
     * Returns the user with the given {@code userName}. The default  configuration will be used while performing
     * this action.
     *
     * @param userName  the username of the user
     * @return the user with the given username, null if the user doesn't exist
     */
    User getUserByUserName(String userName);

    /**
     * Updates the user with the information stored in the given {@code user}. Configuration with the given
     * {@code configName} will be used while performing this action.
     *
     * @param configName  the name of the configuration
     * @param user  the user to be used as an update source
     * @return the update user, null if the user doesn't exist
     */
    User updateUser(String configName, User user);

    /**
     * Updates the user with the information stored in the given {@code user}. The default  configuration will be
     * used while performing this action.
     *
     * @param user  the user to be used as an update source
     * @return the update user, null if the user doesn't exist
     */
    User updateUser(User user);

    /**
     * Deletes the user with the given {@code uuid}. If the provider with the given {@code uuid} doesn't exist an
     * error will be logged. Configuration with the given {@code configName} will be used while performing this action.
     *
     * @param configName  the name of the configuration
     * @param uuid  the UUID of the user
     * @throws UserDeleteException if there were problems while deleting user
     */
    void deleteUser(String configName, String uuid) throws UserDeleteException;

    /**
     * Deletes the user with the given {@code uuid}. If the provider with the given {@code uuid} doesn't exist an
     * error will be logged. The default  configuration will be used while performing this action.
     *
     * @param uuid  the UUID of the user
     * @throws UserDeleteException if there were problems while deleting user
     */
    void deleteUser(String uuid) throws UserDeleteException;
}
