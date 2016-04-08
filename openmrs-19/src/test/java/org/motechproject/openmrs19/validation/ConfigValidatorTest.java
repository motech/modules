package org.motechproject.openmrs19.validation;

import org.junit.Test;
import org.motechproject.openmrs19.config.Config;

import static org.motechproject.openmrs19.validation.ConfigValidator.validateConfig;

public class ConfigValidatorTest {

    private static final String NAME = "name";
    private static final String OPEN_MRS_URL = "openMrsUlr";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String MOTECH_ID = "motechId";
    private static final String EMPTY_STRING = "";

    @Test
    public void shouldPassTheValidationIfAllTheFieldsAreNonEmpty() {
        Config config = new Config();
        config.setName(NAME);
        config.setOpenMrsUrl(OPEN_MRS_URL);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        config.setMotechId(MOTECH_ID);

        validateConfig(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfNameIsNull() {
        Config config = new Config();
        config.setName(null);
        config.setOpenMrsUrl(OPEN_MRS_URL);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        config.setMotechId(MOTECH_ID);

        validateConfig(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfNameIsEmpty() {
        Config config = new Config();
        config.setName(EMPTY_STRING);
        config.setOpenMrsUrl(OPEN_MRS_URL);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        config.setMotechId(MOTECH_ID);

        validateConfig(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfOpenMrsUrlIsNull() {
        Config config = new Config();
        config.setName(NAME);
        config.setOpenMrsUrl(null);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        config.setMotechId(MOTECH_ID);

        validateConfig(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfOpenMrsUrlIsEmpty() {
        Config config = new Config();
        config.setName(NAME);
        config.setOpenMrsUrl(EMPTY_STRING);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        config.setMotechId(MOTECH_ID);

        validateConfig(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfUsernameIsNull() {
        Config config = new Config();
        config.setName(NAME);
        config.setOpenMrsUrl(OPEN_MRS_URL);
        config.setUsername(null);
        config.setPassword(PASSWORD);
        config.setMotechId(MOTECH_ID);

        validateConfig(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfUsernameIsEmpty() {
        Config config = new Config();
        config.setName(NAME);
        config.setOpenMrsUrl(OPEN_MRS_URL);
        config.setUsername(EMPTY_STRING);
        config.setPassword(PASSWORD);
        config.setMotechId(MOTECH_ID);

        validateConfig(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfPasswordIsNull() {
        Config config = new Config();
        config.setName(NAME);
        config.setOpenMrsUrl(OPEN_MRS_URL);
        config.setUsername(USERNAME);
        config.setPassword(null);
        config.setMotechId(MOTECH_ID);

        validateConfig(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfPasswordIsEmpty() {
        Config config = new Config();
        config.setName(NAME);
        config.setOpenMrsUrl(OPEN_MRS_URL);
        config.setUsername(USERNAME);
        config.setPassword(EMPTY_STRING);
        config.setMotechId(MOTECH_ID);

        validateConfig(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfMotechIdIsNull() {
        Config config = new Config();
        config.setName(NAME);
        config.setOpenMrsUrl(OPEN_MRS_URL);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        config.setMotechId(null);

        validateConfig(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfMotechIdIsEmpty() {
        Config config = new Config();
        config.setName(NAME);
        config.setOpenMrsUrl(OPEN_MRS_URL);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        config.setMotechId(EMPTY_STRING);

        validateConfig(config);
    }

}