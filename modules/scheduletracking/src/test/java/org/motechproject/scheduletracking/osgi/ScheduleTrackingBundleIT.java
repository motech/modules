package org.motechproject.scheduletracking.osgi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.scheduletracking.domain.Schedule;
import org.motechproject.scheduletracking.service.ScheduleTrackingService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

import javax.inject.Inject;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class ScheduleTrackingBundleIT extends BasePaxIT {

    @Inject
    private ScheduleTrackingService scheduleTrackingService;

    @Test
    public void testScheduleTrackingService() {
        final String scheduleName = "ScheduleTrackingApiBundleIT-" + UUID.randomUUID();
        try {
            scheduleTrackingService.add("{name: " + scheduleName + "}");
            Schedule schedule = scheduleTrackingService.getScheduleByName(scheduleName);
            assertNotNull(schedule);
            assertEquals(scheduleName, schedule.getName());
        } finally {
            scheduleTrackingService.remove(scheduleName);
        }

    }
}
