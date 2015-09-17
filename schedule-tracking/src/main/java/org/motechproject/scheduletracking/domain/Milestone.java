package org.motechproject.scheduletracking.domain;

import org.joda.time.DateTime;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.motechproject.mds.annotations.CrudEvents;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.Ignore;
import org.motechproject.mds.event.CrudEventType;

import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.Unique;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.motechproject.commons.date.util.DateUtil.now;

/**
 * Milestones represent windows of time in which certain criteria, treatments, courses, etc. should be fulfilled before
 * moving on to the next milestone. Each milestone contains windows in which alerts will be raised.
 */
@Entity
@CrudEvents(CrudEventType.NONE)
@Unique(name ="UNIQUE_NAME_SCHEDULE_COMPOSITE_IDX", members = {"name", "schedule"})
public class Milestone {

    /**
     * The name of the milestone.
     */
    @Field
    private String name;

    /**
     * The data represents additional milestone data.
     */
    @Field
    private Map<String, String> data = new HashMap<>();

    /**
     * The milestone windows. Each milestone contains 4 windows.
     */
    @Field
    @Cascade(delete = true)
    @Persistent(mappedBy = "milestone")
    private List<MilestoneWindow> milestoneWindows = new ArrayList<>();

    /**
     * The schedule to which this milestone belongs.
     */
    @Field
    private Schedule schedule;

    /**
     * Creates Milestone.
     */
    public Milestone() {
        this(null, null, null, null, null);
    }

    /**
     * Creates Milestone with the name attribute set to {@code name}. The {@code earliest}, {@code due}, {@code late}
     * and {@code max} attributes are used to create {@code milestoneWindows}.
     *
     * @param name the name of the milestone
     * @param earliest the duration of the earliest window
     * @param due the duration of the due window
     * @param late the duration of the late window
     * @param max the duration of the max window
     */
    public Milestone(String name, Period earliest, Period due, Period late, Period max) {
        this.name = name;
        createMilestoneWindows(earliest, due, late, max);
    }

    private void createMilestoneWindows(Period earliest, Period due, Period late, Period max) {
        milestoneWindows.add(new MilestoneWindow(WindowName.earliest, earliest));
        milestoneWindows.add(new MilestoneWindow(WindowName.due, due));
        milestoneWindows.add(new MilestoneWindow(WindowName.late, late));
        milestoneWindows.add(new MilestoneWindow(WindowName.max, max));
    }

    public List<MilestoneWindow> getMilestoneWindows() {
        return milestoneWindows;
    }

    public MilestoneWindow getMilestoneWindow(WindowName windowName) {
        for (MilestoneWindow window : milestoneWindows) {
            if (window.getName().equals(windowName)) {
                return window;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void addAlert(WindowName windowName, Alert... alertList) {
        getMilestoneWindow(windowName).addAlerts(alertList);
    }

    /**
     * Returns all the alerts from all milestone windows.
     *
     * @return the list of the alerts
     */
    @Ignore
    public List<Alert> getAlerts() {
        List<Alert> alerts = new ArrayList<Alert>();
        for (MilestoneWindow window : milestoneWindows) {
            alerts.addAll(window.getAlerts());
        }
        return alerts;
    }

    /**
     * Returns the maximum duration of the milestone.
     *
     * @return the maximum milestone duration.
     */
    @Ignore
    public Period getMaximumDuration() {
        return getWindowEnd(WindowName.max);
    }

    /**
     * Returns the period required to start the milestone window with the given name.
     *
     * @param windowName the name of the milestone window
     * @return the period required to start the milestone window
     */
    public Period getWindowStart(WindowName windowName) {
        MutablePeriod period = new MutablePeriod();
        for (MilestoneWindow window : milestoneWindows) {
            if (window.getName().equals(windowName)) {
                break;
            }
            period.add(window.getPeriod());
        }
        return period.toPeriod();
    }

    /**
     * Returns the period required to end the milestone window with the given name.
     *
     * @param windowName
     * @return the period required to end the milestone window
     */
    public Period getWindowEnd(WindowName windowName) {
        MutablePeriod period = new MutablePeriod();
        for (MilestoneWindow window : milestoneWindows) {
            period.add(window.getPeriod());
            if (window.getName().equals(windowName)) {
                break;
            }
        }
        return period.toPeriod();
    }

    /**
     * Returns the duration of the milestone window with the given name.
     *
     * @param windowName the name of the milestone window
     * @return the the duration of the time window
     */
    public Period getWindowDuration(WindowName windowName) {
        return getWindowEnd(windowName).minus(getWindowStart(windowName));
    }

    /**
     * Returns true when the milestone window with the given name elapsed, false otherwise.
     *
     * @param windowName tne name of the milestone window
     * @param milestoneStartDateTime the milestone start date and time
     * @return true when the milestone window elapsed, otherwise false
     */
    public boolean windowElapsed(WindowName windowName, DateTime milestoneStartDateTime) {
        Period endOffset = getWindowEnd(windowName);
        return !now().isBefore(milestoneStartDateTime.plus(endOffset));
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
