package org.motechproject.aggregator.outbound;

import ch.lambdaj.function.convert.Converter;
import org.motechproject.aggregator.aggregation.AggregationHandler;
import org.motechproject.aggregator.repository.MessageStore;
import org.motechproject.model.MotechEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.store.MessageGroup;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

import static ch.lambdaj.Lambda.*;

@Component
public class DeliveryTimeBasedExpiry {
    private Logger logger = LoggerFactory.getLogger(DeliveryTimeBasedExpiry.class);
    private final AggregationHandler aggregationHandler;

    @Autowired
    public DeliveryTimeBasedExpiry(AggregationHandler aggregationHandler) {
        this.aggregationHandler = aggregationHandler;
    }

    public int expireMessageGroups(MessageStore messageStore, long timeout) {
        int count = 0;

        for (MessageGroup group : messageStore) {
            if (!canBeDelivered(group)) {
                continue;
            }

            count++;
            try {
                messageStore.expire(group);
            } catch (Exception e) {
                logger.error("Exception while expiring message group, " + group, e);
            }
        }

        return count;
    }

    private boolean canBeDelivered(MessageGroup group) {
        final Collection<Message<?>> messages = group.getMessages();

        if (messages.size() <= 0) {
            return false;
        }

        List<MotechEvent> eventsInPayload = convert(collect(messages, on(Message.class).getPayload()), new Converter<Object, MotechEvent>() {
            @Override
            public MotechEvent convert(Object from) {
                return (MotechEvent) from;
            }
        });

        return aggregationHandler.canBeDispatched(eventsInPayload);
    }
}
