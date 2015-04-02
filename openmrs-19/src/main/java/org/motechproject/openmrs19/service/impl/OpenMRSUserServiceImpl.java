package org.motechproject.openmrs19.service.impl;

import org.apache.commons.lang.Validate;
import org.motechproject.openmrs19.domain.OpenMRSPerson;
import org.motechproject.openmrs19.domain.OpenMRSUser;
import org.motechproject.openmrs19.domain.Password;
import org.motechproject.openmrs19.exception.OpenMRSException;
import org.motechproject.openmrs19.exception.UserAlreadyExistsException;
import org.motechproject.openmrs19.exception.UserDeleteException;
import org.motechproject.openmrs19.resource.UserResource;
import org.motechproject.openmrs19.resource.model.Role;
import org.motechproject.openmrs19.resource.model.RoleListResult;
import org.motechproject.openmrs19.resource.model.User;
import org.motechproject.openmrs19.resource.model.UserListResult;
import org.motechproject.openmrs19.exception.HttpException;
import org.motechproject.openmrs19.service.OpenMRSUserService;
import org.motechproject.openmrs19.util.ConverterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("userService")
public class OpenMRSUserServiceImpl implements OpenMRSUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenMRSUserServiceImpl.class);

    private static final int DEFAULT_PASSWORD_LENGTH = 8;
    private final Map<String, String> cachedRoles = new HashMap<>();

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
    public List<OpenMRSUser> getAllUsers() {

        try {
            return toPartialOpenMRSUsers(userResource.getAllUsers());
        } catch (HttpException e) {
            LOGGER.error("Failed to get all users from OpenMRS: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public OpenMRSUser getUserByUserName(String userName) {

        Validate.notEmpty(userName, "Username cannot be empty");

        UserListResult results;

        try {
            results = userResource.queryForUsersByUsername(userName);
        } catch (HttpException e) {
            LOGGER.error("Failed to retrieve user by username: " + userName + " with error: " + e.getMessage());
            return null;
        }

        OpenMRSUser openMRSUser = null;

        for (User user : results.getResults()) {
            if (user.getUsername().equals(userName)) {
                openMRSUser = getUserByUuid(user.getUuid());
            }
        }

        return openMRSUser;
    }

    public OpenMRSUser getUserByUuid(String uuid) {

        Validate.notEmpty(uuid, "UUID cannot be empty");

        try {
            return ConverterUtils.toOpenMRSUser(userResource.getUserById(uuid));
        } catch (HttpException e) {
            LOGGER.error("Failed to retrieve user with UUID: " + uuid + " with error: " + e.getMessage());
            return null;
        }
    }

    @Override
    public OpenMRSUser createUser(OpenMRSUser user) throws UserAlreadyExistsException {

        validateUserForSave(user);
        validateUserNameUsage(user);

        // attempt to retrieve the roleUuid before saving the person
        // its possible the role doesn't exist in the OpenMRS, in which case
        // an exception is thrown. If the person is saved prior to checking
        // for the role, there would need to be another call to delete the
        // person
        // otherwise it would leave the OpenMRS in an inconsistent state
        getRoleUuidByRoleName(user);

        if (user.getPerson().getId() == null) {
            OpenMRSPerson savedPerson = personAdapter.createPerson(user.getPerson());
            user.setPerson(savedPerson);
        }

        User converted = convertToUser(user, password.create());

        try {
            return ConverterUtils.toOpenMRSUser(userResource.createUser(converted));
        } catch (HttpException e) {
            LOGGER.error("Failed to save user: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String setNewPasswordForUser(String username) {

        Validate.notEmpty(username, "Username cannot be empty");

        OpenMRSUser user = getUserByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("No user found with username: " + username);
        }

        User tmp = new User();
        tmp.setPassword(password.create());
        tmp.setUuid(user.getUserId());

        try {
            userResource.updateUser(tmp);
        } catch (HttpException e) {
            LOGGER.error("Failed to set password for username: " + username);
            return null;
        }

        return tmp.getPassword();
    }

    @Override
    public OpenMRSUser updateUser(OpenMRSUser user) {

        validateUserForUpdate(user);

        User converted = convertToUser(user, password.create());

        try {
            return ConverterUtils.toOpenMRSUser(userResource.updateUser(converted));
        } catch (HttpException e) {
            LOGGER.error("Failed to update user: " + user.getUserId());
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

    private List<OpenMRSUser> toPartialOpenMRSUsers(UserListResult users) {

        List<OpenMRSUser> openMRSUsers = new ArrayList<>();

        for (User user : users.getResults()) {
            OpenMRSUser openMRSUser = new OpenMRSUser();
            openMRSUser.setUserId(user.getUuid());
            openMRSUser.setUserName(user.getUsername());
            openMRSUsers.add(openMRSUser);
        }

        return openMRSUsers;
    }

    private void validateUserNameUsage(OpenMRSUser user) throws UserAlreadyExistsException {
        if (getUserByUserName(user.getUserName()) != null) {
            throw new UserAlreadyExistsException("Already found user with username: " + user.getUserName());
        }
    }

    private void validateUserForSave(OpenMRSUser user) {
        Validate.notNull(user, "User cannot be null");
        Validate.notEmpty(user.getUserName(), "Username cannot be empty");
        Validate.notNull(user.getPerson(), "Person cannot be null");
    }

    private void validateUserForUpdate(OpenMRSUser user) {
        Validate.notNull(user, "User cannot be null");
        Validate.notEmpty(user.getUserId(), "User id cannot be empty");
        Validate.notNull(user.getPerson(), "User Person cannot be null");
        Validate.notEmpty(user.getPerson().getPersonId(), "User person id cannot be empty");
    }

    private User convertToUser(OpenMRSUser user, String password) {
        User converted = new User();
        converted.setPassword(password);
        converted.setPerson(ConverterUtils.toPerson(user.getPerson(), false));
        converted.setRoles(convertRoles(user));
        converted.setUsername(user.getUserName());
        converted.setSystemId(user.getSystemId());
        converted.setUuid(user.getUserId());
        return converted;
    }

    private List<Role> convertRoles(OpenMRSUser user) {
        List<Role> roles = new ArrayList<>();
        Role role = new Role();
        role.setUuid(getRoleUuidByRoleName(user));
        roles.add(role);
        return roles;
    }

    private String getRoleUuidByRoleName(OpenMRSUser user) {
        if (!roleIsPresentInOpenMrs(user.getSecurityRole())) {
            throw new OpenMRSException("No OpenMRS role found with name: " + user.getSecurityRole());
        }

        return cachedRoles.get(user.getSecurityRole());
    }

    private boolean roleIsPresentInOpenMrs(String securityRole) {
        if (cachedRoles.containsKey(securityRole)) {
            return true;
        }

        populateRoleCache();
        return cachedRoles.containsKey(securityRole);
    }

    private void populateRoleCache() {
        RoleListResult result;
        try {
            result = userResource.getAllRoles();
        } catch (HttpException e) {
            LOGGER.error("Failed to retrieve the list of roles: " + e.getMessage());
            return;
        }

        cachedRoles.putAll(handleResult(result.getResults()));
    }

    private Map<String, String> handleResult(List<Role> results) {
        Map<String, String> roleMap = new HashMap<>();
        for (Role role : results) {
            roleMap.put(role.getDisplay(), role.getUuid());
        }

        return roleMap;
    }
}
