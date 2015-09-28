package org.motechproject.scheduletracking.service.impl;

import org.joda.time.DateTime;
import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.domain.WindowName;
import org.motechproject.scheduletracking.service.EnrollmentRecord;
import org.springframework.stereotype.Component;

/**
 * Component used to map enrollments to the {@link org.motechproject.scheduletracking.service.EnrollmentRecord}.
 */
@Component
public class EnrollmentRecordMapper {

    /**
     * Builds enrollment record from the given enrollment. This method dosen't fill milestone windows date for current
     * milestone.
     *
     * @param enrollment the enrollment to map
     * @return the enrollment record
     */
    public EnrollmentRecord map(Enrollment enrollment) {
        if (enrollment == null) {
            return null;
        }
        return new EnrollmentRecord()
            .setExternalId(enrollment.getExternalId())
            .setScheduleName(enrollment.getScheduleName())
            .setCurrentMilestoneName(enrollment.getCurrentMilestoneName())
            .setPreferredAlertTime(enrollment.getPreferredAlertTime())
            .setReferenceDateTime(enrollment.getStartOfSchedule())
            .setEnrollmentDateTime(enrollment.getEnrolledOn())
            .setStatus(enrollment.getStatus().toString())
            .setMetadata(enrollment.getMetadata())
            .setEarliestStart(null)
            .setDueStart(null)
            .setLateStart(null)
            .setMaxStart(null);
    }

    /**
     * Builds enrollment record from the given enrollment. This method fill milestone windows date for current milestone.
     *
     * @param enrollment the enrollment to map
     * @return the enrollment record
     */
    public EnrollmentRecord mapWithDates(Enrollment enrollment) {
        if (enrollment == null) {
            return null;
        }
        DateTime earliestStart = enrollment.getStartOfWindowForCurrentMilestone(WindowName.earliest);
        DateTime dueStart = enrollment.getStartOfWindowForCurrentMilestone(WindowName.due);
        DateTime lateStart = enrollment.getStartOfWindowForCurrentMilestone(WindowName.late);
        DateTime maxStart = enrollment.getStartOfWindowForCurrentMilestone(WindowName.max);
        return new EnrollmentRecord()
            .setExternalId(enrollment.getExternalId())
            .setScheduleName(enrollment.getScheduleName())
            .setCurrentMilestoneName(enrollment.getCurrentMilestoneName())
            .setPreferredAlertTime(enrollment.getPreferredAlertTime())
            .setReferenceDateTime(enrollment.getStartOfSchedule())
            .setEnrollmentDateTime(enrollment.getEnrolledOn())
            .setStatus(enrollment.getStatus().toString())
            .setMetadata(enrollment.getMetadata())
            .setEarliestStart(earliestStart)
            .setDueStart(dueStart)
            .setLateStart(lateStart)
            .setMaxStart(maxStart);
    }
}
