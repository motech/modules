package org.motechproject.scheduletracking.domain;

import org.joda.time.Period;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

/**
 * Represents an alert which will be raised in the milestone window.
 */
@Entity
public class Alert {

    /**
     * The offset specifies the amount of time that will pass from the alert reference time to when the alert is raised.
     */
    @Field
    private Period offset;

    /**
     * The interval is the time interval between two consecutive alerts.
     */
    @Field
    private Period interval;

    /**
     * The count of the alert in the milestone window.
     */
    @Field
    private int count;

    /**
     * The index of the alert in the schedule.
     */
    @Field
    private int index;

    /**
     * The floating flag. Then this flag is set to true then the enrollment date is used as the alert reference time.
     */
    @Field
    private boolean floating;

    /**
     * The milestone window.
     */
    @Field
    private MilestoneWindow milestoneWindow;

    /**
     * Creates an Alert.
     */
    public Alert() {
    }

    /**
     * Creates an Alert with the offset attribute set to {@code offset}, the interval attribute set to
     * {@code interval}, the count attribute to {@code count}, the index attribute to {@code index},
     * the floating attribute to {@code floating}.
     *
     * @param offset the alert offset
     * @param interval the alert interval
     * @param count the alert count
     * @param index the alert index in the schedule
     * @param floating the floating flag
     */
    public Alert(Period offset, Period interval, int count, int index, boolean floating) {
        this.offset = offset;
        this.interval = interval;
        this.count = count;
        this.index = index;
        this.floating = floating;
    }

    public int getCount() {
        return count;
    }

    public Period getOffset() {
        return offset;
    }

    public Period getInterval() {
        return interval;
    }

    public int getIndex() {
        return index;
    }

    public boolean isFloating() {
        return floating;
    }

    public void setOffset(Period offset) {
        this.offset = offset;
    }

    public void setFloating(boolean floating) {
        this.floating = floating;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setInterval(Period interval) {
        this.interval = interval;
    }

    public MilestoneWindow getMilestoneWindow() { return milestoneWindow; }

    public void setMilestoneWindow(MilestoneWindow milestoneWindow) { this.milestoneWindow = milestoneWindow; }
}
