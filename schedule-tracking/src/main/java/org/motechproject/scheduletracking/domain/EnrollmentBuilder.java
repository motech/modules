package org.motechproject.scheduletracking.domain;

import org.joda.time.DateTime;
import org.motechproject.commons.date.model.Time;

import java.util.List;
import java.util.Map;

/**
 * The <code>EnrollmentBuilder</code> is a convenient class, that allows building {@link Enrollment} instances.
 * Once all required fields are set, call <code>toEnrollment()</code> method to get an instance of
 * Enrollment with the properties you have set.
 */
public class EnrollmentBuilder {

    private Enrollment enrollment;

    public EnrollmentBuilder() {
        enrollment = new Enrollment();
    }

    public EnrollmentBuilder withMetadata(Map<String, String> metadata) {
        enrollment.setMetadata(metadata);
        return this;
    }

    public EnrollmentBuilder withScheduleName(String scheduleName) {
        enrollment.setScheduleName(scheduleName);
        return this;
    }

    public EnrollmentBuilder withExternalId(String externalId) {
        enrollment.setExternalId(externalId);
        return this;
    }

    public EnrollmentBuilder withStartOfSchedule(DateTime startOfSchedule) {
        enrollment.setStartOfSchedule(startOfSchedule);
        return this;
    }

    public EnrollmentBuilder withEnrolledOn(DateTime enrolledOn) {
        enrollment.setEnrolledOn(enrolledOn);
        return this;
    }

    public EnrollmentBuilder withPreferredAlertTime(Time preferredAlertTime) {
        enrollment.setPreferredAlertTime(preferredAlertTime);
        return this;
    }

    public EnrollmentBuilder withFulfillments(List<MilestoneFulfillment> fulfillments) {
        enrollment.setFulfillments(fulfillments);
        return this;
    }

    public EnrollmentBuilder withSchedule(Schedule schedule) {
        enrollment.setSchedule(schedule);
        enrollment.setScheduleName(schedule.getName());
        return this;
    }

    public EnrollmentBuilder withCurrentMilestoneName(String currentMilestoneName) {
        enrollment.setCurrentMilestoneName(currentMilestoneName);
        return this;
    }

    public EnrollmentBuilder withStatus(EnrollmentStatus status) {
        enrollment.setStatus(status);
        return this;
    }

    public EnrollmentBuilder withId(Long id) {
        enrollment.setId(id);
        return this;
    }

    public Enrollment toEnrollment() {
        return enrollment;
    }
}
