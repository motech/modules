package org.motechproject.scheduletracking.domain;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.motechproject.commons.date.model.Time;
import org.motechproject.mds.annotations.CrudEvents;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.Ignore;
import org.motechproject.mds.event.CrudEventType;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.motechproject.commons.date.util.DateUtil.inRange;
import static org.motechproject.commons.date.util.DateUtil.setTimeZone;
import static org.motechproject.scheduletracking.domain.EnrollmentStatus.ACTIVE;
import static org.motechproject.scheduletracking.domain.EnrollmentStatus.COMPLETED;

/**
 * Represents details about user enrollment.
 */
@Entity
@CrudEvents(CrudEventType.NONE)
public class Enrollment {

    /**
     * The id of the enrollment.
     */
    @Field
    private Long id;

    /**
     * The user external id.
     */
    @Field
    private String externalId;

    /**
     * The name of the schedule.
     */
    @Field
    private String scheduleName;

    /**
     * The current milestone.
     */
    @Field
    private String currentMilestoneName;

    /**
     * The start reference date and time of the enrollment.
     */
    @Field
    private DateTime startOfSchedule;

    /**
     * The enrollment date and time.
     */
    @Field
    private DateTime enrolledOn;

    /**
     * The user preferred alert time.
     */
    @Field
    private Time preferredAlertTime;

    /**
     * The status of the enrollment.
     */
    @Field
    private EnrollmentStatus status;

    /**
     * The additional enrollment data.
     */
    @Field
    private Map<String, String> metadata;

    /**
     * The schedule to which the enrollment belongs.
     */
    @Field
    private Schedule schedule;

    /**
     * The details about milestone fulfillments.
     */
    @Field
    private List<MilestoneFulfillment> fulfillments = new LinkedList<>();

    /**
     * Creates an Enrollment.
     */
    public Enrollment() {
        metadata = new HashMap<>();
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public DateTime getStartOfSchedule() {
        return setTimeZone(startOfSchedule);
    }

    public void setStartOfSchedule(DateTime startOfSchedule) {
        this.startOfSchedule = startOfSchedule;
    }

    public DateTime getEnrolledOn() {
        return setTimeZone(enrolledOn);
    }

    public void setEnrolledOn(DateTime enrolledOn) {
        this.enrolledOn = enrolledOn;
    }

    public Time getPreferredAlertTime() {
        return preferredAlertTime;
    }

    public void setPreferredAlertTime(Time preferredAlertTime) {
        this.preferredAlertTime = preferredAlertTime;
    }

    public List<MilestoneFulfillment> getFulfillments() {
        return fulfillments;
    }

    public void setFulfillments(List<MilestoneFulfillment> fulfillments) {
        this.fulfillments = fulfillments;
    }

    /**
     * Returns the date and time of the last milestone fulfilment.
     *
     * @return the date and time of the last milestone fulfilment
     */
    @Ignore
    public DateTime getLastFulfilledDate() {
        if (fulfillments.isEmpty()) {
            return null;
        }
        return fulfillments.get(fulfillments.size() - 1).getFulfillmentDateTime();
    }

    /**
     * Checks whether the enrollment is active.
     *
     * @return true if the enrollment is active, otherwise false
     */
    @Ignore
    public boolean isActive() {
        return status.equals(ACTIVE);
    }

    /**
     * Checks whether the enrollment is completed.
     *
     * @return true if the enrollment is completed, otherwise false
     */
    @Ignore
    public boolean isCompleted() {
        return status.equals(COMPLETED);
    }

    public Schedule getSchedule() {
        return schedule;
    }

    /**
     * Sets the schedule and the schedule name from the given schedule.
     *
     * @param schedule the schedule to which the enrollment belongs
     */
    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
        this.scheduleName = schedule.getName();
    }

    public String getCurrentMilestoneName() {
        return currentMilestoneName;
    }

