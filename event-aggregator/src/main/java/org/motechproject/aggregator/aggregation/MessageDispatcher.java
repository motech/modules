package org.motechproject.aggregator.aggregation;

import org.motechproject.model.MotechEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.CorrelationStrategy;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ReleaseStrategy;

import java.io.Serializable;
import java.util.List;

@MessageEndpoint
public class MessageDispatcher<T extends Serializable> {
    private AggregationHandler aggregationHandler;
    private static Logger logger = LoggerFactory.getLogger(MessageDispatcher.class.toString());

    @Autowired
    public MessageDispatcher(AggregationHandler<T> aggregationHandler) {
        this.aggregationHandler = aggregationHandler;
    }

    public MotechEvent aggregateEvents(List<T> events) {
        return new AggregateMotechEvent<T>(events).toMotechEvent();
    }

    @CorrelationStrategy
    public String correlate(T event) {
        String groupId = aggregationHandler.groupId(event);
        logger.debug("Group ID of event: " + event + " is " + groupId);
        return groupId;
    }

    @ReleaseStrategy
    public boolean canBeDispatched(List<T> events){
        return false;
    }

}
