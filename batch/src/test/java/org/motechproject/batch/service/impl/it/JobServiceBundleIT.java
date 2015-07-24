package org.motechproject.batch.service.impl.it;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.batch.exception.BatchException;
import org.motechproject.batch.mds.BatchJob;
import org.motechproject.batch.mds.service.BatchJobMDSService;
import org.motechproject.batch.model.CronJobScheduleParam;
import org.motechproject.batch.service.JobService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class JobServiceBundleIT extends BasePaxIT {

    @Inject
    private BatchJobMDSService batchJobMDSService;
    @Inject
    private JobService jobService;

    @Test
    public void getListOfJobTest() throws BatchException {

        BatchJob batchJob = new BatchJob();
        batchJob.setJobName("random-test-job");
        batchJob.setCronExpression("0 15 10 * * ? 2017");
        batchJob.setBatchJobStatusId(1);

        batchJobMDSService.create(batchJob);
        BatchJob batchJobMds = batchJobMDSService.findByJobName("random-test-job");

        assertNotNull(batchJobMds);
        assertEquals("random-test-job", batchJobMds.getJobName());
        assertEquals("0 15 10 * * ? 2017", batchJobMds.getCronExpression());
        assertEquals((Integer) 1, batchJobMds.getBatchJobStatusId());
    }

    @Test
    public void scheduleJobTest() throws BatchException {
        CronJobScheduleParam params = new CronJobScheduleParam();
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("param_key1", "param_value1");
        paramsMap.put("param_key2", "param_value2");

        params.setCronExpression("0 15 10 * * ? 2017");
        params.setJobName("random-test-job");
        params.setParamsMap(paramsMap);

        jobService.scheduleJob(params);

        BatchJob batchJobMds = batchJobMDSService.findByJobName("random-test-job");
        assertNotNull(batchJobMds);
        assertEquals("random-test-job", batchJobMds.getJobName());
        assertEquals("0 15 10 * * ? 2017", batchJobMds.getCronExpression());

        batchJobMDSService.delete(batchJobMds);

        batchJobMds = batchJobMDSService.findByJobName("random-test-job");
        assertNull(batchJobMds);
    }
}
