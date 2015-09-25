package org.motechproject.batch.mds.service.it;

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
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class BatchJobMDSServiceBundleIT extends BasePaxIT {

    @Inject
    private BatchJobMDSService batchJobMDSService;

    @Test
    public void testBatchJob() {
        final String jobName = "randomJob";
        BatchJob batchJob = batchJobMDSService.findByJobName(jobName);
        assertNull(batchJob);

        batchJob = new BatchJob();
        batchJob.setJobName(jobName);
        batchJob.setCronExpression("0 15 10 * * ? 2020");
        batchJob.setBatchJobStatusId(1);
        Byte[] jobContent = ArrayUtils.toObject("job content".getBytes());
        batchJob.setJobContent(jobContent);
        batchJobMDSService.create(batchJob);

        batchJobMDSService.doInTransaction(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                BatchJob batchJobFromDatabase = batchJobMDSService.findByJobName(jobName);
                assertNotNull(batchJobFromDatabase);

                String cron = batchJobFromDatabase.getCronExpression();
                Assert.assertEquals("0 15 10 * * ? 2020", cron);

                Byte[] jobContentFromDatabase = (Byte[]) batchJobMDSService.getDetachedField(batchJobFromDatabase,
                        "jobContent");
                Assert.assertEquals("job content",
                        new String(ArrayUtils.toPrimitive(jobContentFromDatabase)));
            }
        });

        batchJobMDSService.delete(batchJob);

        batchJob = batchJobMDSService.findByJobName(jobName);
        assertNull(batchJob);
    }

    @After
    public void tearDown() {
        batchJobMDSService.deleteAll();
    }
}
