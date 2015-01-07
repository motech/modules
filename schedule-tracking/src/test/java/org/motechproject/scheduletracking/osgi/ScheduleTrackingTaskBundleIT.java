package org.motechproject.scheduletracking.osgi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.motechproject.scheduletracking.events.constants.EventDataKeys;
import org.motechproject.scheduletracking.events.constants.EventSubjects;
import org.motechproject.tasks.domain.ActionEvent;
import org.motechproject.tasks.domain.Channel;
import org.motechproject.tasks.domain.TriggerEvent;
import org.motechproject.tasks.osgi.test.AbstractTaskBundleIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class ScheduleTrackingTaskBundleIT extends AbstractTaskBundleIT {

    private static final String CHANNEL_NAME = "org.motechproject.schedule-tracking";

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

        assertMileStoneAlertTrigger(channel.getTriggerTaskEvents());
        assertMilestoneDefaultedTrigger(channel.getTriggerTaskEvents());
    }

    private void assertMileStoneAlertTrigger(List<TriggerEvent> triggerTaskEvents) {
        TriggerEvent milestoneAlertTrigger = findTriggerEventBySubject(triggerTaskEvents, EventSubjects.MILESTONE_ALERT);

        assertNotNull(milestoneAlertTrigger);
        assertTrue(hasEventParameterKey(EventDataKeys.WINDOW_NAME, milestoneAlertTrigger.getEventParameters()));
        assertTrue(hasEventParameterKey(EventDataKeys.MILESTONE_NAME, milestoneAlertTrigger.getEventParameters()));
        assertTrue(hasEventParameterKey(EventDataKeys.EARLIEST_DATE_TIME, milestoneAlertTrigger.getEventParameters()));
        assertTrue(hasEventParameterKey(EventDataKeys.DUE_DATE_TIME, milestoneAlertTrigger.getEventParameters()));
        assertTrue(hasEventParameterKey(EventDataKeys.LATE_DATE_TIME, milestoneAlertTrigger.getEventParameters()));
        assertTrue(hasEventParameterKey(EventDataKeys.DEFAULTMENT_DATE_TIME, milestoneAlertTrigger.getEventParameters()));
        assertTrue(hasEventParameterKey(EventDataKeys.SCHEDULE_NAME, milestoneAlertTrigger.getEventParameters()));
        assertTrue(hasEventParameterKey(EventDataKeys.EXTERNAL_ID, milestoneAlertTrigger.getEventParameters()));
        assertTrue(hasEventParameterKey(EventDataKeys.REFERENCE_DATE, milestoneAlertTrigger.getEventParameters()));
        assertTrue(hasEventParameterKey(EventDataKeys.MILESTONE_DATA, milestoneAlertTrigger.getEventParameters()));
    }

    private void assertMilestoneDefaultedTrigger(List<TriggerEvent> triggerTaskEvents) {
        TriggerEvent milestoneDefaultedTrigger = findTriggerEventBySubject(triggerTaskEvents,
                EventSubjects.MILESTONE_DEFAULTED);

        assertNotNull(milestoneDefaultedTrigger);
        assertTrue(hasEventParameterKey(EventDataKeys.ENROLLMENT_ID, milestoneDefaultedTrigger.getEventParameters()));
        assertTrue(hasEventParameterKey(EventDataKeys.EXTERNAL_ID, milestoneDefaultedTrigger.getEventParameters()));
        assertTrue(hasEventParameterKey(MotechSchedulerService.JOB_ID_KEY,
                milestoneDefaultedTrigger.getEventParameters()));
    }

    public void testTaskActions() throws IOException {
        Channel channel = findChannel(CHANNEL_NAME);

        assertDefaultCaptureAction(channel.getActionTaskEvents());
    }

    private void assertDefaultCaptureAction(List<ActionEvent> actionTaskEvents) {
        ActionEvent milestoneDefaultedAction = findActionEventBySubject(actionTaskEvents, EventSubjects.MILESTONE_DEFAULTED);

        assertNotNull(milestoneDefaultedAction);
        assertTrue(hasActionParameterKey(EventDataKeys.ENROLLMENT_ID, milestoneDefaultedAction.getActionParameters()));
        assertTrue(hasActionParameterKey(EventDataKeys.EXTERNAL_ID, milestoneDefaultedAction.getActionParameters()));
        assertTrue(hasActionParameterKey(MotechSchedulerService.JOB_ID_KEY,
                milestoneDefaultedAction.getActionParameters()));
    }

}
