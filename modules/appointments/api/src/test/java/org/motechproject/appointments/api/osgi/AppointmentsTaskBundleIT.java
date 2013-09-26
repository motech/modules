package org.motechproject.appointments.api.osgi;

import org.motechproject.appointments.api.EventKeys;
import org.motechproject.tasks.domain.Channel;
import org.motechproject.tasks.domain.TriggerEvent;
import org.motechproject.tasks.osgi.test.AbstractTaskBundleIT;

import java.io.IOException;
import java.util.List;

public class AppointmentsTaskBundleIT extends AbstractTaskBundleIT {

    private static final String CHANNEL_NAME = "org.motechproject.motech-appointments-api";

    public void testTaskChannelCreated() throws IOException {
        Channel channel = findChannel(CHANNEL_NAME);

        assertNotNull(channel);
    }

    public void testTaskTriggers() throws IOException {
        Channel channel = findChannel(CHANNEL_NAME);

        assertVisitTrigger(channel.getTriggerTaskEvents());
        assertAppointmentTrigger(channel.getTriggerTaskEvents());
    }

    private void assertVisitTrigger(List<TriggerEvent> triggerTaskEvents) {
        TriggerEvent visitTaskEvent = findTriggerEventBySubject(triggerTaskEvents, EventKeys.VISIT_REMINDER_EVENT_SUBJECT);

        assertNotNull(visitTaskEvent);
        assertTrue(hasEventParameterKey(EventKeys.EXTERNAL_ID_KEY, visitTaskEvent.getEventParameters()));
        assertTrue(hasEventParameterKey(EventKeys.VISIT_NAME, visitTaskEvent.getEventParameters()));
    }

    private void assertAppointmentTrigger(List<TriggerEvent> triggerTaskEvents) {
        TriggerEvent appointmentTaskEvent = findTriggerEventBySubject(triggerTaskEvents,
                EventKeys.APPOINTMENT_REMINDER_EVENT_SUBJECT);

        assertNotNull(appointmentTaskEvent);
        assertTrue(hasEventParameterKey(EventKeys.EXTERNAL_ID_KEY, appointmentTaskEvent.getEventParameters()));
        assertTrue(hasEventParameterKey(EventKeys.VISIT_NAME, appointmentTaskEvent.getEventParameters()));
    }
}
