package org.motechproject.openmrs19.domain;

import ch.lambdaj.Lambda;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

/**
 * Maintains patient's data collected during visits (Encounters)
 *
 * @param <T> Type of the observation's value
 */
public class OpenMRSObservation<T> {

    private String id;
    private Date date;
    private String conceptName;
    private String patientId;
    private T value;
    private Set<OpenMRSObservation> dependentObservations;

    /**
     * Creates an observation object with the given details
     *
     * @param date        Date of the observation
     * @param conceptName Name of the concept
     * @param value       Value of the observation
     */
    public OpenMRSObservation(Date date, String conceptName, T value) {
        this.date = date;
        this.conceptName = conceptName;
        this.value = value;
    }

    public OpenMRSObservation(Date date, String conceptName, String patientId, T value) {
        this(date, conceptName, value);
        this.patientId = patientId;
    }

    /**
     * Creates an observation object with the given details
     *
     * @param id          Observation id
     * @param date        Date of the observation
     * @param conceptName Name of the concept
     * @param value       Value of the observation
     */
    public OpenMRSObservation(String id, Date date, String conceptName, T value) {
        this(date, conceptName, value);
        this.id = id;
    }

    @Deprecated
    public String getId() {
        return id;
    }

    public DateTime getDate() {
        return (date != null) ? new DateTime(date) : null;
    }

    public String getConceptName() {
        return conceptName;
    }

    public T getValue() {
        return value;
    }

    public Set<? extends OpenMRSObservation> getDependentObservations() {
        return dependentObservations;
    }

    public void addDependentObservation(OpenMRSObservation mrsObservation) {
        if (this.dependentObservations == null) {
            dependentObservations = new HashSet<>();
        }
        //to remove duplicate observation
        List<? extends OpenMRSObservation> existingObservationList = Lambda.filter(having(on(OpenMRSObservation.class).getConceptName(),
            is(equalTo(mrsObservation.getConceptName()))), dependentObservations);
        if (!existingObservationList.isEmpty()) {
            dependentObservations.remove(existingObservationList.get(0));
        }
        dependentObservations.add(mrsObservation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof OpenMRSObservation)) {
            return false;
        }

        OpenMRSObservation that = (OpenMRSObservation) o;

        return Objects.equals(conceptName, that.conceptName) && Objects.equals(date, that.date) &&
                Objects.equals(dependentObservations, that.dependentObservations) && Objects.equals(id, that.id) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (conceptName != null ? conceptName.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (dependentObservations != null ? dependentObservations.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MRSObservation{" +
                "id='" + id + '\'' +
                ", date=" + date +
                ", conceptName='" + conceptName + '\'' +
                ", value=" + value +
                '}';
    }

    public String getObservationId() {
        return id;
    }

    public void setObservationId(String observationId) {
        this.id = observationId;
    }

    public void setDate(DateTime date) {
        this.date = date.toDate();
    }

    public void setConceptName(String conceptName) {
        this.conceptName = conceptName;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public void setDependentObservations(Set<OpenMRSObservation> dependentObservations) {
        this.dependentObservations =  dependentObservations;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
}
