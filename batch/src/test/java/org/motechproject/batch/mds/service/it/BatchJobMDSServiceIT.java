package org.motechproject.batch.mds.service.it;

import java.util.List;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Assert;
import org.junit.Ignore;
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

@Ignore
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
		batchJobMDSService.create(batchJob);

		batchJobs = batchJobMDSService.findByJobName(jobName);
		Assert.assertNotNull(batchJobs);
		Assert.assertEquals(1, batchJobs.size());

	}

	@After
	public void tearDown() {
		batchJobMDSService.deleteAll();
	}

}
