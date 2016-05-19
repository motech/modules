package org.motechproject.openmrs.domain;

import com.google.gson.annotations.Expose;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single program enrollment. A program enrollment is a connection between patient and program.
 * A program enrollment stores information about program states assigned to a patient.
 */
public class ProgramEnrollment {

    private String uuid;

    @Expose
    private Program program;

    @Expose
    private Patient patient;

    @Expose
    private Date dateEnrolled;

    @Expose
    private Date dateCompleted;

    @Expose
    private Location location;

    @Expose
    private List<StateStatus> states;

    public static class StateStatus {

        private String uuid;

        @Expose
        private Program.State state;

        @Expose
        private Date startDate;

        @Expose
        private Date endDate;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public Program.State getState() {
            return state;
        }

        public void setState(Program.State state) {
            this.state = state;
        }

        public Date getStartDate() {
            return startDate;
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }

        @Override
        public int hashCode() {
            return Objects.hash(state, startDate, endDate);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }

            StateStatus other = (StateStatus) obj;

            return Objects.equals(this.state, other.state) && Objects.equals(this.startDate, other.startDate) &&
                    Objects.equals(this.endDate, other.endDate);
        }
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Date getDateEnrolled() {
        return dateEnrolled;
    }

    public void setDateEnrolled(Date dateEnrolled) {
        this.dateEnrolled = dateEnrolled;
    }

    public Date getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(Date dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<StateStatus> getStates() {
        return states;
    }

    public void setStates(List<StateStatus> states) {
        this.states = states;
    }

    @Override
    public int hashCode() {
        return Objects.hash(program, patient, dateEnrolled, dateCompleted, location, states);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        ProgramEnrollment other = (ProgramEnrollment) obj;

        return Objects.equals(this.program, other.program) && Objects.equals(this.patient, other.patient) &&
                Objects.equals(this.dateEnrolled, other.dateEnrolled) && Objects.equals(this.dateCompleted, other.dateCompleted) &&
                Objects.equals(this.location, other.location) && Objects.equals(this.states, other.states);
    }
}
