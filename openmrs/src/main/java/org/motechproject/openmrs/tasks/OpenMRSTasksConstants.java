package org.motechproject.openmrs.tasks;

/**
 * A class grouping OpenMRS constants, which are used in tasks module.
 */
public final class OpenMRSTasksConstants {
    public static final String NAME = "openMRS";
    public static final String PACKAGE_ROOT = "org.motechproject.openmrs.domain";

    // Lookup names
    public static final String BY_MOTECH_ID = "openMRS.lookup.motechId";
    public static final String BY_UUID = "openMRS.lookup.uuid";
    public static final String BY_PERSON_UUID = "openMRS.person";
    public static final String BY_MOTECH_ID_AND_PROGRAM_NAME = "openMRS.lookup.motechIdAndProgramName";
    public static final String BY_UUID_AMD_PROGRAM_NAME = "openMRS.lookup.uuidAndProgramName";

    // Lookup objects
    public static final String ENCOUNTER = "Encounter";
    public static final String PATIENT = "Patient";
    public static final String PROVIDER = "Provider";
    public static final String RELATIONSHIP = "Relationship";
    public static final String PROGRAM_ENROLLMENT = "ProgramEnrollment";
    public static final String BAHMNI_PROGRAM_ENROLLMENT = "BahmniProgramEnrollment";
    public static final String IDENTIFIER = "GeneratedIdentifier";

    // Field names
    public static final String MOTECH_ID = "openMRS.motechId";
    public static final String UUID = "openMRS.uuid";
    public static final String PATIENT_MOTECH_ID = "openMRS.patient.motechId";
    public static final String PATIENT_UUID = "openMRS.patient.uuid";
    public static final String PERSON_UUID = "openMRS.person.uuid";
    public static final String RELATIONSHIP_TYPE_UUID = "openMRS.relationshipType.uuid";
    public static final String PROGRAM_NAME = "openMRS.programName";
    public static final String PROGRAM_ENROLLMENT_UUID = "openMRS.programEnrollment.uuid";
    public static final String ACTIVE_PROGRAM = "openMRS.activeProgramOnly";
    public static final String IDENTIFIER_SOURCE_NAME = "openMRS.identifier.sourceName";

    private OpenMRSTasksConstants() {

    }
}
