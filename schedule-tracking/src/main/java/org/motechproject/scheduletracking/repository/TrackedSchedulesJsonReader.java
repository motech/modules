package org.motechproject.scheduletracking.repository;

import org.motechproject.scheduletracking.domain.json.ScheduleRecord;

import java.util.List;

/**
 * <code>TrackedSchedulesJsonReader</code> interface provide methods for creating
 * {@link org.motechproject.scheduletracking.domain.json.ScheduleRecord} form json data and json files.
 */
public interface TrackedSchedulesJsonReader {

    /**
     * Builds schedule record from the given json data.
     *
     * @param schduleJson the json data with schedule definition
     * @return the schedule record
     */
    ScheduleRecord getSchedule(String schduleJson);

    /**
     * Builds schedules records from the given directory which is in the classpath. Only files with 'json' extension
     * will be scanned.
     *
     * @param definitionsDirectoryName the name of the directory which is in the classpath
     * @return the list of the the schedule records
     */
    List<ScheduleRecord> getAllSchedules(String definitionsDirectoryName);
}
