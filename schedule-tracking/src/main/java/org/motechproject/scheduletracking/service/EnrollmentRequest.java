package org.motechproject.scheduletracking.service;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.commons.date.model.Time;

import java.util.HashMap;
import java.util.Map;

import static org.motechproject.commons.date.util.DateUtil.newDateTime;
import static org.motechproject.commons.date.util.DateUtil.today;

/**
 * <code>EnrollmentRequest</code> represents the request document to create an enrollment.
 * @see org.motechproject.scheduletracking.service.ScheduleTrackingService
 */
public class EnrollmentRequest {

    private String externalId;

    private String scheduleName;

    private Time preferredAlertTime;

    private LocalDate referenceDate;

    private Time referenceTime;

    private LocalDate enrollmentDate;

    private Time enrollmentTime;

    private String startingMilestoneName;

    private Map<String, String> metadata;

    public EnrollmentRequest() {
        this.referenceDate = today();
        this.referenceTime = new Time(0, 0);
        this.enrollmentDate = today();
        this.enrollmentTime = new Time(0, 0);
        this.metadata = new HashMap<>();
    }

    /**
     * Returns the external id of the enrollment.
     * @return the enrollment external id
     */
    public String getExternalId() {
        return externalId;
    }

    /**
     * Returns the schedule name of the enrollment.
     * @return the schedule name
     */
    public String getScheduleName() {
        return scheduleName;
    }

    /**
     * Returns the starting milestone name of the enrollment.
     * @return the starting milestone name
     */
    public String getStartingMilestoneName() {
        return startingMilestoneName;
    }

    /**
     * Returns the preferred alert time of the enrollment.
     * @return the preferred alert time
     */
    public Time getPreferredAlertTime() {
        return preferredAlertTime;
    }

    /**
     * Returns the reference date of the enrollment.
     * @return the reference date
     */
    public LocalDate getReferenceDate() {
        return referenceDate;
    }

    /**
     * Returns the reference time of the enrollment.
     * @return the reference time
     */
    public Time getReferenceTime() {
        return referenceTime != null ? referenceTime : new Time(0, 0);
    }

    /**
     * Returns whether the starting milestone has been specified for the enrollment.
     * @return true if the starting milestone has been specified
     */
    public boolean isStartingMilestoneSpecified() {
        return startingMilestoneName != null && !startingMilestoneName.isEmpty();
    }

    /**
     * Returns the enrollment date and time of the enrollment.
     * @return the enrollment date and time
     */
    public DateTime getEnrollmentDateTime() {
        return newDateTime(enrollmentDate, enrollmentTime);
    }

    /**
     * Returns the reference date and time of the enrollment.
     * @return the reference date and time
     */
    public DateTime getReferenceDateTime() {
        return newDateTime(referenceDate, referenceTime);
    }

    /**
     * Returns the metadata key value map of the enrollment.
     * @return the metadata
     */
    public Map<String, String> getMetadata() {
        return metadata;
    }

    /**
     * Sets the metadata key value map of the enrollment. This is list of string key value pairs associated with an
     * enrollment, which can be used to store some additional information about the enrollment. Default value is empty list.
     */
    public EnrollmentRequest setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
        return this;
    }

    /**
     * Sets the external id of the enrollment.
     */
    public EnrollmentRequest setExternalId(String externalId) {
        this.externalId = externalId;
        return this;
    }

    /**
     * Sets the schedule of the enrollment.
     */
    public EnrollmentRequest setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
        return this;
    }

    /**
     * Sets the preferred alert time of the enrollment.
     */
    public EnrollmentRequest setPreferredAlertTime(Time preferredAlertTime) {
        this.preferredAlertTime = preferredAlertTime;
        return this;
    }

    /**
     * Sets the reference date of the enrollment. This is the start date of the schedule based on which all the window
     * duration calculations are made. In case of enrollment into milestones other than first milestone, this date
     * is not used. Default value is today.
     */
    public EnrollmentRequest setReferenceDate(LocalDate referenceDate) {
        this.referenceDate = referenceDate == null ? today() : referenceDate;
        return this;
    }

    /**
     * Sets the reference time of the enrollment. This is the start time of the schedule based on which all the window
     * duration calculations are made. In case of enrollment into milestones other than first milestone, this time
     * is not used. Default value is midnight.
     */
    public EnrollmentRequest setReferenceTime(Time referenceTime) {
        this.referenceTime = referenceTime == null ? new Time(0, 0) : referenceTime;
        return this;
    }

    /**
     * Sets the enrollment date of the enrollment. The date of enrollment. In case of enrollment into milestones other
     * than first milestone, this becomes the start date of that milestone. Default value is today.
     */
    public EnrollmentRequest setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate == null ? today() : enrollmentDate;
        return this;
    }

    /**
     * Sets the enrollment time of the enrollment. The time of enrollment. In case of enrollment into milestones other
     * than first milestone, this becomes the start time of that milestone. Default value is midnight.
     */
    public EnrollmentRequest setEnrollmentTime(Time enrollmentTime) {
        this.enrollmentTime = enrollmentTime == null ? new Time(0, 0) : enrollmentTime;
        return this;
    }

    /**
     * Sets the starting milestone of the Enrollment. Name of the milestone to enroll against.
     * Default value is first milestone name from the schedule.
     */
    public EnrollmentRequest setStartingMilestoneName(String startingMilestoneName) {
        this.startingMilestoneName = startingMilestoneName;
        return this;
    }
}
