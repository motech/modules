package org.motechproject.openmrs19.resource;

import org.motechproject.openmrs19.config.Config;
import org.motechproject.openmrs19.domain.Identifier;
import org.motechproject.openmrs19.domain.Patient;
import org.motechproject.openmrs19.domain.PatientListResult;

import java.util.List;

/**
 * Interface for patients management.
 */
public interface PatientResource {

    /**
     * Creates the given patient on the OpenMRS server. The given {@code config} will be used while performing this
     * action.
     *
     * @param config  the configuration to be used while performing this action
     * @param patient  the patient to be created
     * @return  the saved patient
     */
    Patient createPatient(Config config, Patient patient);

    /**
     * Returns {@code PatientListResult} of all patients matching given term. The given {@code config} will be used
     * while performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param term  the term to be matched
     * @return  the list of matching patients
     */
    PatientListResult queryForPatient(Config config, String term);

    /**
     * Gets patient by its UUID. The given {@code config} will be used while performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param patientId  the UUID of the patient
     * @return  the patient with the given UUID.
     */
    Patient getPatientById(Config config, String patientId);

    /**
     * Returns the UUID of the MOTECH patient identifier. The given {@code config} will be used while performing this
     * action.
     *
     * @param config  the configuration to be used while performing this action
     * @return  the UUID of the MOTECH patient identifier
     */
    String getMotechPatientIdentifierUuid(Config config);

    /**
     * Returns the patient's identifier list.
     *
     * @param config  the configuration to be used while performing this action
     * @param patientUuid the UUID of the patient
     * @return  the identifier list of the patient
     */
    List<Identifier> getPatientIdentifierList(Config config, String patientUuid);

    /**
     * Returns the patient identifier type name for the given uuid only if the identifier type is supported by MOTECH.
     * This method is using cache while retrieving data from an OpenMRS server. The given {@code config} will be used
     * while performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param identifierTypeUuid the UUID of the patient identifier type
     * @return the name of the patient identifier type
     */
    String getPatientIdentifierTypeNameByUuid(Config config, String identifierTypeUuid);

    /**
     * Returns the UUID of the patient identifier type for the given name only if the identifier type is supported by
     * MOTECH. This method is using cache while retrieving data from an OpenMRS server. The given {@code config} will
     * be used while performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param identifierTypeName the name of the patient identifier type
     * @return the UUID of the patient identifier type
     */
    String getPatientIdentifierTypeUuidByName(Config config, String identifierTypeName);

    /**
     * Deletes the patient with the given UUID. The given {@code config} will be used while performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param uuid  the UUID of the patient
     */
    void deletePatient(Config config, String uuid);

    /**
     * Updates the MOTECH Id for the patient with given UUID. The given {@code config} will be used while performing
     * this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param patientUuid  the UUID of the patient
     * @param newMotechId  the new MOTECH Id
     */
    void updatePatientMotechId(Config config, String patientUuid, String newMotechId);

    /**
     * Updates the identifier for the patient with given UUID. The given {@code config} will be used while performing
     * this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param patientUuid  the UUID of the patient
     * @param updatedIdentifier  the updated patient identifier
     */
    void updatePatientIdentifier(Config config, String patientUuid, Identifier updatedIdentifier);
}
