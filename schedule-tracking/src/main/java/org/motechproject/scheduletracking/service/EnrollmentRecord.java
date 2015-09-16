package org.motechproject.scheduletracking.service;

import org.joda.time.DateTime;
import org.motechproject.commons.date.model.Time;

import java.util.Map;

/**
 * <code>EnrollmentRecord</code>represents enrollment which will be returned when schedule tracking service is queried
 * for enrollments. It holds the details of an enrollment.
 * @see org.motechproject.scheduletracking.service.ScheduleTrackingService
 */
public class EnrollmentRecord {

    private String externalId;

    private String scheduleName;

    private String currentMilestoneName;

    private DateTime referenceDateTime;

    private DateTime enrollmentDateTime;

    private Time preferredAlertTime;

    private DateTime earliestStart;

    private DateTime dueStart;

    private DateTime lateStart;

    private DateTime maxStart;

    private String status;

    private Map<String, String> metadata;

    public EnrollmentRecord() {
    }

    /**
     * Returns the external id of an enrollment.
     * @return the external id of an enrollment
     */
    public String getExternalId() {
        return externalId;
    }

    
    /**
     * Returns the schedule name of an enrollment.
     * @return the schedule name
     */
    public String getScheduleName() {
        return scheduleName;
    }

    /**
     * Returns the preferred alert time of an enrollment.
     * @return the preferred alert time
     */
    public Time getPreferredAlertTime() {
        return preferredAlertTime;
    }

    /**
     * Returns the reference date and time of an enrollment.
     * @return the reference date and time
     */
    public DateTime getReferenceDateTime() {
        return referenceDateTime;
    }

    /**
     * Returns the date and time of an enrollment.
     * @return the enrollment date and time
     */
    public DateTime getEnrollmentDateTime() {
        return enrollmentDateTime;
    }

    /**
     * Returns the earliest window start date and time of current milestone of an enrollment.
     * @return the earliest window start date and time
     */
    public DateTime getStartOfEarliestWindow() {
        return earliestStart;
    }

    /**
     * Returns the due window start date and time of current milestone of an enrollment.
     * @return the due window start date and time
     */
    public DateTime getStartOfDueWindow() {
        return dueStart;
    }

    /**
     * Returns the late window start date and time of current milestone of an enrollment.
     * @return the late window start date and time
     */
    public DateTime getStartOfLateWindow() {
        return lateStart;
    }

    /**
     * Returns the max window start date and time of current milestone of an enrollment.
     * @return the max window start date and time
     */
    public DateTime getStartOfMaxWindow() {
        return maxStart;
    }

    /**
     * Returns the current milestone name of an enrollment.
     * @return the current milestone name
     */
    public String getCurrentMilestoneName() {
        return currentMilestoneName;
    }

    /**
     * Returns the status of the enrollment.
     * @return the enrollment status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Returns the metadata associated with the enrollment.
     * @return the metadata
     */
    public Map<String, String> getMetadata() {
        return metadata;
    }

    public EnrollmentRecord setExternalId(String externalId) {
        this.externalId = externalId;
        return this;
    }

    public EnrollmentRecord setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
        return this;
    }

    public EnrollmentRecord setPreferredAlertTime(Time preferredAlertTime) {
        this.preferredAlertTime = preferredAlertTime;
        return this;
    }

    public EnrollmentRecord setReferenceDateTime(DateTime referenceDateTime) {
        this.referenceDateTime = referenceDateTime;
        return this;
    }

    public EnrollmentRecord setEnrollmentDateTime(DateTime enrollmentDateTime) {
        this.enrollmentDateTime = enrollmentDateTime;
        return this;
    }

    public EnrollmentRecord setEarliestStart(DateTime earliestStart) {
        this.earliestStart = earliestStart;
        return this;
    }

    public EnrollmentRecord setDueStart(DateTime dueStart) {
        this.dueStart = dueStart;
        return this;
    }

    public EnrollmentRecord setLateStart(DateTime lateStart) {
        this.lateStart = lateStart;
        return this;
    }

    public EnrollmentRecord setMaxStart(DateTime maxStart) {
        this.maxStart = maxStart;
        return this;
    }

    public EnrollmentRecord setCurrentMilestoneName(String currentMilestoneName) {
        this.currentMilestoneName = currentMilestoneName;
        return this;
    }

    public EnrollmentRecord setStatus(String status) {
        this.status = status;
        return this;
    }

    public EnrollmentRecord setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
        return this;
    }
}
