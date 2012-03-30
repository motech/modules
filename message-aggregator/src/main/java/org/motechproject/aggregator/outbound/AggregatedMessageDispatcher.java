package org.motechproject.aggregator.outbound;

import org.motechproject.gateway.OutboundEventGateway;
import org.motechproject.model.MotechEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.stereotype.Service;

@Service("aggregatedMessageDispatcher")
public class AggregatedMessageDispatcher {
    private final OutboundEventGateway gateway;
    private static Logger logger = LoggerFactory.getLogger(AggregatedMessageDispatcher.class.toString());

    @Autowired
    public AggregatedMessageDispatcher(OutboundEventGateway gateway) {
        this.gateway = gateway;
    }

    public void dispatch(Message message) {
        MotechEvent aggregatedEvent = (MotechEvent) message.getPayload();
        logger.debug("Sending aggregated event on outbound event gateway: " + aggregatedEvent);
        gateway.sendEventMessage(aggregatedEvent);
    }
}
