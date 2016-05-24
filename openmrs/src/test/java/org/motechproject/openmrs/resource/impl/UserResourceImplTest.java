package org.motechproject.openmrs.resource.impl;

import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.motechproject.openmrs.domain.RoleListResult;
import org.motechproject.openmrs.domain.User;
import org.motechproject.openmrs.domain.UserListResult;
import org.mockito.Captor;
import org.mockito.Mock;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.config.ConfigDummyData;
import org.motechproject.openmrs.util.JsonUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestOperations;

import java.net.URI;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserResourceImplTest extends AbstractResourceImplTest {

    private static final String USER_BY_USERNAME_RESPONSE_JSON = "json/user/user-by-username-response.json";
    private static final String USER_UPDATE_NO_USERNAME_JSON = "json/user/user-update-no-username.json";
    private static final String USER_LIST_RESPONSE_JSON = "json/user/user-list-full-response.json";
    private static final String ROLE_RESPONSE_JSON = "json/role/role-response.json";
    private static final String USER_RESPONSE_JSON = "json/user/user-response.json";
    private static final String USER_CREATE_JSON = "json/user/user-create.json";

    @Mock
    private RestOperations restOperations;

    @Captor
    private ArgumentCaptor<HttpEntity<String>> requestCaptor;

    private UserResourceImpl userResource;

    private Config config;

    @Before
    public void setUp() {
        initMocks(this);
        userResource = new UserResourceImpl(restOperations);
        config = ConfigDummyData.prepareConfig("one");
    }

    @Test
    public void shouldCreateUser() throws Exception {
        User user = buildUser();
        URI url = config.toInstancePath("/user");

        when(restOperations.exchange(eq(url), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponse(USER_RESPONSE_JSON));

        User created = userResource.createUser(config, user);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.POST), requestCaptor.capture(), eq(String.class));

        assertThat(created, equalTo(user));
        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForPost(config)));
        assertThat(JsonUtils.readJson(requestCaptor.getValue().getBody(), JsonObject.class),
                equalTo(readFromFile(USER_CREATE_JSON, JsonObject.class)));
    }

    @Test
    public void shouldUpdateUser() throws Exception {
        User user = buildUser();
        URI url = config.toInstancePathWithParams("/user/{uuid}", user.getUuid());

        when(restOperations.exchange(eq(url), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponse(USER_RESPONSE_JSON));

        User updated = userResource.updateUser(config, user);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.POST), requestCaptor.capture(), eq(String.class));

        assertThat(updated, equalTo(user));
        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForPost(config)));
        assertThat(JsonUtils.readJson(requestCaptor.getValue().getBody(), JsonObject.class),
                equalTo(readFromFile(USER_UPDATE_NO_USERNAME_JSON, JsonObject.class)));
    }

    @Test
    public void shouldGetAllUsers() throws Exception {
        URI url = config.toInstancePath("/user?v=full");

        when(restOperations.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponse(USER_LIST_RESPONSE_JSON));

        UserListResult result = userResource.getAllUsers(config);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.GET), requestCaptor.capture(), eq(String.class));

        assertThat(result, equalTo(readFromFile(USER_LIST_RESPONSE_JSON, UserListResult.class)));
        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForGet(config)));
        assertThat(requestCaptor.getValue().getBody(), nullValue());
    }

    @Test
    public void shouldQueryForUserByUserName() throws Exception {
        String query = "AAA";
        URI url = config.toInstancePathWithParams("/user?q={username}&v=full", query);

        when(restOperations.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponse(USER_BY_USERNAME_RESPONSE_JSON));

        UserListResult result = userResource.queryForUsersByUsername(config, query);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.GET), requestCaptor.capture(), eq(String.class));

        assertThat(result, equalTo(readFromFile(USER_BY_USERNAME_RESPONSE_JSON, UserListResult.class)));
        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForGet(config)));
        assertThat(requestCaptor.getValue().getBody(), nullValue());
    }

    @Test
    public void shouldGetAllRoles() throws Exception {
        URI url = config.toInstancePath("/role?v=full");

        when(restOperations.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(getResponse(ROLE_RESPONSE_JSON));

        RoleListResult result = userResource.getAllRoles(config);

        verify(restOperations).exchange(eq(url), eq(HttpMethod.GET), requestCaptor.capture(), eq(String.class));

        assertThat(result, equalTo(readFromFile(ROLE_RESPONSE_JSON, RoleListResult.class)));
        assertThat(requestCaptor.getValue().getHeaders(), equalTo(getHeadersForGet(config)));
        assertThat(requestCaptor.getValue().getBody(), nullValue());
    }

    private User buildUser() throws Exception {
        return (User) readFromFile(USER_RESPONSE_JSON, User.class);
    }
}
