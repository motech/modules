package org.motechproject.batch.mds.service.it;

import java.util.List;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Assert;
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

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class BatchJobParameterMDSServiceIT extends BasePaxIT {

	@Inject
	private BatchJobParameterMDSService batchJobParameterMDSService;
	private int batchJobId = 10000;

	@Test
	public void testBatchJobParameters() {

		List<BatchJobParameters> batchJobsParameters = batchJobParameterMDSService
				.findByJobId(batchJobId);
		Assert.assertNotNull(batchJobsParameters);
		Assert.assertEquals(0, batchJobsParameters.size());

		BatchJobParameters batchJobParameters = new BatchJobParameters();
		batchJobParameters.setBatchJobId(batchJobId);
		batchJobParameters.setParameterName("param_key");
		batchJobParameters.setParameterValue("param_value");

		batchJobsParameters = batchJobParameterMDSService
				.findByJobId(batchJobId);
		Assert.assertNotNull(batchJobsParameters);
		Assert.assertEquals(1, batchJobsParameters.size());

	}

	@After
	public void tearDown() {
		batchJobParameterMDSService.deleteAll();
	}

}
