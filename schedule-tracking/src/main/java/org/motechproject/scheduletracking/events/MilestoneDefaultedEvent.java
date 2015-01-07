package org.motechproject.scheduletracking.events;

import org.motechproject.event.MotechEvent;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.motechproject.scheduletracking.events.constants.EventDataKeys;
import org.motechproject.scheduletracking.events.constants.EventSubjects;

import java.util.HashMap;

public class MilestoneDefaultedEvent {
    private String jobId;
    private Long enrollmentId;
    private String externalId;

    public MilestoneDefaultedEvent(MotechEvent event) {
        this.jobId = (String) event.getParameters().get(MotechSchedulerService.JOB_ID_KEY);
        this.enrollmentId = (Long) event.getParameters().get(EventDataKeys.ENROLLMENT_ID);
        this.externalId = (String) event.getParameters().get(EventDataKeys.EXTERNAL_ID);
    }

    public MilestoneDefaultedEvent(Long enrollmentId, String jobId, String externalId) {
        this.jobId = jobId;
        this.enrollmentId = enrollmentId;
        this.externalId = externalId;
    }

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
