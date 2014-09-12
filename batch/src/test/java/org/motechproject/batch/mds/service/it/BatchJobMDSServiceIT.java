package org.motechproject.batch.mds.service.it;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.ArrayUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.batch.mds.BatchJob;
import org.motechproject.batch.mds.service.BatchJobMDSService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class BatchJobMDSServiceIT extends BasePaxIT {

    @Inject
    private BatchJobMDSService batchJobMDSService;

    private String jobName = "randomJob";

    @Test
    public void testBatchJob() {
        List<BatchJob> batchJobs = batchJobMDSService.findByJobName(jobName);
        Assert.assertNotNull(batchJobs);
        Assert.assertEquals(0, batchJobs.size());

        BatchJob batchJob = new BatchJob();
        batchJob.setJobName(jobName);
        batchJob.setCronExpression("0 15 10 * * ? 2020");
        batchJob.setBatchJobStatusId(1);
        Byte[] jobContent = ArrayUtils.toObject("job content".getBytes());
        batchJob.setJobContent(jobContent);
        batchJobMDSService.create(batchJob);

        batchJobs = batchJobMDSService.findByJobName(jobName);
        Assert.assertNotNull(batchJobs);
        Assert.assertEquals(1, batchJobs.size());
        
        batchJob = batchJobs.get(0);
        String cron = batchJob.getCronExpression();
        Assert.assertEquals("0 15 10 * * ? 2020", cron);
        jobContent = (Byte[]) batchJobMDSService.getDetachedField(batchJob, "jobContent");
        Assert.assertEquals("job content", new String(ArrayUtils.toPrimitive(jobContent)));
        
        batchJobMDSService.delete(batchJob);

        batchJobs = batchJobMDSService.findByJobName(jobName);
        Assert.assertNotNull(batchJobs);
        Assert.assertEquals(0, batchJobs.size());
        
    }

    @After
    public void tearDown() {
        batchJobMDSService.deleteAll();
    }
}
