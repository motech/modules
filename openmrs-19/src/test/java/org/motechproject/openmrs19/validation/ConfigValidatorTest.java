package org.motechproject.openmrs19.validation;

import org.junit.Test;
import org.motechproject.openmrs19.config.Config;

import java.util.ArrayList;

import static org.motechproject.openmrs19.validation.ConfigValidator.validateConfig;

public class ConfigValidatorTest {

    private static final String NAME = "name";
    private static final String OPEN_MRS_URL = "openMrsUlr";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String MOTECH_PATIENT_IDENTIFIER_TYPE_NAME = "motechId";
    private static final String EMPTY_STRING = "";

    @Test
    public void shouldPassTheValidationIfAllTheFieldsAreNonEmpty() {
        Config config = new Config();
        config.setName(NAME);
        config.setOpenMrsUrl(OPEN_MRS_URL);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        config.setMotechPatientIdentifierTypeName(MOTECH_PATIENT_IDENTIFIER_TYPE_NAME);
        config.setPatientIdentifierTypeNames(new ArrayList<>());

        validateConfig(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfNameIsNull() {
        Config config = new Config();
        config.setName(null);
        config.setOpenMrsUrl(OPEN_MRS_URL);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        config.setMotechPatientIdentifierTypeName(MOTECH_PATIENT_IDENTIFIER_TYPE_NAME);
        config.setPatientIdentifierTypeNames(new ArrayList<>());

        validateConfig(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfNameIsEmpty() {
        Config config = new Config();
        config.setName(EMPTY_STRING);
        config.setOpenMrsUrl(OPEN_MRS_URL);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        config.setMotechPatientIdentifierTypeName(MOTECH_PATIENT_IDENTIFIER_TYPE_NAME);
        config.setPatientIdentifierTypeNames(new ArrayList<>());

        validateConfig(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfOpenMrsUrlIsNull() {
        Config config = new Config();
        config.setName(NAME);
        config.setOpenMrsUrl(null);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        config.setMotechPatientIdentifierTypeName(MOTECH_PATIENT_IDENTIFIER_TYPE_NAME);
        config.setPatientIdentifierTypeNames(new ArrayList<>());

        validateConfig(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfOpenMrsUrlIsEmpty() {
        Config config = new Config();
        config.setName(NAME);
        config.setOpenMrsUrl(EMPTY_STRING);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        config.setMotechPatientIdentifierTypeName(MOTECH_PATIENT_IDENTIFIER_TYPE_NAME);
        config.setPatientIdentifierTypeNames(new ArrayList<>());

        validateConfig(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfUsernameIsNull() {
        Config config = new Config();
        config.setName(NAME);
        config.setOpenMrsUrl(OPEN_MRS_URL);
        config.setUsername(null);
        config.setPassword(PASSWORD);
        config.setMotechPatientIdentifierTypeName(MOTECH_PATIENT_IDENTIFIER_TYPE_NAME);
        config.setPatientIdentifierTypeNames(new ArrayList<>());

        validateConfig(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfUsernameIsEmpty() {
        Config config = new Config();
        config.setName(NAME);
        config.setOpenMrsUrl(OPEN_MRS_URL);
        config.setUsername(EMPTY_STRING);
        config.setPassword(PASSWORD);
        config.setMotechPatientIdentifierTypeName(MOTECH_PATIENT_IDENTIFIER_TYPE_NAME);
        config.setPatientIdentifierTypeNames(new ArrayList<>());

        validateConfig(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfPasswordIsNull() {
        Config config = new Config();
        config.setName(NAME);
        config.setOpenMrsUrl(OPEN_MRS_URL);
        config.setUsername(USERNAME);
        config.setPassword(null);
        config.setMotechPatientIdentifierTypeName(MOTECH_PATIENT_IDENTIFIER_TYPE_NAME);
        config.setPatientIdentifierTypeNames(new ArrayList<>());

        validateConfig(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfPasswordIsEmpty() {
        Config config = new Config();
        config.setName(NAME);
        config.setOpenMrsUrl(OPEN_MRS_URL);
        config.setUsername(USERNAME);
        config.setPassword(EMPTY_STRING);
        config.setMotechPatientIdentifierTypeName(MOTECH_PATIENT_IDENTIFIER_TYPE_NAME);
        config.setPatientIdentifierTypeNames(new ArrayList<>());

        validateConfig(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfMotechPatientIdentifierTypeNameIsNull() {
        Config config = new Config();
        config.setName(NAME);
        config.setOpenMrsUrl(OPEN_MRS_URL);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        config.setMotechPatientIdentifierTypeName(null);
        config.setPatientIdentifierTypeNames(new ArrayList<>());

        validateConfig(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfMotechPatientIdentifierTypeNameIsEmpty() {
        Config config = new Config();
        config.setName(NAME);
        config.setOpenMrsUrl(OPEN_MRS_URL);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        config.setMotechPatientIdentifierTypeName(EMPTY_STRING);
        config.setPatientIdentifierTypeNames(new ArrayList<>());

        validateConfig(config);
    }
}