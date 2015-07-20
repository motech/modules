package org.motechproject.batch.mds.service.it;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.batch.mds.BatchJobParameters;
import org.motechproject.batch.mds.service.BatchJobParameterMDSService;
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

@Ignore
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class BatchJobParameterMDSServiceBundleIT extends BasePaxIT {

    @Inject
    private BatchJobParameterMDSService batchJobParameterMDSService;

    @Test
    public void testBatchJobParameters() {
        int batchJobId = 10000;
        List<BatchJobParameters> batchJobsParameters = batchJobParameterMDSService
                .findByJobId(batchJobId);
        assertNotNull(batchJobsParameters);
        assertEquals(0, batchJobsParameters.size());

        BatchJobParameters batchJobParameters = new BatchJobParameters();
        batchJobParameters.setBatchJobId(batchJobId);
        batchJobParameters.setParameterName("param_key");
        batchJobParameters.setParameterValue("param_value");

        batchJobsParameters = batchJobParameterMDSService
                .findByJobId(batchJobId);
        assertNotNull(batchJobsParameters);
        assertEquals(1, batchJobsParameters.size());
    }

    @After
    public void tearDown() {
        batchJobParameterMDSService.deleteAll();
    }
}
