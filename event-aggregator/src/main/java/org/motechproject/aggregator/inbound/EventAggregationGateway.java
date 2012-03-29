package org.motechproject.aggregator.inbound;

import org.springframework.integration.annotation.Gateway;

public interface EventAggregationGateway<T> {
    @Gateway(requestChannel="inputMessages")
    public void dispatch(T message);
}
