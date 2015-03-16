package org.motechproject.scheduletracking.domain;

import org.joda.time.DateTime;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.motechproject.commons.date.util.DateUtil.now;

@Entity
public class Milestone {

    @Field
    private String name;

    @Field
    private Map<String, String> data = new HashMap<>();

    @Field
    private List<MilestoneWindow> milestoneWindows = new ArrayList<>();

    public Milestone() {
        this(null, null, null, null, null);
    }

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

    public List<Alert> getAlerts() {
        List<Alert> alerts = new ArrayList<Alert>();
        for (MilestoneWindow window : milestoneWindows) {
            alerts.addAll(window.getAlerts());
        }
        return alerts;
    }

    public Period getMaximumDuration() {
        return getWindowEnd(WindowName.max);
    }

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

    public Period getWindowDuration(WindowName windowName) {
        return getWindowEnd(windowName).minus(getWindowStart(windowName));
    }

    public boolean windowElapsed(WindowName windowName, DateTime milestoneStartDateTime) {
        Period endOffset = getWindowEnd(windowName);
        return !now().isBefore(milestoneStartDateTime.plus(endOffset));
    }
}
