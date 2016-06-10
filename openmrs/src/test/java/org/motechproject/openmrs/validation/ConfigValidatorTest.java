package org.motechproject.openmrs.validation;

import org.junit.Test;
import org.motechproject.openmrs.config.Config;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.openmrs.validation.ConfigValidator.validateConfig;

public class ConfigValidatorTest {

    private static final String NAME = "name";
    private static final String OPEN_MRS_VERSION = "openMrsVersion";
    private static final String OPEN_MRS_URL = "openMrsUlr";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String MOTECH_PATIENT_IDENTIFIER_TYPE_NAME = "motechId";
    private static final String EMPTY_STRING = "";

    @Test
    public void shouldPassTheValidationIfAllTheFieldsAreNonEmpty() {

        configValidator(NAME, OPEN_MRS_VERSION, OPEN_MRS_URL, USERNAME, PASSWORD, MOTECH_PATIENT_IDENTIFIER_TYPE_NAME, new ArrayList<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfNameIsNull() {

        configValidator(null, OPEN_MRS_VERSION, OPEN_MRS_URL, USERNAME, PASSWORD, MOTECH_PATIENT_IDENTIFIER_TYPE_NAME, new ArrayList<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfNameIsEmpty() {

        configValidator(EMPTY_STRING, OPEN_MRS_VERSION, OPEN_MRS_URL, USERNAME, PASSWORD, MOTECH_PATIENT_IDENTIFIER_TYPE_NAME, new ArrayList<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfVersionIsNull() {

        configValidator(NAME, null, OPEN_MRS_URL, USERNAME, PASSWORD, MOTECH_PATIENT_IDENTIFIER_TYPE_NAME, new ArrayList<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfVersionIsEmpty() {

        configValidator(NAME, EMPTY_STRING, OPEN_MRS_URL, USERNAME, PASSWORD, MOTECH_PATIENT_IDENTIFIER_TYPE_NAME, new ArrayList<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfOpenMrsUrlIsNull() {

        configValidator(NAME, OPEN_MRS_VERSION, null, USERNAME, PASSWORD, MOTECH_PATIENT_IDENTIFIER_TYPE_NAME, new ArrayList<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfOpenMrsUrlIsEmpty() {

        configValidator(NAME, OPEN_MRS_VERSION, EMPTY_STRING, USERNAME, PASSWORD, MOTECH_PATIENT_IDENTIFIER_TYPE_NAME, new ArrayList<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfUsernameIsNull() {

        configValidator(NAME, OPEN_MRS_VERSION, OPEN_MRS_URL, null, PASSWORD, MOTECH_PATIENT_IDENTIFIER_TYPE_NAME, new ArrayList<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfUsernameIsEmpty() {

        configValidator(NAME, OPEN_MRS_VERSION, OPEN_MRS_URL, EMPTY_STRING, PASSWORD, MOTECH_PATIENT_IDENTIFIER_TYPE_NAME, new ArrayList<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfPasswordIsNull() {

        configValidator(NAME, OPEN_MRS_VERSION, OPEN_MRS_URL, USERNAME, null, MOTECH_PATIENT_IDENTIFIER_TYPE_NAME, new ArrayList<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfPasswordIsEmpty() {

        configValidator(NAME, OPEN_MRS_VERSION, OPEN_MRS_URL, USERNAME, EMPTY_STRING, MOTECH_PATIENT_IDENTIFIER_TYPE_NAME, new ArrayList<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfMotechPatientIdentifierTypeNameIsNull() {

        configValidator(NAME, OPEN_MRS_VERSION, OPEN_MRS_URL, USERNAME, PASSWORD, null, new ArrayList<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfMotechPatientIdentifierTypeNameIsEmpty() {

        configValidator(NAME, OPEN_MRS_VERSION, OPEN_MRS_URL, USERNAME, PASSWORD, EMPTY_STRING, new ArrayList<>());
    }

    private void configValidator(String name, String version, String url, String username, String password, String motechPatientIdentifierTypeName, List<String> patientIdentifierTypeNames) {
        Config config = new Config();
        config.setName(name);
        config.setOpenMrsVersion(version);
        config.setOpenMrsUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setMotechPatientIdentifierTypeName(motechPatientIdentifierTypeName);
        config.setPatientIdentifierTypeNames(patientIdentifierTypeNames);

        validateConfig(config);
    }
}