package org.motechproject.openmrs19.domain;

import java.util.HashMap;
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
    private Map<String, String> identifiers;

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
     * Creates a patient with the given {@code motechId} and {@code identifiers} based on the given {@code person}
     * details and assigns it to the given {@code facility}.
     *
     * @param motechId  the MOTECH ID of the patient
     * @param person  the personal details about the patient
     * @param facility  the facility by which the patient is treated
     * @param identifiers the supported identifiers of patient, key - identifier type name, value - identifier number
     */
    public OpenMRSPatient(String motechId, OpenMRSPerson person, OpenMRSFacility facility, Map<String, String> identifiers) {
        this(null, motechId, person, facility, identifiers);
    }

    /**
     * Creates a patient with the given {@code motechId}, OpenMRS {@code patientId} and {@code identifiers}
     * based on the given {@code person} details and assigns it to the given {@code facility}.
     *
     * @param patientId  the OpenMRS ID of the patient
     * @param motechId  the MOTECH ID of the patient
     * @param person  the personal details about the patient
     * @param facility  the facility by which the patient is treated
     * @param identifiers the supported identifiers of patient, key - identifier type name, value - identifier number
     */
    public OpenMRSPatient(String patientId, String motechId, OpenMRSPerson person, OpenMRSFacility facility,
                          Map<String, String> identifiers) {
        this.patientId = patientId;
        this.motechId = motechId;
        this.person = person;
        this.facility = facility;
        this.identifiers = identifiers;
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

    public Map<String, String> getIdentifiers() {
        return identifiers == null ? new HashMap<>() : identifiers;
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
                Objects.equals(identifiers, that.identifiers);
    }

    @Override
    public int hashCode() {
        int result = patientId != null ? patientId.hashCode() : 0;
        result = 31 * result + (facility != null ? facility.hashCode() : 0);
        result = 31 * result + (person != null ? person.hashCode() : 0);
        result = 31 * result + (motechId != null ? motechId.hashCode() : 0);
        result = 31 * result + (identifiers != null ? identifiers.hashCode() : 0);
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

    public void setIdentifiers(Map<String, String> identifiers) {
        this.identifiers = identifiers;
    }
}
