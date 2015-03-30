package org.motechproject.scheduletracking.domain;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.motechproject.commons.date.model.Time;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.motechproject.commons.date.util.DateUtil.inRange;
import static org.motechproject.commons.date.util.DateUtil.setTimeZone;
import static org.motechproject.scheduletracking.domain.EnrollmentStatus.ACTIVE;
import static org.motechproject.scheduletracking.domain.EnrollmentStatus.COMPLETED;

@Entity
public class Enrollment {

    @Field
    private Long id;

    @Field
    private String externalId;

    @Field
    private String scheduleName;

    @Field
    private String currentMilestoneName;

    @Field
    private DateTime startOfSchedule;

    @Field
    private DateTime enrolledOn;

    @Field
    private Time preferredAlertTime;

    @Field
    private EnrollmentStatus status;

    @Field
    private Map<String, String> metadata;

    @Field
    private Schedule schedule;

    @Field
    private List<MilestoneFulfillment> fulfillments = new LinkedList<>();

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

    public DateTime getLastFulfilledDate() {
        if (fulfillments.isEmpty()) {
            return null;
        }
        return fulfillments.get(fulfillments.size() - 1).getFulfillmentDateTime();
    }

    public boolean isActive() {
        return status.equals(ACTIVE);
    }

    public boolean isCompleted() {
        return status.equals(COMPLETED);
    }

    public Schedule getSchedule() {
        return schedule;
    }

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

    public void fulfillCurrentMilestone(DateTime fulfillmentDateTime) {
        fulfillments.add(new MilestoneFulfillment(currentMilestoneName, fulfillmentDateTime));
    }

    public DateTime getStartOfWindowForCurrentMilestone(WindowName windowName) {
        DateTime currentMilestoneStartDate = getCurrentMilestoneStartDate();
        Milestone currentMilestone = schedule.getMilestone(currentMilestoneName);
        return currentMilestoneStartDate.plus(currentMilestone.getWindowStart(windowName));
    }

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

    public Enrollment copyFrom(Enrollment enrollment) {
        enrolledOn = enrollment.getEnrolledOn();
        currentMilestoneName = enrollment.getCurrentMilestoneName();
        startOfSchedule = enrollment.getStartOfSchedule();
        preferredAlertTime = enrollment.getPreferredAlertTime();
        status = enrollment.getStatus();
        metadata = new HashMap<>(enrollment.getMetadata());
        return this;
    }

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
