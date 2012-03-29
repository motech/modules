package org.motechproject.aggregator.aggregation;

import java.util.List;

public interface AggregationHandler<T> {
    String groupId(T event);

    boolean canBeDispatched(List<T> events);
}
