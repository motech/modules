package org.motechproject.scheduletracking.domain.json;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * <code>ScheduleRecord</code> is used to keep the deserialized form of a schedule from json format.
 * @see org.motechproject.scheduletracking.domain.Schedule
 */
public class ScheduleRecord {

    @JsonProperty
    private String name;

    @JsonProperty
    private boolean absolute;

    @JsonProperty
    private List<MilestoneRecord> milestones = new ArrayList<MilestoneRecord>();

    public String name() {
        return name;
    }

    public List<MilestoneRecord> milestoneRecords() {
        return milestones;
    }

    @JsonIgnore
    public boolean isAbsoluteSchedule() {
        return absolute;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ScheduleRecord that = (ScheduleRecord) o;

        if (!name.equals(that.name)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
