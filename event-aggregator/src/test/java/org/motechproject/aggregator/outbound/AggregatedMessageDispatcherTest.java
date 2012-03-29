package org.motechproject.aggregator.outbound;

import org.junit.Test;
import org.motechproject.gateway.OutboundEventGateway;
import org.motechproject.model.MotechEvent;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AggregatedMessageDispatcherTest {
    @Test
    public void shouldSendTheMessagePayloadOnDispatch() {
        OutboundEventGateway gateway = mock(OutboundEventGateway.class);
        AggregatedMessageDispatcher dispatcher = new AggregatedMessageDispatcher(gateway);

        MotechEvent event = new MotechEvent("SUBJECT");
        dispatcher.dispatch(message(event));

        verify(gateway).sendEventMessage(event);
    }

    private Message<MotechEvent> message(final MotechEvent event) {
        return new Message<MotechEvent>() {
            @Override
            public MotechEvent getPayload() {
                return event;
            }

            @Override
            public MessageHeaders getHeaders() {
                throw new RuntimeException("Unsupported.");
            }
        };
    }
}
