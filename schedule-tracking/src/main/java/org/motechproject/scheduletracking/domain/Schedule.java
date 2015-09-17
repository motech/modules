package org.motechproject.scheduletracking.domain;

import org.joda.time.DateTime;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.Ignore;

import javax.jdo.annotations.Persistent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.motechproject.commons.date.util.DateUtil.now;

/**
 * Represents schedule of the milestones. Users are enrolled to the schedules, after this alerts will be raised.
 * If the schedule is absolute: The alert reference time is the start of the schedule. All offsets for alerts are
 * calculated from the start of schedule. If the schedule is not absolute(default): If there are no fulfilled milestones,
 * the enrollment date is used as the alert reference time. If there are fulfilled milestones, the last milestone’s
 * fulfillment date is used as the alert reference time.
 */
@Entity
public class Schedule {

    /**
     * The name of the schedule.
     */
    @Field
    private String name;

    /**
     * The list of the milestones.
     */
    @Field
    @Cascade(delete = true)
    @Persistent(mappedBy = "schedule")
    private List<Milestone> milestones = new ArrayList<>();

    /**
     * This is flag that tells whether the schedule is absolute.
     */
    @Field
    private boolean basedOnAbsoluteWindows;

    /**
     * Creates a Schedule.
     */
    public Schedule() {
        this(null);
    }

    /**
     * Creates a Schedule with the name attribute set to {@code name}.
     *
     * @param name the name of the schedule
     */
    public Schedule(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Adds the given milestones to this schedule.
     *
     * @param milestonesList the list of the milestones
     */
    public void addMilestones(Milestone... milestonesList) {
        milestones.addAll(Arrays.asList(milestonesList));
    }

    /**
     * Returns the first milestone in the schedule.
     *
     * @return the first milestone
     */
    @Ignore
    public Milestone getFirstMilestone() {
        return milestones.get(0);
    }

    public List<Milestone> getMilestones() {
        return milestones;
    }

    /**
     * Returns the milestone with the given name.
     *
     * @param milestoneName the name of the milestone
     * @return the milestone
     */
    public Milestone getMilestone(String milestoneName) {
        for (Milestone milestone : milestones) {
            if (milestone.getName().equals(milestoneName)) {
                return milestone;
            }
        }
        return null;
    }

    /**
     * Returns the name of the next milestone.
     *
     * @param currentMilestoneName the name of the current milestone
     * @return the name of the next milestone
     */
    public String getNextMilestoneName(String currentMilestoneName) {
        int currentIndex = milestones.indexOf(getMilestone(currentMilestoneName));
        if (currentIndex < milestones.size() - 1) {
            return milestones.get(currentIndex + 1).getName();
        }
        return null;
    }

    /**
     * Returns the duration of all milestones in this schedule.
     *
     * @return the duration of all milestones
     */
    @Ignore
    public Period getDuration() {
        MutablePeriod duration = new MutablePeriod();
        for (Milestone milestone : milestones) {
            duration.add(milestone.getMaximumDuration());
        }
        return duration.toPeriod();
    }

    /**
     * Checks whether the milstone with the given name has been expired.
     *
     * @param referenceDateTime the expiration reference date and time
     * @param currentMilestoneStr the name of the milestone
     * @return true if the milestone has been expired
     */
    public boolean hasExpiredSince(DateTime referenceDateTime, String currentMilestoneStr) {
        Milestone currentMilestone = getMilestone(currentMilestoneStr);
        return referenceDateTime.plus(currentMilestone.getMaximumDuration()).isBefore(now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Schedule schedule = (Schedule) o;

        if (name != null ? !name.equals(schedule.name) : schedule.name != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Ignore
    public void setBasedOnAbsoluteWindows(boolean value) {
        this.basedOnAbsoluteWindows = value;
    }

    public boolean isBasedOnAbsoluteWindows() {
        return basedOnAbsoluteWindows;
    }

    /**
     * Sets the {@code milestones} and the {@code basedOnAbsoluteWindows} attributes. The values are retrieved from the
     * given scheduler.
     *
     * @param schedule the scheduler which data will be merged with this scheduler
     * @return the scheduler with merged data
     */
    @Ignore
    public Schedule merge(Schedule schedule) {
        this.milestones = schedule.milestones;
        this.basedOnAbsoluteWindows = schedule.basedOnAbsoluteWindows;
        return this;
    }
}
