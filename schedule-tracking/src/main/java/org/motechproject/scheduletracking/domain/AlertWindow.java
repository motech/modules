package org.motechproject.scheduletracking.domain;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.motechproject.commons.date.model.Time;
import org.motechproject.commons.date.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.commons.date.util.DateUtil.greaterThanOrEqualTo;
import static org.motechproject.commons.date.util.DateUtil.lessThan;
import static org.motechproject.commons.date.util.DateUtil.newDateTime;
import static org.motechproject.commons.date.util.DateUtil.now;

/**
 * Contains details about alerts timings. Used to create events and to compute timings.
 */
public class AlertWindow {

    private DateTime enrolledOn;

    private Alert alert;

    private DateTime alertWindowStart;

    private DateTime alertWindowEnd;

    private List<DateTime> schedulableAlertTimings;

    private Time preferredAlertTime;

    private final List<DateTime> allAlertTimings;

    /**
     * Creates an AlertWindow with the windowStart attribute set to {@code windowStart}, the windowEnd attribute set to {@code windowEnd},
     * the enrolledOn attribute set to {@code enrolledOn}, the preferredAlertTime attribute set to {@code preferredAlertTime} and
     * the alert attribute set to {@code alert}. Also computes all alert timings.
     *
     * @param windowStart the start date and time of the window
     * @param windowEnd the end date and time of the window
     * @param enrolledOn the enrollment date and time
     * @param preferredAlertTime the preferred alert time
     * @param alert the alert
     */
    public AlertWindow(DateTime windowStart, DateTime windowEnd, DateTime enrolledOn, Time preferredAlertTime, Alert alert) {
        this.alertWindowStart = toPreferredTime(windowStart.plus(alert.getOffset()), preferredAlertTime);
        this.alertWindowEnd = windowEnd;
        this.preferredAlertTime = preferredAlertTime;
        this.enrolledOn = enrolledOn;
        this.alert = alert;

        allAlertTimings = computeAllAlertTimings();
        schedulableAlertTimings = alertsFallingInAlertWindow(allAlertTimings);
    }

    /**
     * Returns the number of the alerts to schedule.
     *
     * @return the number of the alerts to schedule
     */
    public int numberOfAlertsToSchedule() {
        return schedulableAlertTimings.size();
    }

    /**
     * Returns the alert start date and time.
     *
     * @return the alert start date and time.
     */
    public DateTime scheduledAlertStartDate() {
        if (schedulableAlertTimings.size() == 0) {
            return null;
        }
        return schedulableAlertTimings.get(0);
    }

    /**
     * Returns all possible alerts timings.
     *
     * @return all possible alerts timings.
     */
    public List<DateTime> allPossibleAlerts() {
        return allAlertTimings;

    }

    private List<DateTime> computeAllAlertTimings() {
        List<DateTime> alertTimings = new ArrayList<>();

        if (alert.getCount() > 0) {
            alertTimings.add(alertWindowStart);
        }
        for (int alertIndex = 1; alertIndex < alert.getCount(); alertIndex++) {
            DateTime previousAlertTime = alertTimings.get(alertIndex - 1);
            alertTimings.add(previousAlertTime.plus(alert.getInterval()));
        }

        boolean isDelayed = earliestValidAlertDateTime().isAfter(alertWindowStart);
        if (alert.isFloating() && isDelayed) {
            return floatWindowAndAlertTimings(alertTimings);
        }

        return alertTimings;
    }

    private List<DateTime> floatWindowAndAlertTimings(List<DateTime> alertTimings) {
        List<DateTime> floatedAlertTimings = new ArrayList<>();

        DateTime floatingAlertsStartDateTime = newDateTime(earliestValidAlertDateTime().toLocalDate(), new Time(alertWindowStart.getHourOfDay(), alertWindowStart.getMinuteOfHour()));

        Period periodToBeFloatedWith = new Period(alertWindowStart, floatingAlertsStartDateTime);
        alertWindowStart = alertWindowStart.plus(periodToBeFloatedWith);

        // Schedule floating alerts from tomorrow, if today's alert time has already passed
        if (preferredAlertTime != null && alertWindowStart.isBefore(DateUtil.now())) {
            periodToBeFloatedWith = periodToBeFloatedWith.plusDays(1);
        }

        for (DateTime alertTime : alertTimings) {
            floatedAlertTimings.add(alertTime.plus(periodToBeFloatedWith));
        }

        return floatedAlertTimings;
    }

    private List<DateTime> alertsFallingInAlertWindow(List<DateTime> alertTimings) {
        List<DateTime> alertsWithInEndDate = filterAlertsBeyondEndDate(alertTimings);
        return filterElapsedAlerts(alertsWithInEndDate);
    }

    private List<DateTime> filterElapsedAlerts(List<DateTime> alertsWithInEndDate) {
        DateTime start = earliestValidAlertDateTime();

        // In case of floating alerts with no preferred alert time, don't filter out today's alerts.
        if (alert.isFloating() && preferredAlertTime == null) {
            start = newDateTime(start.toLocalDate(), new Time(0, 0));
        }

        return greaterThanOrEqualTo(start, alertsWithInEndDate);
    }

    private List<DateTime> filterAlertsBeyondEndDate(List<DateTime> alertTimings) {
        return lessThan(alertWindowEnd, alertTimings);
    }

    private DateTime toPreferredTime(DateTime alertTime, Time preferredTime) {
        if (preferredTime == null) {
            return alertTime;
        }
        return newDateTime(alertTime.toLocalDate(), preferredTime.getHour(), preferredTime.getMinute(), 0);
    }

    private DateTime earliestValidAlertDateTime() {
        DateTime now = now();
        return !enrolledOn.isAfter(now) ? now : enrolledOn;
    }
}
