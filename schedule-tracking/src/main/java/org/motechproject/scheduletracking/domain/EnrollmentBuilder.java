package org.motechproject.scheduletracking.domain;

import org.joda.time.DateTime;
import org.motechproject.commons.date.model.Time;

import java.util.List;
import java.util.Map;

/**
 * The <code>EnrollmentBuilder</code> is a convenient class, that allows building {@link org.motechproject.scheduletracking.domain.Enrollment}
 * instances. Once all required fields are set, call <code>toEnrollment()</code> method to get an instance of
 * enrollment with the properties you have set.
 */
public class EnrollmentBuilder {

    private Enrollment enrollment;

    /**
     * Creates an EnrollmentBuilder.
     */
    public EnrollmentBuilder() {
        enrollment = new Enrollment();
    }

    /**
     * Adds the metadata for the built enrollment.
     *
     * @param metadata the metadata to add
     * @return this instance with the metadata set up
     */
    public EnrollmentBuilder withMetadata(Map<String, String> metadata) {
        enrollment.setMetadata(metadata);
        return this;
    }

    /**
     * Adds the schedule name for the built enrollment.
     *
     * @param scheduleName the name of the schedule
     * @return this instance with the schedule name set up
     */
    public EnrollmentBuilder withScheduleName(String scheduleName) {
        enrollment.setScheduleName(scheduleName);
        return this;
    }

    /**
     * Adds the external id for the built enrollment.
     *
     * @param externalId the client external id
     * @return this instance with the external id set up
     */
    public EnrollmentBuilder withExternalId(String externalId) {
        enrollment.setExternalId(externalId);
        return this;
    }

    /**
     * Adds the start reference date and time for the built enrollment.
     *
     * @param startOfSchedule the start reference date and time
     * @return this instance with the start reference date and time set up
     */
    public EnrollmentBuilder withStartOfSchedule(DateTime startOfSchedule) {
        enrollment.setStartOfSchedule(startOfSchedule);
        return this;
    }

    /**
     * Adds the enrollment date and time for the built enrollment.
     *
     * @param enrolledOn the enrollment date and time
     * @return this instance with the enrollment date and time set up
     */
    public EnrollmentBuilder withEnrolledOn(DateTime enrolledOn) {
        enrollment.setEnrolledOn(enrolledOn);
        return this;
    }

    /**
     * Adds the preferred alert time name for the built enrollment.
     *
     * @param preferredAlertTime the preferred alert time
     * @return this instance with the preferred alert time name set up
     */
    public EnrollmentBuilder withPreferredAlertTime(Time preferredAlertTime) {
        enrollment.setPreferredAlertTime(preferredAlertTime);
        return this;
    }

    /**
     * Adds the milestone fulfillments for the built enrollment.
     *
     * @param fulfillments the milestone fulfillments
     * @return this instance with the milestone fulfillments  set up
     */
    public EnrollmentBuilder withFulfillments(List<MilestoneFulfillment> fulfillments) {
        enrollment.setFulfillments(fulfillments);
        return this;
    }

    /**
     * Adds the schedule for the built enrollment.
     *
     * @param schedule the schedule to which the enrollment belongs
     * @return this instance with the schedule set up
     */
    public EnrollmentBuilder withSchedule(Schedule schedule) {
        enrollment.setSchedule(schedule);
        enrollment.setScheduleName(schedule.getName());
        return this;
    }

    /**
     * Adds the current milestone name for the built enrollment.
     *
     * @param currentMilestoneName the current milestone name
     * @return this instance with the current milestone name set up
     */
    public EnrollmentBuilder withCurrentMilestoneName(String currentMilestoneName) {
        enrollment.setCurrentMilestoneName(currentMilestoneName);
        return this;
    }

    /**
     * Adds the status for the built enrollment.
     *
     * @param status the status of the enrollment
     * @return this instance with the status set up
     */
    public EnrollmentBuilder withStatus(EnrollmentStatus status) {
        enrollment.setStatus(status);
        return this;
    }

    /**
     * Adds the id for the built enrollment.
     *
     * @param id the id of the enrollment
     * @return this instance with the id set up
     */
    public EnrollmentBuilder withId(Long id) {
        enrollment.setId(id);
        return this;
    }

    /**
     * Returns an instance of enrollment with the properties you have set.
     *
     * @return the enrollment
     */
    public Enrollment toEnrollment() {
        return enrollment;
    }
}
