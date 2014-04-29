package org.motechproject.appointments.api.osgi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.appointments.api.EventKeys;
import org.motechproject.tasks.domain.Channel;
import org.motechproject.tasks.domain.TriggerEvent;
import org.motechproject.tasks.osgi.test.AbstractTaskBundleIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class AppointmentsTaskBundleIT extends AbstractTaskBundleIT {

    private static final String CHANNEL_NAME = "org.motechproject.motech-appointments-api";

    @Test
    public void testTaskChannelCreated() throws IOException, InterruptedException {
        waitForChannel(CHANNEL_NAME);
        Channel channel = findChannel(CHANNEL_NAME);
        assertNotNull(channel);
    }

    @Test
    public void testTaskTriggers() throws IOException, InterruptedException {
        waitForChannel(CHANNEL_NAME);
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
