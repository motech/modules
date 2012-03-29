package org.motechproject.testaggregation;

import org.motechproject.aggregator.aggregation.AggregationHandler;
import org.motechproject.model.MotechEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AggregationBySubject implements AggregationHandler {
    @Override
    public String groupId(MotechEvent event) {
        return event.getSubject();
    }

    @Override
    public boolean canBeDispatched(List<MotechEvent> events) {
/*
        DateTime creationTime = (DateTime) event.getParameters().get("CREATION_TIME");
        boolean can = new NextMondayDispatcher().deliveryDate(creationTime).toDate().before(DateUtil.now().toDate());
*/
        if (events.size() == 2) {
            return true;
        }
        return false;
    }
}
