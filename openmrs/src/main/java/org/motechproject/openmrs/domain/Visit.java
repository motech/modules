package org.motechproject.openmrs.domain;

import java.util.Date;
import java.util.Objects;

/**
 * Represents a single OpenMRS visit.
 */
public class Visit {

    private String uuid;
    private Location location;
    private VisitType visitType;
    private Date startDatetime;
    private Date stopDatetime;
    private Patient patient;

    /**
     * Default constructor.
     */
    public Visit() {
    }

    public Visit(Date startDatetime, Date stopDatetime, Patient patient, VisitType visitType) {
        this(startDatetime, stopDatetime, patient, visitType, null);
    }

    public Visit(Date startDatetime, Date stopDatetime, Patient patient, VisitType visitType, Location location) {
        this.startDatetime = startDatetime;
        this.stopDatetime = stopDatetime;
        this.patient = patient;
        this.visitType = visitType;
        this.location = location;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public VisitType getVisitType() {
        return visitType;
    }

    public void setVisitType(VisitType visitType) {
        this.visitType = visitType;
    }

    public Date getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(Date startDatetime) {
        this.startDatetime = startDatetime;
    }

    public Date getStopDatetime() {
        return stopDatetime;
    }

    public void setStopDatetime(Date stopDatetime) {
        this.stopDatetime = stopDatetime;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, location, visitType, startDatetime, stopDatetime, patient);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Visit visit = (Visit) o;
        return Objects.equals(uuid, visit.uuid) &&
                Objects.equals(location, visit.location) &&
                Objects.equals(visitType, visit.visitType) &&
                Objects.equals(startDatetime, visit.startDatetime) &&
                Objects.equals(stopDatetime, visit.stopDatetime) &&
                Objects.equals(patient, visit.patient);
    }
}
