package org.motechproject.aggregator.inbound;

import org.springframework.integration.annotation.Gateway;

import java.io.Serializable;

public interface EventAggregationGateway<T extends Serializable> {
    @Gateway(requestChannel="inputMessages")
    public void dispatch(T message);
}
