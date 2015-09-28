package org.motechproject.scheduletracking.domain;


import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the milestone data used to create events.
 */
public final class MilestoneAlert implements Serializable {

    private static final long serialVersionUID = 2228943790691449648L;

    private String milestoneName;

    private DateTime earliestDateTime;

    private DateTime dueDateTime;

    private DateTime lateDateTime;

    private DateTime defaultmentDateTime;

    /**
     * Creates a MilestoneAlert from the given milestone.
     *
     * @param milestone the milestone
     * @param startOfMilestone the start date and time of the milestone
     * @return the milestone alert
     */
    public static MilestoneAlert fromMilestone(Milestone milestone, DateTime startOfMilestone) {
        return new MilestoneAlert(milestone.getName(),
                getWindowStartDate(milestone, startOfMilestone, WindowName.earliest),
                getWindowStartDate(milestone, startOfMilestone, WindowName.due),
                getWindowStartDate(milestone, startOfMilestone, WindowName.late),
                getWindowStartDate(milestone, startOfMilestone, WindowName.max));
    }

    private static DateTime getWindowStartDate(Milestone milestone, DateTime startOfMilestone, WindowName windowName) {
        return startOfMilestone.plus(milestone.getWindowStart(windowName));
    }

    /**
     * Creates a MilestoneAlert.
     */
    private MilestoneAlert() {
    }

    /**
     * Creates a MilestoneAlert from with the milestoneName attribute set to {@code milestoneName}, the earliestDateTime attribute
     * set to {@code earliestDateTime}, the dueDateTime attribute set to {@code dueDateTime}, the lateDateTime attribute set to {@code lateDateTime}
     * and the defaultmentDateTime attribute set to {@code defaultmentDateTime}.
     *
     * @param milestoneName the name of the milestone
     * @param earliestDateTime the start date and time of the earliest window
     * @param dueDateTime the start date and time of the due window
     * @param lateDateTime the start date and time of the late window
     * @param defaultmentDateTime the defaultment date and time of the milestone
     */
    public MilestoneAlert(String milestoneName, DateTime earliestDateTime, DateTime dueDateTime, DateTime lateDateTime,
                          DateTime defaultmentDateTime) {
        this.milestoneName = milestoneName;
        this.earliestDateTime = earliestDateTime;
        this.dueDateTime = dueDateTime;
        this.lateDateTime = lateDateTime;
        this.defaultmentDateTime = defaultmentDateTime;
    }

    public String getMilestoneName() {
        return milestoneName;
    }

    public DateTime getEarliestDateTime() {
        return earliestDateTime;
    }

    public DateTime getDueDateTime() {
        return dueDateTime;
    }

    public DateTime getLateDateTime() {
        return lateDateTime;
    }

    public DateTime getDefaultmentDateTime() {
        return defaultmentDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof MilestoneAlert)) {
            return false;
        }

        MilestoneAlert that = (MilestoneAlert) o;

        return Objects.equals(defaultmentDateTime, that.defaultmentDateTime) &&
                Objects.equals(dueDateTime, that.dueDateTime) && Objects.equals(earliestDateTime, that.earliestDateTime) &&
                Objects.equals(lateDateTime, that.lateDateTime) && Objects.equals(milestoneName, that.milestoneName);
    }

    @Override
    public int hashCode() {
        int result = milestoneName != null ? milestoneName.hashCode() : 0;
        result = 31 * result + (earliestDateTime != null ? earliestDateTime.hashCode() : 0);
        result = 31 * result + (dueDateTime != null ? dueDateTime.hashCode() : 0);
        result = 31 * result + (lateDateTime != null ? lateDateTime.hashCode() : 0);
        result = 31 * result + (defaultmentDateTime != null ? defaultmentDateTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MilestoneAlert{" +
                "milestoneName='" + milestoneName + '\'' +
                ", earliestDateTime=" + earliestDateTime +
                ", dueDateTime=" + dueDateTime +
                ", lateDateTime=" + lateDateTime +
                ", defaultmentDateTime=" + defaultmentDateTime +
                '}';
    }
}
