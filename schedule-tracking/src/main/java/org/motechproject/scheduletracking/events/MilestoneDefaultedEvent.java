package org.motechproject.scheduletracking.events;

import org.motechproject.event.MotechEvent;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.motechproject.scheduletracking.events.constants.EventDataKeys;
import org.motechproject.scheduletracking.events.constants.EventSubjects;

import java.util.HashMap;

/**
 * The <code>MilestoneDefaultedEvent</code> is used to create a Motech event. The event payload contains details about milestone
 * defaulted.
 * @see org.motechproject.scheduletracking.service.impl.EndOfMilestoneListener
 * @see org.motechproject.scheduletracking.service.impl.EnrollmentDefaultmentService
 */
public class MilestoneDefaultedEvent {

    private String jobId;

    private Long enrollmentId;

    private String externalId;

    /**
     * Creates a MilestoneDefaultedEvent from the given Motech event.
     *
     * @param event the Motech event with defaulted details
     */
    public MilestoneDefaultedEvent(MotechEvent event) {
        this.jobId = (String) event.getParameters().get(MotechSchedulerService.JOB_ID_KEY);
        this.enrollmentId = (Long) event.getParameters().get(EventDataKeys.ENROLLMENT_ID);
        this.externalId = (String) event.getParameters().get(EventDataKeys.EXTERNAL_ID);
    }

    /**
     * Creates a MilestoneDefaultedEvent with the enrollmentId attribute set to {@code enrollmentId}, the jobId attribute to {@code jobId},
     * the externalId attribute to {@code externalId}.
     *
     * @param enrollmentId the defaulted enrolment id
     * @param jobId the job id
     * @param externalId the user external id
     */
    public MilestoneDefaultedEvent(Long enrollmentId, String jobId, String externalId) {
        this.jobId = jobId;
        this.enrollmentId = enrollmentId;
        this.externalId = externalId;
    }

    /**
     * Creates a Motech event.
     *
     * @return the Motech event with details about milestone defaulted
     */
    public MotechEvent toMotechEvent() {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put(EventDataKeys.ENROLLMENT_ID, enrollmentId);
        data.put(EventDataKeys.EXTERNAL_ID, externalId);
        data.put(MotechSchedulerService.JOB_ID_KEY, jobId);
        return new MotechEvent(EventSubjects.MILESTONE_DEFAULTED, data);
    }

    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public String getJobId() {
        return jobId;
    }

    public String getExternalId() {
        return externalId;
    }
}
