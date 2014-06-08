package org.motechproject.scheduletracking.repository;

import org.motechproject.scheduletracking.domain.json.ScheduleRecord;

import java.util.List;

public interface TrackedSchedulesJsonReader {
    ScheduleRecord getSchedule(String schduleJson);
    List<ScheduleRecord> getAllSchedules(String definitionsDirectoryName);
}
