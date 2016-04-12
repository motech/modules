package org.motechproject.openmrs19.resource.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.motechproject.openmrs19.OpenMrsInstance;
import org.motechproject.openmrs19.domain.Person;
import org.motechproject.openmrs19.domain.Role;
import org.motechproject.openmrs19.domain.RoleListResult;
import org.motechproject.openmrs19.domain.User;
import org.motechproject.openmrs19.domain.UserListResult;
import org.motechproject.openmrs19.exception.HttpException;
import org.motechproject.openmrs19.resource.UserResource;
import org.motechproject.openmrs19.rest.RestClient;
import org.motechproject.openmrs19.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserResourceImpl implements UserResource {

    private final RestClient restClient;
    private final OpenMrsInstance openmrsInstance;

    @Autowired
    public UserResourceImpl(RestClient restClient, OpenMrsInstance openmrsInstace) {
        this.restClient = restClient;
        this.openmrsInstance = openmrsInstace;
    }

    @Override
    public UserListResult getAllUsers() throws HttpException {
        String responseJson = restClient.getJson(openmrsInstance.toInstancePath("/user?v=full"));
        return (UserListResult) JsonUtils.readJson(responseJson, UserListResult.class);
    }

    @Override
    public UserListResult queryForUsersByUsername(String username) throws HttpException {
        String responseJson = restClient.getJson(openmrsInstance.toInstancePathWithParams("/user?q={username}&v=full",
                username));

        return (UserListResult) JsonUtils.readJson(responseJson, UserListResult.class);
    }

    @Override
    public User createUser(User user) throws HttpException {
        Gson gson = getGsonWithAdapters();
        String responseJson = restClient.postForJson(openmrsInstance.toInstancePath("/user"), gson.toJson(user));

        return (User) JsonUtils.readJson(responseJson, User.class);
    }

    private Gson getGsonWithAdapters() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(Person.class, new Person.PersonSerializer())
                .registerTypeAdapter(Role.class, new Role.RoleSerializer()).create();
        return gson;
    }

    @Override
    public RoleListResult getAllRoles() throws HttpException {
        String responseJson = restClient.getJson(openmrsInstance.toInstancePath("/role?v=full"));
        return (RoleListResult) JsonUtils.readJson(responseJson, RoleListResult.class);
    }

    @Override
    public User updateUser(User user) throws HttpException {
        Gson gson = getGsonWithAdapters();

        String requestJson = gson.toJson(user);

        String responseJson = restClient.postForJson(openmrsInstance.toInstancePathWithParams("/user/{uuid}", user.getUuid()),
                requestJson);

        return (User) JsonUtils.readJson(responseJson, User.class);
    }

    @Override
    public void deleteUser(String uuid) throws HttpException {
        restClient.delete(openmrsInstance.toInstancePathWithParams("/user/{userId}?purge", uuid));
    }

    @Override
    public User getUserById(String uuid) throws HttpException {
        String responseJson = restClient.getJson(openmrsInstance.toInstancePathWithParams("/user/{uuid}", uuid));
        return (User) JsonUtils.readJson(responseJson, User.class);
    }

}
