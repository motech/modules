package org.motechproject.aggregator.aggregation;

import org.motechproject.model.MotechEvent;

import java.util.List;

public interface AggregationHandler {
    String groupId(MotechEvent event);

    boolean canBeDispatched(List<MotechEvent> events);
}
