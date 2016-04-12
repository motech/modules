package org.motechproject.openmrs19.service.impl;

import org.apache.commons.lang.Validate;
import org.motechproject.openmrs19.domain.Password;
import org.motechproject.openmrs19.domain.Person;
import org.motechproject.openmrs19.domain.Role;
import org.motechproject.openmrs19.domain.User;
import org.motechproject.openmrs19.exception.HttpException;
import org.motechproject.openmrs19.exception.OpenMRSException;
import org.motechproject.openmrs19.exception.UserAlreadyExistsException;
import org.motechproject.openmrs19.exception.UserDeleteException;
import org.motechproject.openmrs19.resource.UserResource;
import org.motechproject.openmrs19.service.OpenMRSUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service("userService")
public class OpenMRSUserServiceImpl implements OpenMRSUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenMRSUserServiceImpl.class);

    private static final int DEFAULT_PASSWORD_LENGTH = 8;

    private final UserResource userResource;
    private final OpenMRSPersonServiceImpl personAdapter;
    private final Password password = new Password(DEFAULT_PASSWORD_LENGTH);

    @Autowired
    public OpenMRSUserServiceImpl(UserResource userResource, OpenMRSPersonServiceImpl personAdapter) {
        this.userResource = userResource;
        this.personAdapter = personAdapter;
    }

    @Override
    public void changeCurrentUserPassword(String currentPassword, String newPassword) {
        // no way of doing this operation because you cannot retrieve the
        // password for a user through the web services
        throw new UnsupportedOperationException();
    }

    @Override
    public List<User> getAllUsers() {
        try {
            return userResource.getAllUsers().getResults();
        } catch (HttpException e) {
            LOGGER.error("Failed to get all users from OpenMRS: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public User getUserByUserName(String userName) {
        Validate.notEmpty(userName, "Username cannot be empty");

        List<User> results;

        try {
            results = userResource.queryForUsersByUsername(userName).getResults();
        } catch (HttpException e) {
            LOGGER.error("Failed to retrieve user by username: " + userName + " with error: " + e.getMessage());
            return null;
        }

        User openMRSUser = null;

        for (User user : results) {
            if (user.getUsername().equals(userName)) {
                openMRSUser = getUserByUuid(user.getUuid());
            }
        }

        return openMRSUser;
    }

    @Override
    public User getUserByUuid(String uuid) {
        Validate.notEmpty(uuid, "UUID cannot be empty");

        try {
            return userResource.getUserById(uuid);
        } catch (HttpException e) {
            LOGGER.error("Failed to retrieve user with UUID: " + uuid + " with error: " + e.getMessage());
            return null;
        }
    }

    @Override
    public User createUser(User user) throws UserAlreadyExistsException {
        validateUserForSave(user);
        validateUserNameUsage(user);
        // If the person is saved prior to checking for the role, there would need to be another call to delete the person
        // otherwise it would leave the OpenMRS in an inconsistent state
        resolveRoleUuidFromRoleName(user.getRoles());

        if (user.getPerson().getUuid() == null) {
            Person savedPerson = personAdapter.createPerson(user.getPerson());
            user.setPerson(savedPerson);
        }

        user.setPassword(password.generate());

        try {
            return userResource.createUser(user);
        } catch (HttpException e) {
            LOGGER.error("Failed to save user: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String setNewPasswordForUser(String username) {
        Validate.notEmpty(username, "Username cannot be empty");

        User user = getUserByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("No user found with username: " + username);
        }

        User tmp = new User();
        tmp.setPassword(password.generate());
        tmp.setUuid(user.getUuid());

        try {
            userResource.updateUser(tmp);
        } catch (HttpException e) {
            LOGGER.error("Failed to set password for username: " + username);
            return null;
        }

        return tmp.getPassword();
    }

    @Override
    public User updateUser(User user) {
        validateUserForUpdate(user);

        user.setPassword(password.generate());

        try {
            return userResource.updateUser(user);
        } catch (HttpException e) {
            LOGGER.error("Failed to update user: " + user.getUuid());
            return null;
        }
    }

    @Override
    public void deleteUser(String uuid) throws UserDeleteException {
        Validate.notEmpty(uuid);

        try {
            userResource.deleteUser(uuid);
        } catch (HttpException e) {
            throw new UserDeleteException("Error occurred while deleting user with UUID: " + uuid, e);
        }

    }

    private void validateUserNameUsage(User user) throws UserAlreadyExistsException {
        if (getUserByUserName(user.getUsername()) != null) {
            throw new UserAlreadyExistsException("Already found user with username: " + user.getUsername());
        }
    }

    private void validateUserForSave(User user) {
        Validate.notNull(user, "User cannot be null");
        Validate.notEmpty(user.getUsername(), "Username cannot be empty");
        Validate.notNull(user.getPerson(), "Person cannot be null");
    }

    private void validateUserForUpdate(User user) {
        Validate.notNull(user, "User cannot be null");
        Validate.notEmpty(user.getUuid(), "User id cannot be empty");
        Validate.notNull(user.getPerson(), "User Person cannot be null");
        Validate.notEmpty(user.getPerson().getUuid(), "User person id cannot be empty");
    }

    private void resolveRoleUuidFromRoleName(List<Role> roles) {
        List<Role> retrievedRoles = getAllRoles();
        for (Role role : roles) {
            boolean roleExist = false;
            for (Role retrieveRole : retrievedRoles) {
                if (retrieveRole.getName().equals(role.getName())) {
                    role.setUuid(retrieveRole.getUuid());
                    roleExist = true;
                    break;
                }
            }

            if (!roleExist) {
                throw new OpenMRSException("No OpenMRS role found with name: " + role.getName());
            }
        }
    }

    private List<Role> getAllRoles() {
        List<Role> roles;

        try {
            roles = userResource.getAllRoles().getResults();
        } catch (HttpException e) {
            LOGGER.error("Failed to retrieve the list of roles: " + e.getMessage());
            return null;
        }

        return roles;
    }
}
