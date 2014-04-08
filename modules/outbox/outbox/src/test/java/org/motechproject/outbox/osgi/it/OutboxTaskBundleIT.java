package org.motechproject.outbox.osgi.it;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.outbox.api.EventKeys;
import org.motechproject.tasks.domain.ActionEvent;
import org.motechproject.tasks.domain.Channel;
import org.motechproject.tasks.domain.TriggerEvent;
import org.motechproject.tasks.osgi.test.AbstractTaskBundleIT;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class OutboxTaskBundleIT extends AbstractTaskBundleIT {

    private static final String CHANNEL_NAME = "org.motechproject.motech-outbox";

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

        assertNotNull(channel);
        assertExecuteOutboxTrigger(channel.getTriggerTaskEvents());
        assertIncompleteOutboxCallTrigger(channel.getTriggerTaskEvents());
        assertCompletedOutboxCallTrigger(channel.getTriggerTaskEvents());
        assertMaxPendingMessagesTrigger(channel.getTriggerTaskEvents());
    }

    @Test
    public void testTaskActions() throws IOException, InterruptedException {
        waitForChannel(CHANNEL_NAME);
        Channel channel = findChannel(CHANNEL_NAME);

        assertNotNull(channel);
        assertExecuteOutboxAction(channel.getActionTaskEvents());
        assertScheduleExecutionAction(channel.getActionTaskEvents());
        assertUnscheduleExecutionAction(channel.getActionTaskEvents());
    }

    private void assertExecuteOutboxTrigger(List<TriggerEvent> triggerTaskEvents) {
        TriggerEvent executeOutboxTrigger = findTriggerEventBySubject(triggerTaskEvents, EventKeys.EXECUTE_OUTBOX_SUBJECT);

        assertNotNull(executeOutboxTrigger);
        assertTrue(hasEventParameterKey(EventKeys.EXTERNAL_ID_KEY, executeOutboxTrigger.getEventParameters()));
        assertTrue(hasEventParameterKey(EventKeys.PHONE_NUMBER_KEY, executeOutboxTrigger.getEventParameters()));
        assertTrue(hasEventParameterKey(EventKeys.LANGUAGE_KEY, executeOutboxTrigger.getEventParameters()));
    }

    private void assertIncompleteOutboxCallTrigger(List<TriggerEvent> triggerTaskEvents) {
        TriggerEvent incompleteOutboxCallTrigger = findTriggerEventBySubject(triggerTaskEvents,
                EventKeys.INCOMPLETE_OUTBOX_CALL_SUBJECT);

        assertNotNull(incompleteOutboxCallTrigger);
        assertTrue(hasEventParameterKey(EventKeys.EXTERNAL_ID_KEY, incompleteOutboxCallTrigger.getEventParameters()));
    }

    private void assertCompletedOutboxCallTrigger(List<TriggerEvent> triggerTaskEvents) {
        TriggerEvent completeOutboxCallTrigger = findTriggerEventBySubject(triggerTaskEvents,
                EventKeys.COMPLETED_OUTBOX_CALL_SUBJECT);

        assertNotNull(completeOutboxCallTrigger);
        assertTrue(hasEventParameterKey(EventKeys.EXTERNAL_ID_KEY, completeOutboxCallTrigger.getEventParameters()));
    }

    private void assertMaxPendingMessagesTrigger(List<TriggerEvent> triggerTaskEvents) {
        TriggerEvent maxPendingMessagesTrigger = findTriggerEventBySubject(triggerTaskEvents,
                EventKeys.OUTBOX_MAX_PENDING_MESSAGES_EVENT_SUBJECT);

        assertNotNull(maxPendingMessagesTrigger);
        assertTrue(hasEventParameterKey(EventKeys.EXTERNAL_ID_KEY, maxPendingMessagesTrigger.getEventParameters()));
    }

    private void assertExecuteOutboxAction(List<ActionEvent> actionTaskEvents) {
        ActionEvent executeOutboxAction = findActionEventBySubject(actionTaskEvents, EventKeys.EXECUTE_OUTBOX_SUBJECT);

        assertNotNull(executeOutboxAction);
        assertTrue(hasActionParameterKey(EventKeys.EXTERNAL_ID_KEY, executeOutboxAction.getActionParameters()));
        assertTrue(hasActionParameterKey(EventKeys.PHONE_NUMBER_KEY, executeOutboxAction.getActionParameters()));
        assertTrue(hasActionParameterKey(EventKeys.LANGUAGE_KEY, executeOutboxAction.getActionParameters()));
    }

    private void assertScheduleExecutionAction(List<ActionEvent> actionTaskEvents) {
        ActionEvent scheduleExecutionAction = findActionEventBySubject(actionTaskEvents,
                EventKeys.SCHEDULE_EXECUTION_SUBJECT);

        assertNotNull(scheduleExecutionAction);
        assertTrue(hasActionParameterKey(EventKeys.CALL_HOUR_KEY, scheduleExecutionAction.getActionParameters()));
        assertTrue(hasActionParameterKey(EventKeys.CALL_MINUTE_KEY, scheduleExecutionAction.getActionParameters()));
        assertTrue(hasActionParameterKey(EventKeys.EXTERNAL_ID_KEY, scheduleExecutionAction.getActionParameters()));
        assertTrue(hasActionParameterKey(EventKeys.PHONE_NUMBER_KEY, scheduleExecutionAction.getActionParameters()));
        assertTrue(hasActionParameterKey(EventKeys.LANGUAGE_KEY, scheduleExecutionAction.getActionParameters()));
    }

    private void assertUnscheduleExecutionAction(List<ActionEvent> actionTaskEvents) {
        ActionEvent unscheduleExecutionAction = findActionEventBySubject(actionTaskEvents,
                EventKeys.UNSCHEDULE_EXECUTION_SUBJECT);

        assertNotNull(unscheduleExecutionAction);
        assertTrue(hasActionParameterKey(EventKeys.SCHEDULE_JOB_ID_KEY, unscheduleExecutionAction.getActionParameters()));
    }
}
