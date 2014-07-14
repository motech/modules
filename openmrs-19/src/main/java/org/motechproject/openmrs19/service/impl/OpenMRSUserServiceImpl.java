package org.motechproject.openmrs19.service.impl;

import org.apache.commons.lang.Validate;
import org.motechproject.openmrs19.domain.OpenMRSPerson;
import org.motechproject.openmrs19.domain.OpenMRSUser;
import org.motechproject.openmrs19.exception.OpenMRSException;
import org.motechproject.openmrs19.exception.UserAlreadyExistsException;
import org.motechproject.openmrs19.service.OpenMRSUserService;
import org.motechproject.openmrs19.domain.Password;
import org.motechproject.openmrs19.rest.HttpException;
import org.motechproject.openmrs19.resource.UserResource;
import org.motechproject.openmrs19.resource.model.Role;
import org.motechproject.openmrs19.resource.model.RoleListResult;
import org.motechproject.openmrs19.resource.model.User;
import org.motechproject.openmrs19.resource.model.UserListResult;
import org.motechproject.openmrs19.util.ConverterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("userService")
public class OpenMRSUserServiceImpl implements OpenMRSUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenMRSUserServiceImpl.class);

    private static final int DEFAULT_PASSWORD_LENGTH = 8;
    private final Map<String, String> cachedRoles = new HashMap<String, String>();

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
        UserListResult result = null;
        try {
            result = userResource.getAllUsers();
        } catch (HttpException e) {
            LOGGER.error("Failed to get all users from OpenMRS: " + e.getMessage());
            return Collections.emptyList();
        }

        List<User> users = result.getResults();
        List<OpenMRSUser> mrsUsers = new ArrayList<>();
        for (User u : users) {
            // OpenMRS provides 2 default users (admin/daemon)
            // Intentionally filtering out these users as they have missing
            // properties e.g. daemon has no associated person
            if ("admin".equals(u.getSystemId()) || "daemon".equals(u.getSystemId())) {
                continue;
            }
            // the user response does not include the full person name or
            // address
            // must retrieve these separately
            OpenMRSPerson person = personAdapter.findByPersonId(u.getPerson().getUuid()).get(0);
            mrsUsers.add(convertToMrsUser(u, person));
        }

        return mrsUsers;
    }

    private OpenMRSUser convertToMrsUser(User u, OpenMRSPerson person) {
        OpenMRSUser user = new OpenMRSUser();
        user.setUserId(u.getUuid());
        user.setPerson(person);
        user.setSecurityRole(u.getFirstRole());
        user.setSystemId(u.getSystemId());
        user.setUserName(u.getUsername());

        return user;
    }

    @Override
    public OpenMRSUser getUserByUserName(String username) {
        Validate.notEmpty(username, "Username cannot be empty");

        UserListResult results = null;
        try {
            results = userResource.queryForUsersByUsername(username);
        } catch (HttpException e) {
            LOGGER.error("Failed to retrieve user by username: " + username + " with error: " + e.getMessage());
            return null;
        }

        if (results.getResults().size() == 0) {
            return null;
        } else if (results.getResults().size() > 1) {
            LOGGER.warn("Found multipe user accounts");
        }

        // User response json does not include person name or address
        // must retrieve these separately
        User u = results.getResults().get(0);
        OpenMRSPerson person = personAdapter.findByPersonId(u.getPerson().getUuid()).get(0);
        OpenMRSUser user = convertToMrsUser(u, (OpenMRSPerson) person);

        return user;
    }

    @Override
    public Map<String, Object> saveUser(OpenMRSUser user) throws UserAlreadyExistsException {
        Validate.notNull(user, "User cannot be null");
        Validate.notEmpty(user.getUserName(), "Username cannot be empty");
        Validate.notNull(user.getPerson(), "Person cannot be null");

        if (getUserByUserName(user.getUserName()) != null) {
            LOGGER.warn("Already found user with username: " + user.getUserName());
            throw new UserAlreadyExistsException();
        }

        // attempt to retrieve the roleUuid before saving the person
        // its possible the role doesn't exist in the OpenMRS, in which case
        // an exception is thrown. If the person is saved prior to checking
        // for the role, there would need to be another call to delete the
        // person
        // otherwise it would leave the OpenMRS in an inconsistent state
        getRoleUuidByRoleName(user);

        OpenMRSPerson savedPerson = personAdapter.addPerson((OpenMRSPerson) user.getPerson());
        user.setPerson((OpenMRSPerson) savedPerson);

        String generatedPassword = password.create();
        User converted = convertToUser(user, generatedPassword);
        User saved = null;
        try {
            saved = userResource.createUser(converted);
        } catch (HttpException e) {
            LOGGER.error("Failed to save user: " + e.getMessage());
            return null;
        }

        user.setUserId(saved.getUuid());
        user.setSystemId(saved.getSystemId());
        Map<String, Object> values = new HashMap<String, Object>();
        values.put(USER_KEY, user);
        values.put(PASSWORD_KEY, generatedPassword);

        return values;
    }

    private User convertToUser(OpenMRSUser user, String password) {
        User converted = new User();
        converted.setPassword(password);
        converted.setPerson(ConverterUtils.convertToPerson(user.getPerson(), false));
        converted.setRoles(convertRoles(user));
        converted.setUsername(user.getUserName());
        converted.setSystemId(user.getSystemId());
        converted.setUuid(user.getUserId());
        return converted;
    }

    private List<Role> convertRoles(OpenMRSUser user) {
        List<Role> roles = new ArrayList<Role>();
        Role role = new Role();
        role.setUuid(getRoleUuidByRoleName(user));
        roles.add(role);
        return roles;
    }

    private String getRoleUuidByRoleName(OpenMRSUser user) {
        if (!roleIsPresentInOpenMrs(user.getSecurityRole())) {
            LOGGER.error("Could not find a role in OpenMRS with name: " + user.getSecurityRole());
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
        RoleListResult result = null;
        try {
            result = userResource.getAllRoles();
        } catch (HttpException e) {
            LOGGER.error("Failed to retrieve the list of roles: " + e.getMessage());
            return;
        }

        cachedRoles.putAll(handleResult(result.getResults()));
    }

    private Map<String, String> handleResult(List<Role> results) {
        Map<String, String> roleMap = new HashMap<String, String>();
        for (Role role : results) {
            roleMap.put(role.getName(), role.getUuid());
        }

        return roleMap;
    }

    @Override
    public String setNewPasswordForUser(String username) {
        Validate.notEmpty(username, "Username cannot be empty");

        OpenMRSUser user = getUserByUserName(username);
        if (user == null) {
            LOGGER.warn("No user foudn with username: " + username);
            throw new UsernameNotFoundException("No user found with username: " + username);
        }

        String newPassword = password.create();

        User tmp = new User();
        tmp.setPassword(newPassword);
        tmp.setUuid(user.getUserId());

        try {
            userResource.updateUser(tmp);
        } catch (HttpException e) {
            LOGGER.error("Failed to set password for username: " + username);
            return null;
        }

        return newPassword;
    }

    @Override
    public Map<String, Object> updateUser(OpenMRSUser user) {
        Validate.notNull(user, "User cannot be null");
        Validate.notEmpty(user.getUserId(), "User id cannot be empty");
        Validate.notNull(user.getPerson(), "User Person cannot be null");
        Validate.notEmpty(user.getPerson().getPersonId(), "User person id cannot be empty");

        personAdapter.updatePerson(user.getPerson());

        String generatedPassword = password.create();
        User converted = convertToUser(user, generatedPassword);

        try {
            userResource.updateUser(converted);
        } catch (HttpException e) {
            LOGGER.error("Failed to update user: " + user.getUserId());
            return null;
        }

        Map<String, Object> values = new HashMap<String, Object>();
        values.put(USER_KEY, user);
        values.put(PASSWORD_KEY, generatedPassword);

        return values;
    }
}
