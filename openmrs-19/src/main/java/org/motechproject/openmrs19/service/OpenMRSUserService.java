package org.motechproject.openmrs19.service;

import org.motechproject.openmrs19.domain.OpenMRSUser;
import org.motechproject.openmrs19.exception.UserAlreadyExistsException;
import org.motechproject.openmrs19.exception.UserDeleteException;

import java.util.List;

/**
 * Interface to handle MRS users (medical staff).
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
     * Changes the password of the user.
     *
     * @param currentPassword  old password
     * @param newPassword  new password
     */
    void changeCurrentUserPassword(String currentPassword, String newPassword);

    /**
     * Creates a new user.
     *
     * @param mrsUser  instance of the User object to be created
     * @return  the created user
     * @throws UserAlreadyExistsException if the user already exists
     */
    OpenMRSUser createUser(OpenMRSUser mrsUser) throws UserAlreadyExistsException;

    /**
     * Resets the password of a given user.
     *
     * @param userId  user's unique identifier
     * @return new password
     */
    String setNewPasswordForUser(String userId);

    /**
     * Gets all users present in the MRS system.
     *
     * @return list of all Users
     */
    List<OpenMRSUser> getAllUsers();

    /**
     * Finds user by UUID.
     *
     * @param uuid  the UUID of the user
     * @return  the user with given UUID, null if not found
     */
    OpenMRSUser getUserByUuid(String uuid);

    /**
     * Finds user by username.
     *
     * @param userName  the user's username
     * @return the User object, if found
     */
    OpenMRSUser getUserByUserName(String userName);

    /**
     * Updates User attributes.
     *
     * @param mrsUser  MRS User object
     * @return a Map containing saved user's data
     */
    OpenMRSUser updateUser(OpenMRSUser mrsUser);

    /**
     * Deletes user with given UUID.
     *
     * @param userID  the id of the user.
     */
    void deleteUser(String userID) throws UserDeleteException;
}
