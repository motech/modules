package org.motechproject.testaggregation;

import org.motechproject.aggregator.aggregation.AggregationHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AggregationBySubject implements AggregationHandler<String> {
    @Override
    public String groupId(String value) {
        return value;
    }

    @Override
    public boolean canBeDispatched(List<String> values) {
        return values.size() == 2;
    }
}
