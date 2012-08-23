package org.motechproject.aggregator.aggregation;

import org.motechproject.event.MotechEvent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AggregateMotechEvent<T extends Serializable> {
    public static final String SUBJECT = "AGGREGATED_EVENT";
    public static final String VALUES_KEY = "VALUES";
    private final List<T> values;

    public AggregateMotechEvent(List<T> values) {
        this.values = values;
    }

    public MotechEvent toMotechEvent() {
        Map<String,Object> parameters = new HashMap<String, Object>();
        parameters.put(VALUES_KEY, values);
        return new MotechEvent(SUBJECT, parameters);
    }
}
