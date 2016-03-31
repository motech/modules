package org.motechproject.openmrs19.it;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.openmrs19.domain.OpenMRSPerson;
import org.motechproject.openmrs19.domain.OpenMRSUser;
import org.motechproject.openmrs19.exception.UserAlreadyExistsException;
import org.motechproject.openmrs19.exception.UserDeleteException;
import org.motechproject.openmrs19.service.OpenMRSPersonService;
import org.motechproject.openmrs19.service.OpenMRSUserService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class MRSUserServiceIT extends BasePaxIT {

    private static final String USER_ONE_USER_NAME = "fooUserNameOne";
    private static final String USER_ONE_SECURITY_ROLE = "Provider";

    @Inject
    private OpenMRSUserService userAdapter;

    @Inject
    private OpenMRSPersonService personAdapter;

    private OpenMRSUser userOne;
    private OpenMRSUser userTwo;

    @Before
    public void setUp() throws UserAlreadyExistsException {
        userOne = createUser(prepareUserOne());
        userTwo = createUser(prepareUserTwo());
    }

    @Test
    public void shouldCreateUser() {
        assertEquals(userOne.getUserName(), USER_ONE_USER_NAME);
        assertEquals(userOne.getSecurityRole(), USER_ONE_SECURITY_ROLE);
    }

    @Test
    public void shouldSetNewPassword() {

        String newPassword = userAdapter.setNewPasswordForUser(userOne.getUserName());

        assertNotNull(newPassword);
    }

    @Test
    public void shouldGetUserById() {

        OpenMRSUser user = userAdapter.getUserByUuid(userOne.getUserId());

        assertNotNull(user);
        assertEquals(user, userOne);
    }

    @Test
    public void shouldGetUserByUsername() {

        OpenMRSUser user = userAdapter.getUserByUserName(userOne.getUserName());

        assertNotNull(user);
        assertEquals(user, userOne);
    }

    @Test
    public void shouldGetAllUsers() {

        List<OpenMRSUser> users = userAdapter.getAllUsers();

        // 4 including daemon and admin
        assertEquals(4, users.size());

        assertEquals(users.get(2).getUserId(), userOne.getUserId());
        assertEquals(users.get(3).getUserId(), userTwo.getUserId());
    }

    @Test
    public void shouldUpdateUser() {

        final String newUsername = "newFooUsername";

        userOne.setUserName(newUsername);

        OpenMRSUser updated = userAdapter.updateUser(userOne);

        assertNotNull(updated);
        assertEquals(updated.getUserName(), newUsername);
    }

    @After
    public void tearDown() throws UserDeleteException {
        deleteUser(userOne);
        deleteUser(userTwo);
    }

    private OpenMRSUser prepareUserOne() {
        OpenMRSPerson person = new OpenMRSPerson();
        person.setFirstName("FooFirstNameOne");
        person.setLastName("FooLastNameOne");
        person.setGender("M");

        OpenMRSUser user = new OpenMRSUser();
        user.setSecurityRole(USER_ONE_SECURITY_ROLE);
        user.setUserName(USER_ONE_USER_NAME);
        user.setPerson(person);

        return user;
    }

    private OpenMRSUser prepareUserTwo() {
        OpenMRSPerson person = new OpenMRSPerson();
        person.setFirstName("FooFirstNameTwo");
        person.setLastName("FooLastNameTwo");
        person.setGender("O");

        OpenMRSUser user = new OpenMRSUser();
        user.setSecurityRole("Provider");
        user.setUserName("fooUserTwo");
        user.setPerson(person);

        return user;
    }

    private OpenMRSUser createUser(OpenMRSUser user) throws UserAlreadyExistsException {

        OpenMRSUser saved = userAdapter.createUser(user);

        assertNotNull(saved);
        assertNotNull(saved.getUserId());

        return saved;
    }

    private void deleteUser(OpenMRSUser user) throws UserDeleteException {

        userAdapter.deleteUser(user.getUserId());
        assertNull(userAdapter.getUserByUserName(user.getUserName()));

        personAdapter.deletePerson(user.getPerson().getPersonId());
    }
}
