package org.motechproject.mtraining.service.it;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.mtraining.domain.ActivityRecord;
import org.motechproject.mtraining.domain.ActivityState;
import org.motechproject.mtraining.service.ActivityService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import javax.inject.Inject;

import java.util.List;

import static org.junit.Assert.*;


/**
 * Verify that Activity service is present and functional
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class ActivityServiceBundleIT extends BasePaxIT {

    @Inject
    private ActivityService activityService;

    @Test
    public void testActivityServiceInstance() throws Exception {
        assertNotNull(activityService);
    }

    @Test
    public void testActivityCreation() throws Exception {
        ActivityRecord ar = activityService.createActivity(new ActivityRecord("12345", "MyCourse", "MyChapter", "MyLesson",
                DateTime.now(), null, ActivityState.STARTED));

        assertNotNull(ar);
    }

    @Test
    public void testActivityQuizCreation() throws Exception {
        ActivityRecord ar = activityService.createActivity(new ActivityRecord("1222245", "MyCourse", "MyChapter", "MyLesson", "MyQuiz", 89.9,
                DateTime.now(), DateTime.now().plusDays(1), ActivityState.STARTED));

        assertNotNull(ar);
        assertTrue(89.9 == ar.getQuizScore());
    }

    @Test
    public void testAllActivityForUser() throws Exception {
        int previousActivity = activityService.getAllActivityForUser("2222").size();

        activityService.createActivity(new ActivityRecord("2222", "MyCourse", "MyChapter", "MyLesson1", "MyQuiz", 89.9,
                DateTime.now(), null, ActivityState.STARTED));
        activityService.createActivity(new ActivityRecord("2222", "MyCourse", "MyChapter", "MyLesson2", "MyQuiz", 89.9,
                DateTime.now(), null, ActivityState.STARTED));
        activityService.createActivity(new ActivityRecord("2222", "MyCourse", "MyChapter", "MyLesson3", "MyQuiz", 89.9,
                DateTime.now(), null, ActivityState.STARTED));
        activityService.createActivity(new ActivityRecord("2222", "MyCourse", "MyChapter", "MyLesson4", "MyQuiz", 89.9,
                DateTime.now(), null, ActivityState.STARTED));

        List<ActivityRecord> latestActivity = activityService.getAllActivityForUser("2222");
        assertEquals(previousActivity + 4, latestActivity.size());
    }

    @Test
    public void testCompletedActivityForUser() throws Exception {
        int previousCompleted = activityService.getCompletedActivityForUser("3333").size();
        activityService.createActivity(new ActivityRecord("3333", "MyCourse", "MyChapter", "MyLesson1", "MyQuiz", 89.9,
                DateTime.now(), DateTime.now().plusDays(1), ActivityState.COMPLETED));

        List<ActivityRecord> ca = activityService.getCompletedActivityForUser("3333");

        assertEquals(previousCompleted + 1, ca.size());

        for (ActivityRecord current : ca) {
            assertEquals(ActivityState.COMPLETED, current.getState());
        }
    }

    @Test
    public void testUpdateActivity() throws Exception {
        final ActivityRecord original = activityService.createActivity(new ActivityRecord("5555", null, null, null, DateTime.now(), null, ActivityState.STARTED));
        assertEquals(ActivityState.STARTED, original.getState());
        assertNull(original.getCompletionTime());

        activityService.getActivityDataService().doInTransaction(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                ActivityRecord activityRecordToUpdate = activityService.getActivityById(original.getId());
                activityRecordToUpdate.setState(ActivityState.COMPLETED);
                activityRecordToUpdate.setCompletionTime(original.getStartTime().plusDays(1));
                activityService.updateActivity(activityRecordToUpdate);
            }
        });

        ActivityRecord updated = activityService.getActivityById(original.getId());
        assertEquals(ActivityState.COMPLETED, updated.getState());
        assertEquals(original.getStartTime().plusDays(1), updated.getCompletionTime());
    }

    @Test
    public void testDeleteActivity() throws Exception {
        ActivityRecord original = activityService.createActivity(new ActivityRecord("7777", null, null, null, DateTime.now(), null, ActivityState.STARTED));
        assertNotNull(original);
        ActivityRecord lookup = activityService.getActivityById(original.getId());
        assertNotNull(lookup);

        activityService.deleteActivity(lookup.getId());
        assertNull(activityService.getActivityById(lookup.getId()));
    }

    @Test
    public void testDeleteAllActivity() throws Exception {
        activityService.createActivity(new ActivityRecord("7777", null, null, null, DateTime.now(), null, ActivityState.STARTED));
        activityService.createActivity(new ActivityRecord("7777", null, null, null, DateTime.now(), null, ActivityState.STARTED));
        activityService.createActivity(new ActivityRecord("7777", null, null, null, DateTime.now(), null, ActivityState.STARTED));

        assertTrue(activityService.getAllActivityForUser("7777").size() > 1);

        activityService.deleteAllActivityForUser("7777");
        assertTrue(activityService.getAllActivityForUser("7777").size() == 0);
    }
}
