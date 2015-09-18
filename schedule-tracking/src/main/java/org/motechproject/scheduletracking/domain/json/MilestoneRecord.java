package org.motechproject.scheduletracking.domain.json;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <code>MilestoneRecord</code> is used keep the deserialized form of a milestone from json format.
 * @see org.motechproject.scheduletracking.domain.Milestone
 */
public class MilestoneRecord {

    @JsonProperty
    private String name;

    @JsonProperty
    private ScheduleWindowsRecord scheduleWindows;

    @JsonProperty
    private List<AlertRecord> alerts = new ArrayList<AlertRecord>();

    @JsonProperty
    private Map<String, String> data = new HashMap<String, String>();

    public String name() {
        return name;
    }

    public ScheduleWindowsRecord scheduleWindowsRecord() {
        return scheduleWindows;
    }

    public List<AlertRecord> alerts() {
        return alerts;
    }

    public Map<String, String> data() {
        return data;
    }

}
