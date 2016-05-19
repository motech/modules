package org.motechproject.openmrs.it;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.openmrs.domain.Person;
import org.motechproject.openmrs.domain.Role;
import org.motechproject.openmrs.domain.User;
import org.motechproject.openmrs.exception.UserAlreadyExistsException;
import org.motechproject.openmrs.exception.UserDeleteException;
import org.motechproject.openmrs.service.OpenMRSPersonService;
import org.motechproject.openmrs.service.OpenMRSUserService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.motechproject.openmrs.util.TestConstants.DEFAULT_CONFIG_NAME;

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

    private User userOne;
    private User userTwo;

    @Before
    public void setUp() throws UserAlreadyExistsException {
        userOne = createUser(prepareUserOne());
        userTwo = createUser(prepareUserTwo());
    }

    @Test
    public void shouldCreateUser() {
        assertEquals(USER_ONE_USER_NAME, userOne.getUsername());
        assertEquals(USER_ONE_SECURITY_ROLE, userOne.getRoles().get(0).getDisplay());
    }

    @Test
    public void shouldSetNewPassword() {

        String newPassword = userAdapter.setNewPasswordForUser(DEFAULT_CONFIG_NAME, userOne.getUsername());

        assertNotNull(newPassword);
    }

    @Test
    public void shouldGetUserById() {

        User user = userAdapter.getUserByUuid(DEFAULT_CONFIG_NAME, userOne.getUuid());

        assertNotNull(user);
        assertEquals(user, userOne);
    }

    @Test
    public void shouldGetUserByUsername() {

        User user = userAdapter.getUserByUserName(DEFAULT_CONFIG_NAME, userOne.getUsername());

        assertNotNull(user);
        assertEquals(user, userOne);
    }

    @Test
    public void shouldGetAllUsers() {

        List<User> users = userAdapter.getAllUsers(DEFAULT_CONFIG_NAME);

        // 4 including daemon and admin
        assertEquals(4, users.size());

        assertEquals(users.get(2).getUuid(), userOne.getUuid());
        assertEquals(users.get(3).getUuid(), userTwo.getUuid());
    }

    @Test
    public void shouldUpdateUser() {

        final String newUsername = "newFooUsername";

        userOne.setUsername(newUsername);

        User updated = userAdapter.updateUser(DEFAULT_CONFIG_NAME, userOne);

        assertNotNull(updated);
        assertEquals(updated.getUsername(), newUsername);
    }

    @After
    public void tearDown() throws UserDeleteException {
        deleteUser(userOne);
        deleteUser(userTwo);
    }

    private User prepareUserOne() {
        Person person = new Person();

        Person.Name name = new Person.Name();
        name.setGivenName("FooFirstNameOne");
        name.setFamilyName("FooLastNameOne");
        person.setNames(Collections.singletonList(name));

        person.setGender("M");

        Role role = new Role();
        role.setName(USER_ONE_SECURITY_ROLE);

        User user = new User();
        user.setUsername(USER_ONE_USER_NAME);
        user.setPerson(person);
        user.setRoles(Collections.singletonList(role));

        return user;
    }

    private User prepareUserTwo() {
        Person person = new Person();

        Person.Name name = new Person.Name();
        name.setGivenName("FooFirstNameTwo");
        name.setFamilyName("FooLastNameTwo");
        person.setNames(Collections.singletonList(name));

        person.setGender("O");

        Role role = new Role();
        role.setName("Provider");

        User user = new User();
        user.setUsername("fooUserTwo");
        user.setPerson(person);
        user.setRoles(Collections.singletonList(role));

        return user;
    }

    private User createUser(User user) throws UserAlreadyExistsException {

        User saved = userAdapter.createUser(DEFAULT_CONFIG_NAME, user);

        assertNotNull(saved);
        assertNotNull(saved.getUuid());

        return saved;
    }

    private void deleteUser(User user) throws UserDeleteException {

        userAdapter.deleteUser(DEFAULT_CONFIG_NAME, user.getUuid());
        assertNull(userAdapter.getUserByUserName(DEFAULT_CONFIG_NAME, user.getUsername()));

        personAdapter.deletePerson(DEFAULT_CONFIG_NAME, user.getPerson().getUuid());
    }
}
