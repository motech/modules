package org.motechproject.mtraining.service.it;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.mtraining.domain.EnrollmentRecord;
import org.motechproject.mtraining.service.ActivityRecordService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

import javax.inject.Inject;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Verify that HelloWorldRecordService present, functional.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class ActivityRecordServiceIT extends BasePaxIT {

    @Inject
    private ActivityRecordService activityRecordService;

    @Test
    @Ignore
    public void testEnrollmentRecord() throws Exception {
        EnrollmentRecord testRecord = new EnrollmentRecord("testName", "test message");
        activityRecordService.add(testRecord);

        EnrollmentRecord record = activityRecordService.findRecordByName(testRecord.getName());
        assertEquals(testRecord, record);

        List<EnrollmentRecord> records = activityRecordService.getRecords();
        assertTrue(records.contains(testRecord));

        activityRecordService.delete(testRecord);
        record = activityRecordService.findRecordByName(testRecord.getName());
        assertNull(record);
    }
}
