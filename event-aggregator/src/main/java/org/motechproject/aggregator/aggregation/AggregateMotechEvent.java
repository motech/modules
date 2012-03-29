package org.motechproject.aggregator.aggregation;

import org.motechproject.model.MotechEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AggregateMotechEvent {
    public static final String SUBJECT = "AGGREGATED_EVENT";
    public static final String EVENTS_KEY = "EVENTS";
    private final List<MotechEvent> events;

    public AggregateMotechEvent(List<MotechEvent> events) {
        this.events = events;
    }

    public MotechEvent toMotechEvent() {
        Map<String,Object> parameters = new HashMap<String, Object>();
        parameters.put(EVENTS_KEY, events);
        return new MotechEvent(SUBJECT, parameters);
    }
}
