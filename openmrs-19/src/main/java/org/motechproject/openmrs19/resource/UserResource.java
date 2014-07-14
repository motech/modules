package org.motechproject.openmrs19.resource;

import org.motechproject.openmrs19.rest.HttpException;
import org.motechproject.openmrs19.resource.model.RoleListResult;
import org.motechproject.openmrs19.resource.model.User;
import org.motechproject.openmrs19.resource.model.UserListResult;

public interface UserResource {

    UserListResult getAllUsers() throws HttpException;

    UserListResult queryForUsersByUsername(String username) throws HttpException;

    User createUser(User user) throws HttpException;

    void updateUser(User user) throws HttpException;

    RoleListResult getAllRoles() throws HttpException;
}
