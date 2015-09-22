package org.motechproject.scheduletracking.domain;

import org.joda.time.Period;
import org.motechproject.mds.annotations.CrudEvents;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.Ignore;
import org.motechproject.mds.event.CrudEventType;

import javax.jdo.annotations.Persistent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a time interval for the milestone duration.
 */
@Entity
@CrudEvents(CrudEventType.NONE)
public class MilestoneWindow {

    /**
     * The name of the window. Can have one of the following values : earliest, due, late or max.
     */
    @Field
    private WindowName name;

    /**
     * The period represents the duration of the time window.
     */
    @Field
    private Period period;

    /**
     * The list of the alerts which can be raised during this window.
     */
    @Field
    @Cascade(delete = true)
    @Persistent(mappedBy = "milestoneWindow")
    private List<Alert> alerts = new ArrayList<Alert>();

    /**
     * The milestone which owns this window.
     */
    @Field
    private Milestone milestone;

    /**
     * Creates MilestoneWindow.
     */
    public MilestoneWindow() {
    }

    /**
     * Creates MilestoneWindow with the name attribute set to {@code name}, the period attribute set to {@code period}.
     *
     * @param name the name of the milestone window
     * @param period the duration of the time window
     */
    public MilestoneWindow(WindowName name, Period period) {
        this.name = name;
        this.period = period;
    }

    public WindowName getName() {
        return name;
    }

    public Period getPeriod() {
        return period;
    }

    /**
     * Adds the given alerts to this window.
     *
     * @param alertsList the list of the alerts do add
     */
    @Ignore
    public void addAlerts(Alert... alertsList) {
        alerts.addAll(Arrays.asList(alertsList));
    }

    public List<Alert> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<Alert> alerts) {
        this.alerts = alerts;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public void setName(WindowName name) {
        this.name = name;
    }

    public Milestone getMilestone() { return milestone; }

    public void setMilestone(Milestone milestone) { this.milestone = milestone; }
}
