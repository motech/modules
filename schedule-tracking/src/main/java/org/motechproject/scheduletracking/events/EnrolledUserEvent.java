package org.motechproject.scheduletracking.events;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.commons.date.model.Time;
import org.motechproject.event.MotechEvent;
import org.motechproject.scheduletracking.events.constants.EventDataKeys;
import org.motechproject.scheduletracking.events.constants.EventSubjects;

import java.util.HashMap;
import java.util.Map;

/**
 * <code>EnrolledUserEvent</code> is used to hold details about user enrollment and to create Motech event with
 * this details.
 * @see org.motechproject.scheduletracking.service.EnrollmentService
 */
public class EnrolledUserEvent {

    private String externalId;

    private String scheduleName;

    private Time preferredAlertTime;

    private LocalDate referenceDate;

    private Time referenceTime;

    private LocalDate enrollmentDate;

    private Time enrollmentTime;

    private String startingMilestoneName;

    /**
     * Creates a EnrolledUserEvent with the externalId attribute set to {@code scheduleName}, the scheduleName attribute to {@code scheduleName},
     * the preferredAlertTime attribute to {@code preferredAlertTime}, the referenceDateTime attribute is used to fill
     * {@code referenceDate} and {@code referenceTime}, the enrollmentDateTime attribute is used to fill {@code enrollmentDate}
     * and {@code enrollmentTime}, the startingMilestoneName attribute to {@code startingMilestoneName}.
     *
     * @param externalId the user external id
     * @param scheduleName the name of the schedule
     * @param preferredAlertTime the preferred alert time
     * @param referenceDateTime the reference date and time
     * @param enrollmentDateTime the enrollment date and time
     * @param startingMilestoneName the starting milestone name
     */
    public EnrolledUserEvent(String externalId, String scheduleName, Time preferredAlertTime, DateTime referenceDateTime,
                             DateTime enrollmentDateTime, String startingMilestoneName) {
        this.externalId = externalId;
        this.scheduleName = scheduleName;
        this.preferredAlertTime = preferredAlertTime;
        if (referenceDateTime == null) {
            this.referenceDate = null;
            this.referenceTime = null;
        } else {
            this.referenceDate = referenceDateTime.toLocalDate();
            this.referenceTime = new Time(referenceDateTime.toLocalTime());
        }
        if (enrollmentDateTime == null) {
            this.enrollmentDate = null;
            this.enrollmentTime = null;
        } else {
            this.enrollmentDate = enrollmentDateTime.toLocalDate();
            this.enrollmentTime = new Time(enrollmentDateTime.toLocalTime());
        }

        this.startingMilestoneName = startingMilestoneName;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public Time getPreferredAlertTime() {
        return preferredAlertTime;
    }

    public LocalDate getReferenceDate() {
        return referenceDate;
    }

    public Time getReferenceTime() {
        return referenceTime;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public Time getEnrollmentTime() {
        return enrollmentTime;
    }

    public String getStartingMilestoneName() {
        return startingMilestoneName;
    }

    /**
     * Creates Motech event with details about user enrollment.
     *
     * @return the Motech event with details about user enrollment
     */
    public MotechEvent toMotechEvent() {
        Map<String, Object> param = new HashMap<>();
        param.put(EventDataKeys.EXTERNAL_ID, externalId);
        param.put(EventDataKeys.SCHEDULE_NAME, scheduleName);
        param.put(EventDataKeys.MILESTONE_NAME, startingMilestoneName);
        param.put(EventDataKeys.PREFERRED_ALERT_TIME, preferredAlertTime);
        param.put(EventDataKeys.REFERENCE_DATE, referenceDate);
        param.put(EventDataKeys.REFERENCE_TIME, referenceTime);
        param.put(EventDataKeys.ENROLLMENT_DATE, enrollmentDate);
        param.put(EventDataKeys.ENROLLMENT_TIME, enrollmentTime);
        return new MotechEvent(EventSubjects.USER_ENROLLED, param);
    }
}
