package org.motechproject.openmrs19.domain;

import java.util.Map;
import java.util.Objects;

/**
 * Represents a single patient. It's a part of MOTECH model.
 */
public class OpenMRSPatient {

    private String patientId;
    private OpenMRSFacility facility;
    private OpenMRSPerson person;
    private String motechId;
    private Map<String, String> identifierTypesByValue;

    public OpenMRSPatient() {
        this(null);
    }

    /**
     * Creates a patient with the given OpenMRS {@code patientId}.
     *
     * @param patientId  the OpenMRS ID of the patient
     */
    public OpenMRSPatient(String patientId) {
        this(patientId, null, null, null, null);
    }

    /**
     * Creates a patient with the given {@code motechId} based on the given {@code person} details and assigns it to the
     * given {@code facility}.
     *
     * @param motechId  the MOTECH ID of the patient
     * @param person  the personal details about the patient
     * @param facility  the facility by which the patient is treated
     */
    public OpenMRSPatient(String motechId, OpenMRSPerson person, OpenMRSFacility facility) {
        this(motechId, person, facility, null);
    }

    /**
     * Creates a patient with the given {@code motechId} and {@code identifierTypesByValue} based on the given {@code person}
     * details and assigns it to the given {@code facility}.
     *
     * @param motechId  the MOTECH ID of the patient
     * @param person  the personal details about the patient
     * @param facility  the facility by which the patient is treated
     * @param identifierTypesByValue the identifiers of patient by value
     */
    public OpenMRSPatient(String motechId, OpenMRSPerson person, OpenMRSFacility facility, Map<String, String> identifierTypesByValue) {
        this(null, motechId, person, facility, identifierTypesByValue);
    }

    /**
     * Creates a patient with the given {@code motechId}, OpenMRS {@code patientId} and {@code identifierTypesByValue}
     * based on the given {@code person} details and assigns it to the given {@code facility}.
     *
     * @param patientId  the OpenMRS ID of the patient
     * @param motechId  the MOTECH ID of the patient
     * @param person  the personal details about the patient
     * @param facility  the facility by which the patient is treated
     * @param identifierTypesByValue the identifiers of patient by value
     */
    public OpenMRSPatient(String patientId, String motechId, OpenMRSPerson person, OpenMRSFacility facility,
                          Map<String, String> identifierTypesByValue) {
        this.patientId = patientId;
        this.motechId = motechId;
        this.person = person;
        this.facility = facility;
        this.identifierTypesByValue = identifierTypesByValue;
    }

    public String getPatientId() {
        return patientId;
    }

    public OpenMRSFacility getFacility() {
        return facility;
    }

    public OpenMRSPerson getPerson() {
        return person;
    }

    public String getMotechId() {
        return motechId;
    }

    public Map<String, String> getIdentifierTypesByValue() {
        return identifierTypesByValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof OpenMRSPatient)) {
            return false;
        }

        OpenMRSPatient that = (OpenMRSPatient) o;

        return Objects.equals(facility, that.facility) && Objects.equals(patientId, that.patientId) &&
                Objects.equals(motechId, that.motechId) && Objects.equals(person, that.person) &&
                Objects.equals(identifierTypesByValue, that.identifierTypesByValue);
    }

    @Override
    public int hashCode() {
        int result = patientId != null ? patientId.hashCode() : 0;
        result = 31 * result + (facility != null ? facility.hashCode() : 0);
        result = 31 * result + (person != null ? person.hashCode() : 0);
        result = 31 * result + (motechId != null ? motechId.hashCode() : 0);
        result = 31 * result + (identifierTypesByValue != null ? identifierTypesByValue.hashCode() : 0);
        return result;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public void setFacility(OpenMRSFacility facility) {
        this.facility = facility;
    }

    public void setPerson(OpenMRSPerson person) {
        this.person = person;
    }

    public void setMotechId(String motechId) {
        this.motechId = motechId;
    }

    public void setIdentifierTypesByValue(Map<String, String> identifierTypesByValue) {
        this.identifierTypesByValue = identifierTypesByValue;
    }
}
