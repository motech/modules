package org.motechproject.aggregator.aggregation;

import org.motechproject.model.MotechEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AggregateMotechEvent<T> {
    public static final String SUBJECT = "AGGREGATED_EVENT";
    public static final String EVENTS_KEY = "EVENTS";
    private final List<T> events;

    public AggregateMotechEvent(List<T> events) {
        this.events = events;
    }

    public MotechEvent toMotechEvent() {
        Map<String,Object> parameters = new HashMap<String, Object>();
        parameters.put(EVENTS_KEY, events);
        return new MotechEvent(SUBJECT, parameters);
    }
}
