package org.motechproject.aggregator.outbound;

import org.motechproject.gateway.OutboundEventGateway;
import org.motechproject.model.MotechEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.stereotype.Service;

@Service("aggregatedMessageDispatcher")
public class AggregatedMessageDispatcher {
    private final OutboundEventGateway gateway;

    @Autowired
    public AggregatedMessageDispatcher(OutboundEventGateway gateway) {
        this.gateway = gateway;
    }

    public void dispatch(Message message) {
        MotechEvent aggregatedEvent = (MotechEvent) message.getPayload();
        gateway.sendEventMessage(aggregatedEvent);
    }
}
