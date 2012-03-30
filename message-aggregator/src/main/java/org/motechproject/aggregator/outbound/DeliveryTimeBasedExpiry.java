package org.motechproject.aggregator.outbound;

import ch.lambdaj.function.convert.Converter;
import org.motechproject.aggregator.aggregation.AggregationHandler;
import org.motechproject.aggregator.repository.MessageStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.store.MessageGroup;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import static ch.lambdaj.Lambda.*;

@Component
public class DeliveryTimeBasedExpiry<T extends Serializable> {
    private Logger logger = LoggerFactory.getLogger(DeliveryTimeBasedExpiry.class);
    private final AggregationHandler<T> aggregationHandler;

    @Autowired
    public DeliveryTimeBasedExpiry(AggregationHandler<T> aggregationHandler) {
        this.aggregationHandler = aggregationHandler;
    }

    public int expireMessageGroups(MessageStore<T> messageStore, long timeout) {
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

        List<T> eventsInPayload = convert(collect(messages, on(Message.class).getPayload()), new Converter<Object, T>() {
            @Override
            public T convert(Object from) {
                return (T) from;
            }
        });

        boolean canBeDispatched = aggregationHandler.canBeDispatched(eventsInPayload);
        logger.debug("Events: " + eventsInPayload + ". Can be dispatched? " + canBeDispatched);
        return canBeDispatched;
    }
}
