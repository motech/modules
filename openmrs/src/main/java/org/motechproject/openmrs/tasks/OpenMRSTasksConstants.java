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

    // Lookup objects
    public static final String ENCOUNTER = "Encounter";
    public static final String PATIENT = "Patient";
    public static final String PROVIDER = "Provider";
    public static final String RELATIONSHIP = "Relationship";

    // Field names
    public static final String MOTECH_ID = "openMRS.motechId";
    public static final String UUID = "openMRS.uuid";
    public static final String PERSON_UUID = "openMRS.person.uuid";
    public static final String RELATIONSHIP_TYPE_UUID = "openMRS.relationshipType.uuid";

    private OpenMRSTasksConstants() {

    }
}
