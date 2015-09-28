package org.motechproject.scheduletracking.domain;

import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.ReadWritablePeriod;
import org.motechproject.commons.date.util.JodaFormatter;
import org.motechproject.scheduletracking.domain.exception.InvalidScheduleDefinitionException;
import org.motechproject.scheduletracking.domain.json.AlertRecord;
import org.motechproject.scheduletracking.domain.json.MilestoneRecord;
import org.motechproject.scheduletracking.domain.json.ScheduleRecord;
import org.motechproject.scheduletracking.domain.json.ScheduleWindowsRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ScheduleFactory {
    public static final Period EMPTY_PERIOD = Period.ZERO;

    /**
     * Creates a ScheduleFactory.
     */
    public ScheduleFactory() {
    }

    /**
     * Builds a schedule form the given schedule record details, default locale will be used.
     *
     * @param scheduleRecord the schedule record with details about the schedule, milestones and alerts
     * @return the schedule
     */
    public Schedule build(ScheduleRecord scheduleRecord) {
        return build(scheduleRecord, Locale.getDefault());
    }

    /**
     * Builds a schedule form the given schedule record details
     *
     * @param scheduleRecord the schedule record with details about the schedule, milestones and alerts
     * @param locale the locale to use for parsing
     * @return the schedule
     */
    public Schedule build(ScheduleRecord scheduleRecord, Locale locale) {
        Schedule schedule = new Schedule(scheduleRecord.name());
        schedule.setBasedOnAbsoluteWindows(scheduleRecord.isAbsoluteSchedule());
        int alertIndex = 0;
        Period previousWindowEnd = new Period();

        for (MilestoneRecord milestoneRecord : scheduleRecord.milestoneRecords()) {
            Map<WindowName, List<String>> values = getWindowValues(milestoneRecord.scheduleWindowsRecord());

            Map<WindowName, Period> windowDurations = new HashMap<WindowName, Period>();
            Map<WindowName, Period> windowStarts = new HashMap<WindowName, Period>();
            for (WindowName windowName : WindowName.values()) {
                Period currentWindowEnd = getPeriodFromValue(values.get(windowName), locale);
                windowStarts.put(windowName, previousWindowEnd);
                if (currentWindowEnd.equals(EMPTY_PERIOD)) {
                    windowDurations.put(windowName, EMPTY_PERIOD);
                } else {
                    windowDurations.put(windowName, currentWindowEnd.minus(previousWindowEnd));
                    previousWindowEnd = currentWindowEnd;
                }
            }
            if (!scheduleRecord.isAbsoluteSchedule()) {
                previousWindowEnd = EMPTY_PERIOD;
            }

            Milestone milestone = new Milestone(milestoneRecord.name(), windowDurations.get(WindowName.earliest),
                    windowDurations.get(WindowName.due), windowDurations.get(WindowName.late), windowDurations.get(WindowName.max));
            milestone.setData(milestoneRecord.data());
            addAlertsToMilestone(milestone, milestoneRecord.alerts(), windowStarts, scheduleRecord.isAbsoluteSchedule(), alertIndex, locale);
            schedule.addMilestones(milestone);

            alertIndex += milestoneRecord.alerts().size();
        }
        return schedule;
    }

    private void addAlertsToMilestone(Milestone milestone, List<AlertRecord> alerts, Map<WindowName, Period> windowStarts,
                                      boolean isAbsoluteAlert, int alertIndex, Locale locale) {
        int localAlertIndex = alertIndex;
        for (AlertRecord alertRecord : alerts) {
            Period offset = getPeriodFromValue(alertRecord.offset(), locale);
            if (isAbsoluteAlert) {
                offset = offset.minus(windowStarts.get(WindowName.valueOf(alertRecord.window())));
                if (alertRecord.isFloating()) {
                    throw new InvalidScheduleDefinitionException("Cannot define floating alerts for absolute schedules.");
                }
            }
            milestone.addAlert(WindowName.valueOf(alertRecord.window()), new Alert(offset, getPeriodFromValue(alertRecord.interval(), locale),
                    Integer.parseInt(alertRecord.count()), localAlertIndex++, alertRecord.isFloating()));
        }
    }

    private Map<WindowName, List<String>> getWindowValues(ScheduleWindowsRecord windowsRecord) {
        Map<WindowName, List<String>> values = new HashMap<WindowName, List<String>>();
        values.put(WindowName.earliest, windowsRecord.earliest());
        values.put(WindowName.due, windowsRecord.due());
        values.put(WindowName.late, windowsRecord.late());
        values.put(WindowName.max, windowsRecord.max());
        return values;
    }

    private Period getPeriodFromValue(List<String> readableValues, Locale locale) {
        ReadWritablePeriod period = new MutablePeriod();
        final JodaFormatter jodaFormatter = new JodaFormatter();
        for (String s : readableValues) {
            period.add(jodaFormatter.parse(s, locale));
        }
        return period.toPeriod();
    }
}
