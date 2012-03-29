package org.motechproject.aggregator.inbound;

import org.motechproject.model.MotechEvent;
import org.springframework.integration.annotation.Gateway;

public interface EventAggregationGateway {
    @Gateway(requestChannel="inputMessages")
    public void dispatch(MotechEvent message);
}
