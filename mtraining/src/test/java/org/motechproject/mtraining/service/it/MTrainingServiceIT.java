package org.motechproject.mtraining.service.it;

import org.junit.Ignore;
import org.motechproject.mtraining.domain.Course;
import org.motechproject.mtraining.service.MTrainingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;

import org.motechproject.testing.osgi.BasePaxIT;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Verify that mTrainingService present, functional.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class MTrainingServiceIT extends BasePaxIT {

    @Inject
    private MTrainingService mTrainingService;

    @Test
    public void testNull() throws Exception {

        assertNotNull(mTrainingService);
    }

    @Test
    public void testCourseCreation() throws Exception {
        Course course = mTrainingService.createCourse(new Course("MyCourse", true, "FooBar.com/ivr1"));
        assertNotNull(course);

        List<Course> saved = mTrainingService.getCourseByName("MyCourse");
        assertTrue(saved.size() > 0);
    }
}
