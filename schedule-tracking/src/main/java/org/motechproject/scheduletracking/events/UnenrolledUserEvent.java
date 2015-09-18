package org.motechproject.scheduletracking.events;

import org.motechproject.event.MotechEvent;
import org.motechproject.scheduletracking.events.constants.EventDataKeys;
import org.motechproject.scheduletracking.events.constants.EventSubjects;

import java.util.HashMap;
import java.util.Map;

/**
 * The <code>UnenrolledUserEvent</code> is used to create a Motech event. The event payload contains details about user unenrollment.
 * @see org.motechproject.scheduletracking.service.EnrollmentService
 */
public class UnenrolledUserEvent {

    private String externalId;

    private String scheduleName;

    /**
     * Creates a UnenrolledUserEvent with the externalId attribute set to {@code externalId},
     * the scheduleName attribute to {@code scheduleName}.
     *
     * @param externalId the user external id
     * @param scheduleName the name of the schedule
     */
    public UnenrolledUserEvent(String externalId, String scheduleName) {
        this.externalId = externalId;
        this.scheduleName = scheduleName;
    }

    /**
     * Creates a UnenrolledUserEvent from map with the given parameters.
     *
     * @param parameters the parameters map, it should contains the external id and the schedule name
     */
    public UnenrolledUserEvent(Map<String, Object> parameters) {
        this.externalId = parameters.get(EventDataKeys.EXTERNAL_ID).toString();
        this.scheduleName = parameters.get(EventDataKeys.SCHEDULE_NAME).toString();
    }

    public String getExternalId() {
        return externalId;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    /**
     * Creates a Motech event with details about user unenrollment.
     *
     * @return the Motech event with details about user unenrollment
     */
    public MotechEvent toMotechEvent() {
        Map<String, Object> param = new HashMap<>();
        param.put(EventDataKeys.EXTERNAL_ID, externalId);
        param.put(EventDataKeys.SCHEDULE_NAME, scheduleName);
        return new MotechEvent(EventSubjects.USER_UNENROLLED, param);
    }
}