    public void setCurrentMilestoneName(String currentMilestoneName) {
        this.currentMilestoneName = currentMilestoneName;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }

    /**
     * Adds the current milestone to the milestone fulfillments list with the given date and time.
     *
     * @param fulfillmentDateTime the fulfillment date and time
     */
    public void fulfillCurrentMilestone(DateTime fulfillmentDateTime) {
        fulfillments.add(new MilestoneFulfillment(currentMilestoneName, fulfillmentDateTime));
    }

    /**
     * Returns the start date and time of the window with the given name in the current milestone.
     *
     * @param windowName the window name
     * @return the start date and time of the window
     */
    @Ignore
    public DateTime getStartOfWindowForCurrentMilestone(WindowName windowName) {
        DateTime currentMilestoneStartDate = getCurrentMilestoneStartDate();
        Milestone currentMilestone = schedule.getMilestone(currentMilestoneName);
        return currentMilestoneStartDate.plus(currentMilestone.getWindowStart(windowName));
    }

    /**
     * Returns the start date and time of the current milestone.
     *
     * @return the start date and time of the current milestone
     */
    @Ignore
    public DateTime getCurrentMilestoneStartDate() {
        if (schedule.isBasedOnAbsoluteWindows()) {
            DateTime startOfMilestone = getStartOfSchedule();
            List<Milestone> milestones = schedule.getMilestones();
            for (Milestone milestone : milestones) {
                if (milestone.getName().equals(currentMilestoneName)) {
                    break;
                }
                startOfMilestone = startOfMilestone.plus(milestone.getMaximumDuration());
            }
            return startOfMilestone;
        }
        if (currentMilestoneName.equals(schedule.getFirstMilestone().getName())) {
            return getStartOfSchedule();
        }
        return (fulfillments.isEmpty()) ? getEnrolledOn() : getLastFulfilledDate();
    }

    /**
     * Creates copy of the given enrollment.
     *
     * @param enrollment the enrollment to copy
     * @return this instance with copied properties
     */
    public Enrollment copyFrom(Enrollment enrollment) {
        enrolledOn = enrollment.getEnrolledOn();
        currentMilestoneName = enrollment.getCurrentMilestoneName();
        startOfSchedule = enrollment.getStartOfSchedule();
        preferredAlertTime = enrollment.getPreferredAlertTime();
        status = enrollment.getStatus();
        metadata = new HashMap<>(enrollment.getMetadata());
        return this;
    }

    /**
     * Returns the name of the window in the current milestone where the given date and time must be in the window
     * time range.
     *
     * @param asOf the date and time which is the time range of the window
     * @return the window name
     */
    @Ignore
    public WindowName getCurrentWindowAsOf(DateTime asOf) {
        DateTime milestoneStart = this.getCurrentMilestoneStartDate();
        Milestone milestone = schedule.getMilestone(this.getCurrentMilestoneName());
        for (MilestoneWindow window : milestone.getMilestoneWindows()) {
            Period windowStart = milestone.getWindowStart(window.getName());
            Period windowEnd = milestone.getWindowEnd(window.getName());
            DateTime windowStartDateTime = milestoneStart.plus(windowStart);
            DateTime windowEndDateTime = milestoneStart.plus(windowEnd);
            if (inRange(asOf, windowStartDateTime, windowEndDateTime)) {
                return window.getName();
            }
        }
        return null;
    }

    /**
     * Returns the end date and time of the window with the given name in the current milestone.
     *
     * @param windowName the window name
     * @return the end date and time of the window
     */
    @Ignore
    public DateTime getEndOfWindowForCurrentMilestone(WindowName windowName) {
        DateTime currentMilestoneStartDate = this.getCurrentMilestoneStartDate();
        Milestone currentMilestone = schedule.getMilestone(this.getCurrentMilestoneName());
        return currentMilestoneStartDate.plus(currentMilestone.getWindowEnd(windowName));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
