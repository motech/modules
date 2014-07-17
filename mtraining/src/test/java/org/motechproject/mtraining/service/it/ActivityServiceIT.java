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

import javax.inject.Inject;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * Verify that Activity service is present and functional
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class ActivityServiceIT extends BasePaxIT {

    @Inject
    private ActivityService activityService;

    @Test
    public void testActivityServiceInstance() throws Exception {

        assertNotNull(activityService);
    }

    @Test
    public void testActivityCreation() throws Exception {
        ClassLoader old = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            ActivityRecord ar = activityService.createActivity(new ActivityRecord("12345", "MyCourse", "MyChapter", "MyLesson",
                    DateTime.now(), null, ActivityState.STARTED));

            assertNotNull(ar);
        } finally {
            Thread.currentThread().setContextClassLoader(old);
        }
    }

    @Test
    public void testActivityQuizCreation() throws Exception {
        ClassLoader old = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            ActivityRecord ar = activityService.createActivity(new ActivityRecord("1222245", "MyCourse", "MyChapter", "MyLesson", "MyQuiz", 89.9,
                    DateTime.now(), DateTime.now().plusDays(1), ActivityState.STARTED));

            assertNotNull(ar);
            assertTrue(89.9 == ar.getQuizScore());
        } finally {
            Thread.currentThread().setContextClassLoader(old);
        }
    }

    @Test
    public void testAllActivityForUser() throws Exception {
        ClassLoader old = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());

            int previousActivity = activityService.getActivityForUser("2222").size();

            activityService.createActivity(new ActivityRecord("2222", "MyCourse", "MyChapter", "MyLesson1", "MyQuiz", 89.9,
                    DateTime.now(), null, ActivityState.STARTED));
            activityService.createActivity(new ActivityRecord("2222", "MyCourse", "MyChapter", "MyLesson2", "MyQuiz", 89.9,
                    DateTime.now(), null, ActivityState.STARTED));
            activityService.createActivity(new ActivityRecord("2222", "MyCourse", "MyChapter", "MyLesson3", "MyQuiz", 89.9,
                    DateTime.now(), null, ActivityState.STARTED));
            activityService.createActivity(new ActivityRecord("2222", "MyCourse", "MyChapter", "MyLesson4", "MyQuiz", 89.9,
                    DateTime.now(), null, ActivityState.STARTED));

            List<ActivityRecord> latestActivity = activityService.getActivityForUser("2222");
            assertEquals(previousActivity + 4, latestActivity.size());
        } finally {
            Thread.currentThread().setContextClassLoader(old);
        }
    }

    @Test
    public void testCompletedActivityForUser() throws Exception {
        ClassLoader old = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            int previousCompleted = activityService.getCompletedActivityForUser("3333").size();
            activityService.createActivity(new ActivityRecord("3333", "MyCourse", "MyChapter", "MyLesson1", "MyQuiz", 89.9,
                    DateTime.now(), DateTime.now().plusDays(1), ActivityState.COMPLETED));

            List<ActivityRecord> ca = activityService.getCompletedActivityForUser("3333");

            assertEquals(previousCompleted + 1, ca.size());
        } finally {
            Thread.currentThread().setContextClassLoader(old);
        }
    }
}
