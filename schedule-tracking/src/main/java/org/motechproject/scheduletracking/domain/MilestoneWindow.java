package org.motechproject.scheduletracking.domain;

import org.joda.time.Period;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class MilestoneWindow {

    @Field
    private WindowName name;

    @Field
    private Period period;

    @Field
    private List<Alert> alerts = new ArrayList<Alert>();

    public MilestoneWindow() {
    }

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
}
