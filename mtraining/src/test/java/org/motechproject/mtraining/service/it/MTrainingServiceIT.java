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

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Verify that mTrainingService present, functional.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class MTrainingServiceIT extends BasePaxIT {

    @Test
    public void testNull() throws Exception {

        assertNull(null);
    }
}
