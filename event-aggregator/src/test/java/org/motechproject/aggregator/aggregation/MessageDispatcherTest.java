package org.motechproject.aggregator.aggregation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.model.MotechEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

public class MessageDispatcherTest {
    @Mock
    private AggregationHandler handler;

    private MessageDispatcher dispatcher;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        dispatcher = new MessageDispatcher(handler);
    }

    @Test
    public void shouldUseAggregationHandlerToCorrelateEvents() {
        MotechEvent event = new MotechEvent("SUBJECT");

        dispatcher.correlate(event);

        verify(handler).groupId(event);
    }

    @Test
    public void shouldCreateAnAggregateEventFromAListOfEvents() {
        List<MotechEvent> events = Arrays.asList(new MotechEvent("SUBJECT 1"), new MotechEvent("SUBJECT 2"));

        MotechEvent aggregatedEvent = dispatcher.aggregateEvents(events);

        assertThat(aggregatedEvent.getSubject(), is(AggregateMotechEvent.SUBJECT));
        assertThat((List<MotechEvent>) aggregatedEvent.getParameters().get(AggregateMotechEvent.EVENTS_KEY), is(events));
        verifyZeroInteractions(handler);
    }

    @Test
    public void shouldNeverSayThatEventsAreDispatchable() {
        assertThat(dispatcher.canBeDispatched(null), is(false));
        assertThat(dispatcher.canBeDispatched(new ArrayList<MotechEvent>()), is(false));
        assertThat(dispatcher.canBeDispatched(Arrays.asList(new MotechEvent("SUBJECT"))), is(false));
    }
}
