package org.motechproject.scheduletracking.domain;

import org.joda.time.DateTime;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.Ignore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.motechproject.commons.date.util.DateUtil.now;

@Entity
public class Schedule {

    private String name;
    private List<Milestone> milestones = new ArrayList<>();
    @Field
    private boolean basedOnAbsoluteWindows;

    public Schedule() {
        this(null);
    }

    public Schedule(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addMilestones(Milestone... milestonesList) {
        milestones.addAll(Arrays.asList(milestonesList));
    }

    @Ignore
    public Milestone getFirstMilestone() {
        return milestones.get(0);
    }

    public List<Milestone> getMilestones() {
        return milestones;
    }

    public Milestone getMilestone(String milestoneName) {
        for (Milestone milestone : milestones) {
            if (milestone.getName().equals(milestoneName)) {
                return milestone;
            }
        }
        return null;
    }

    public String getNextMilestoneName(String currentMilestoneName) {
        int currentIndex = milestones.indexOf(getMilestone(currentMilestoneName));
        if (currentIndex < milestones.size() - 1) {
            return milestones.get(currentIndex + 1).getName();
        }
        return null;
    }

    @Ignore
    public Period getDuration() {
        MutablePeriod duration = new MutablePeriod();
        for (Milestone milestone : milestones) {
            duration.add(milestone.getMaximumDuration());
        }
        return duration.toPeriod();
    }

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

    @Ignore
    public Schedule merge(Schedule schedule) {
        this.milestones = schedule.milestones;
        this.basedOnAbsoluteWindows = schedule.basedOnAbsoluteWindows;
        return this;
    }
}
