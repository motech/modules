package org.motechproject.openmrs19.it;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.openmrs19.domain.OpenMRSUser;
import org.motechproject.openmrs19.exception.UserAlreadyExistsException;
import org.motechproject.openmrs19.service.OpenMRSUserService;
import org.motechproject.openmrs19.domain.OpenMRSPerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
public abstract class AbstractUserServiceIT {

    @Autowired
    private OpenMRSUserService userAdapter;

    @Test
    public void shouldCreateUser() throws UserAlreadyExistsException {
        OpenMRSPerson person = new OpenMRSPerson("3789");
        person.setFirstName("Denise");
        person.setLastName("Test");
        person.setAddress("10 Fifth Avenue");
        person.setBirthDateEstimated(false);
        person.setGender("M");
        person.setPreferredName("Denise");

        OpenMRSUser user = new OpenMRSUser();
        user.securityRole("Provider");
        user.userName("denise");
        user.person(person);

        Map<String, Object> result = userAdapter.saveUser(user);
        OpenMRSUser saved = (OpenMRSUser) result.get(OpenMRSUserService.USER_KEY);

        assertNotNull(saved);
        assertNotNull(saved.getUserId());
    }

    @Test
    public void shouldSetNewPassword() throws UserAlreadyExistsException {
        String newPassword = userAdapter.setNewPasswordForUser("chuck");

        assertNotNull(newPassword);
    }

    @Test
    public void shouldGetUserByUsername() throws UserAlreadyExistsException {
        OpenMRSUser user = (OpenMRSUser) userAdapter.getUserByUserName("chuck");

        assertNotNull(user);
    }

    @Test
    public void shouldGetAllUsers() throws UserAlreadyExistsException {
        List<OpenMRSUser> users = userAdapter.getAllUsers();

        assertTrue(users.size() > 0);
    }

    @Test
    public void shouldUpdateUser() throws UserAlreadyExistsException {
        OpenMRSUser saved = (OpenMRSUser) userAdapter.getUserByUserName("chuck");
        OpenMRSPerson person = (OpenMRSPerson)saved.getPerson();
        person.setFirstName("John2");
        userAdapter.updateUser(saved);

        OpenMRSUser updated = (OpenMRSUser) userAdapter.getUserByUserName("john2");

        assertNotNull(updated);
        assertEquals("John2", updated.getPerson().getFirstName());
    }
}
