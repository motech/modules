package org.motechproject.openmrs19.service;

import org.motechproject.openmrs19.domain.OpenMRSUser;
import org.motechproject.openmrs19.exception.UserAlreadyExistsException;

import java.util.List;
import java.util.Map;

/**
 * Interface to handle MRS users (medical staff).
 */
public interface OpenMRSUserService {

    /**
     * Key to obtain {@link OpenMRSUser} from {@link #saveUser(OpenMRSUser)} and {@link #updateUser(OpenMRSUser)}
     */
    String USER_KEY = "mrsUser";

    /**
     * Key to obtain password from {@link #saveUser(OpenMRSUser)} and {@link #updateUser(OpenMRSUser)}
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
     * @param mrsUser  instance of the User object to be saved
     * @return a Map containing saved user's data
     * @throws UserAlreadyExistsException if the user already exists
     */
    Map<String, Object> saveUser(OpenMRSUser mrsUser) throws UserAlreadyExistsException;

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
     * Finds user by UserName.
     *
     * @param userId  user's unique Identifier
     * @return the User object, if found
     */
    OpenMRSUser getUserByUserName(String userId);

    /**
     * Updates User attributes.
     *
     * @param mrsUser  MRS User object
     * @return a Map containing saved user's data
     */
    Map<String, Object> updateUser(OpenMRSUser mrsUser);
}
