package org.motechproject.scheduletracking.events;

import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.domain.MilestoneAlert;
import org.motechproject.scheduletracking.domain.MilestoneWindow;
import org.motechproject.scheduletracking.events.constants.EventDataKeys;
import org.motechproject.scheduletracking.events.constants.EventSubjects;

import java.util.HashMap;
import java.util.Map;

/**
 * The <code>MilestoneEvent</code> is used to create a Motech event. The event payload contains details about milestone.
 * @see org.motechproject.scheduletracking.service.impl.EnrollmentAlertService
 */
public class MilestoneEvent {

    private String windowName;

    private MilestoneAlert milestoneAlert;

    private String scheduleName;

    private String externalId;

    private DateTime referenceDateTime;

    private Map<String, String> milestoneData;

    /**
     * Creates a MilestoneEvent with the enrollmentId attribute set to {@code enrollmentId}, the scheduleName attribute to {@code scheduleName},
     * the milestoneAlert attribute to {@code milestoneAlert}, the windowName attribute to {@code windowName},
     * the referenceDateTime attribute to {@code referenceDateTime}, the milestoneData attribute to {@code milestoneData}.
     *
     * @param externalId the user external id
     * @param scheduleName the name of the schedule
     * @param milestoneAlert the milestone alert
     * @param windowName the name of the milestone window
     * @param referenceDateTime the reference date and time
     * @param milestoneData the milestone additional data
     */
    public MilestoneEvent(String externalId, String scheduleName, MilestoneAlert milestoneAlert, String windowName, DateTime referenceDateTime, Map<String, String> milestoneData) {
        this.scheduleName = scheduleName;
        this.milestoneAlert = milestoneAlert;
        this.windowName = windowName;
        this.externalId = externalId;
        this.referenceDateTime = referenceDateTime;
        this.milestoneData = milestoneData;
    }

    /**
     * Creates a MilestoneEvent from an enrollment by passing in an Motech event.
     *
     * @param motechEvent the Motech event with details
     */
    public MilestoneEvent(MotechEvent motechEvent) {
        this.scheduleName = (String) motechEvent.getParameters().get(EventDataKeys.SCHEDULE_NAME);
        this.milestoneAlert = new MilestoneAlert((String) motechEvent.getParameters().get(EventDataKeys.MILESTONE_NAME),
                (DateTime) motechEvent.getParameters().get(EventDataKeys.EARLIEST_DATE_TIME),
                (DateTime) motechEvent.getParameters().get(EventDataKeys.DUE_DATE_TIME), 
                (DateTime) motechEvent.getParameters().get(EventDataKeys.LATE_DATE_TIME),
                (DateTime) motechEvent.getParameters().get(EventDataKeys.DEFAULTMENT_DATE_TIME));
        this.windowName = (String) motechEvent.getParameters().get(EventDataKeys.WINDOW_NAME);
        this.externalId = (String) motechEvent.getParameters().get(EventDataKeys.EXTERNAL_ID);
        this.referenceDateTime = (DateTime) motechEvent.getParameters().get(EventDataKeys.REFERENCE_DATE);
        this.milestoneData = (Map<String, String>) motechEvent.getParameters().get(EventDataKeys.MILESTONE_DATA);
    }

    /**
     * Creates a MilestoneEvent from an enrollment, alert and milestone window.
     *
     * @param enrollment the enrollment
     * @param milestoneAlert the alert which will be triggered
     * @param milestoneWindow the milestone window
     */
    public MilestoneEvent(Enrollment enrollment, MilestoneAlert milestoneAlert, MilestoneWindow milestoneWindow) {
        this.externalId = enrollment.getExternalId();
        this.scheduleName = enrollment.getScheduleName();
        this.milestoneAlert = milestoneAlert;
        this.windowName = milestoneWindow.getName().toString();
        this.referenceDateTime = enrollment.getStartOfSchedule();
        this.milestoneData = enrollment.getSchedule().getMilestone(enrollment.getCurrentMilestoneName()).getData();
    }

    /**
     * Creates a Motech event with alert details.
     *
     * @return the Motech event with alert details
     */
    public MotechEvent toMotechEvent() {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put(EventDataKeys.WINDOW_NAME, windowName);
        parameters.put(EventDataKeys.MILESTONE_NAME, milestoneAlert.getMilestoneName());
        parameters.put(EventDataKeys.EARLIEST_DATE_TIME, milestoneAlert.getEarliestDateTime());
        parameters.put(EventDataKeys.DUE_DATE_TIME, milestoneAlert.getDueDateTime());
        parameters.put(EventDataKeys.LATE_DATE_TIME, milestoneAlert.getLateDateTime());
        parameters.put(EventDataKeys.DEFAULTMENT_DATE_TIME, milestoneAlert.getDefaultmentDateTime());
        parameters.put(EventDataKeys.SCHEDULE_NAME, scheduleName);
        parameters.put(EventDataKeys.EXTERNAL_ID, externalId);
        parameters.put(EventDataKeys.REFERENCE_DATE, referenceDateTime);
        parameters.put(EventDataKeys.MILESTONE_DATA, milestoneData);
        return new MotechEvent(EventSubjects.MILESTONE_ALERT, parameters);
    }

    /**
     * Returns the window name of the MilestoneEvent.
     *
     * @return the milestone window name
     */
    public String getWindowName() {
        return windowName;
    }

    /**
     * Returns the milestone alert of the MilestoneEvent.
     *
     * @return the milestone alert
     */
    public MilestoneAlert getMilestoneAlert() {
        return milestoneAlert;
    }

    /**
     * Returns the schedule name of the MilestoneEvent.
     *
     * @return the name of the schedule
     */
    public String getScheduleName() {
        return scheduleName;
    }

    /**
     * Returns the external id of the MilestoneEvent.
     *
     * @return the external id
     */
    public String getExternalId() {
        return externalId;
    }

    /**
     * Returns the reference date and time of the MilestoneEvent.
     *
     * @return the reference date and time
     */
    public DateTime getReferenceDateTime() {
        return referenceDateTime;
    }

    /**
     * Returns the milestone data of the MilestoneEvent.
     *
     * @return the milestone data
     */
    public Map<String, String> getMilestoneData() {
        return milestoneData;
    }
}
