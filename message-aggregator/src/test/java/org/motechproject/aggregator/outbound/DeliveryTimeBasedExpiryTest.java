package org.motechproject.aggregator.outbound;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.aggregator.aggregation.AggregationHandler;
import org.motechproject.aggregator.repository.MessageStore;
import org.motechproject.event.MotechEvent;
import org.springframework.integration.Message;
import org.springframework.integration.store.MessageGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class DeliveryTimeBasedExpiryTest {
    @Mock
    AggregationHandler<MotechEvent> aggregationHandler;
    @Mock
    MessageStore messageStore;
    @Mock
    Message<?> firstMessage;
    @Mock
    Message<?> secondMessage;

    private DeliveryTimeBasedExpiry<MotechEvent> timeBasedExpiry;
    private List<MotechEvent> events;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        timeBasedExpiry = new DeliveryTimeBasedExpiry<MotechEvent>(aggregationHandler);
        events = Arrays.asList(new MotechEvent("SUBJECT 1"), new MotechEvent("SUBJECT 2"));
    }

    @Test
    public void shouldDoNothingWhenThereAreNoMessageGroupsInTheStore() {
        when(messageStore.iterator()).thenReturn(Collections.<MessageGroup>emptyList().iterator());

        timeBasedExpiry.expireMessageGroups(messageStore, 0);

        verify(messageStore).iterator();
        verifyNoMoreInteractions(messageStore);
        verifyNoMoreInteractions(aggregationHandler);
    }

    @Test
    public void shouldNotExpireAGroupWhenTheAggregationHandlerSaysThatItCannotBeDispatched() {
        MessageGroup group = group();
        when(messageStore.iterator()).thenReturn(Arrays.asList(group).iterator());
        when(aggregationHandler.canBeDispatched(events)).thenReturn(false);

        timeBasedExpiry.expireMessageGroups(messageStore, 0);

        verify(messageStore).iterator();
        verify(aggregationHandler).canBeDispatched(events);
        verifyNoMoreInteractions(messageStore);
        verifyNoMoreInteractions(aggregationHandler);
    }

    @Test
    public void shouldExpireAGroupWhenTheAggregationHandlerSaysThatItCanBeDispatched() {
        MessageGroup group = group();
        when(messageStore.iterator()).thenReturn(Arrays.asList(group).iterator());
        when(aggregationHandler.canBeDispatched(events)).thenReturn(true);

        timeBasedExpiry.expireMessageGroups(messageStore, 0);

        verify(messageStore).iterator();
        verify(messageStore).expire(group);
        verify(aggregationHandler).canBeDispatched(events);
        verifyNoMoreInteractions(messageStore);
        verifyNoMoreInteractions(aggregationHandler);
    }

    @Test
    public void shouldExpireEachGroupWhichCanBeDispatched() {
        MessageGroup firstGroup = group();
        MessageGroup secondGroup = group();
        MessageGroup thirdGroup = group();
        when(messageStore.iterator()).thenReturn(Arrays.asList(firstGroup, secondGroup, thirdGroup).iterator());
        when(aggregationHandler.canBeDispatched(events)).thenReturn(true, false, true);

        timeBasedExpiry.expireMessageGroups(messageStore, 0);

        verify(messageStore).expire(firstGroup);
        verify(messageStore).expire(thirdGroup);

        verify(messageStore, times(0)).expire(secondGroup);
    }

    private MessageGroup group() {
        ArrayList<Message<?>> messages = new ArrayList<Message<?>>();
        messages.add(firstMessage);
        messages.add(secondMessage);

        when(firstMessage.getPayload()).thenReturn(events.get(0));
        when(secondMessage.getPayload()).thenReturn(events.get(1));

        MessageGroup group = mock(MessageGroup.class);
        when(group.getMessages()).thenReturn(messages);
        return group;
    }
}
