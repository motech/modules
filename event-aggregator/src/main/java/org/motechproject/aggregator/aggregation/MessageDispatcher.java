package org.motechproject.aggregator.aggregation;

import org.motechproject.model.MotechEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.CorrelationStrategy;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ReleaseStrategy;

import java.util.List;

@MessageEndpoint
public class MessageDispatcher {
    private AggregationHandler aggregationHandler;
    private static Logger logger = LoggerFactory.getLogger(MessageDispatcher.class.toString());

    @Autowired
    public MessageDispatcher(AggregationHandler aggregationHandler) {
        this.aggregationHandler = aggregationHandler;
    }

    public MotechEvent aggregateEvents(List<MotechEvent> events) {
        return new AggregateMotechEvent(events).toMotechEvent();
    }

    @CorrelationStrategy
    public String correlate(MotechEvent event) {
        String groupId = aggregationHandler.groupId(event);
        logger.debug("Group ID of event: " + event + " is " + groupId);
        return groupId;
    }

    @ReleaseStrategy
    public boolean canBeDispatched(List<MotechEvent> events){
        return false;
    }

}
