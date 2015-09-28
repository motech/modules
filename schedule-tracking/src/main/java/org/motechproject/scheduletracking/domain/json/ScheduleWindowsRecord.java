package org.motechproject.scheduletracking.domain.json;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * <code>ScheduleWindowsRecord</code> is used keep the deserialized form of a schedule window from json format.
 * @see org.motechproject.scheduletracking.domain.MilestoneWindow
 */
public class ScheduleWindowsRecord {

    @JsonProperty
    private List<String> earliest = new ArrayList<String>();

    @JsonProperty
    private List<String> due = new ArrayList<String>();

    @JsonProperty
    private List<String> late = new ArrayList<String>();

    @JsonProperty
    private List<String> max = new ArrayList<String>();

    public List<String> earliest() {
        return earliest;
    }

    public List<String> due() {
        return due;
    }

    public List<String> late() {
        return late;
    }

    public List<String> max() {
        return max;
    }
}
