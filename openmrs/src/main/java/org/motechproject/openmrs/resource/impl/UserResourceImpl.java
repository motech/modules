package org.motechproject.openmrs.resource.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.httpclient.HttpClient;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.Person;
import org.motechproject.openmrs.domain.Role;
import org.motechproject.openmrs.domain.RoleListResult;
import org.motechproject.openmrs.domain.User;
import org.motechproject.openmrs.domain.UserListResult;
import org.motechproject.openmrs.resource.UserResource;
import org.motechproject.openmrs.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

@Component
public class UserResourceImpl extends BaseResource implements UserResource {

    @Autowired
    public UserResourceImpl(RestOperations restOperations, HttpClient httpClient) {
        super(restOperations, httpClient);
    }

    @Override
    public UserListResult getAllUsers(Config config) {
        String responseJson = getJson(config, "/user?v=full");
        return (UserListResult) JsonUtils.readJson(responseJson, UserListResult.class);
    }

    @Override
    public UserListResult queryForUsersByUsername(Config config, String username) {
        String responseJson = getJson(config, "/user?q={username}&v=full", username);
        return (UserListResult) JsonUtils.readJson(responseJson, UserListResult.class);
    }

    @Override
    public User createUser(Config config, User user) {
        String requestJson = getGson().toJson(user);
        String responseJson = postForJson(config, requestJson, "/user");
        return (User) JsonUtils.readJson(responseJson, User.class);
    }

    @Override
    public RoleListResult getAllRoles(Config config) {
        String responseJson = getJson(config, "/role?v=full");
        return (RoleListResult) JsonUtils.readJson(responseJson, RoleListResult.class);
    }

    @Override
    public User updateUser(Config config, User user) {
        String requestJson = getGson().toJson(user);
        String responseJson = postForJson(config, requestJson, "/user/{uuid}", user.getUuid());
        return (User) JsonUtils.readJson(responseJson, User.class);
    }

    @Override
    public void deleteUser(Config config, String uuid) {
        delete(config, "/user/{userId}?purge", uuid);
    }

    @Override
    public User getUserById(Config config, String uuid) {
        String responseJson = getJson(config, "/user/{uuid}", uuid);
        return (User) JsonUtils.readJson(responseJson, User.class);
    }

    private Gson getGson() {
        GsonBuilder builder = new GsonBuilder();

        builder.excludeFieldsWithoutExposeAnnotation();
        builder.registerTypeAdapter(Person.class, new Person.PersonSerializer());
        builder.registerTypeAdapter(Role.class, new Role.RoleSerializer());

        return builder.create();
    }
}
