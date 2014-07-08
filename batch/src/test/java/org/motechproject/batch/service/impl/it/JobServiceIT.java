package org.motechproject.batch.service.impl.it;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.batch.exception.BatchException;
import org.motechproject.batch.mds.BatchJob;
import org.motechproject.batch.mds.service.BatchJobMDSService;
import org.motechproject.batch.model.BatchJobListDTO;
import org.motechproject.batch.model.CronJobScheduleParam;
import org.motechproject.batch.service.JobService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class JobServiceIT extends BasePaxIT {
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
		BatchJobListDTO batchJobs = jobService.getListOfJobs();
		List<BatchJob> batchJobsMds =  batchJobMDSService.findByJobName("random-test-job");
		
		Assert.assertNotNull(batchJobs);
		Assert.assertEquals(1,batchJobsMds.size());
		Assert.assertEquals("random-test-job",batchJobsMds.get(0).getJobName());
		Assert.assertEquals("0 15 10 * * ? 2017",batchJobsMds.get(0).getCronExpression());
		Assert.assertEquals((Integer)1,batchJobsMds.get(0).getBatchJobStatusId());
	}
	
	@Test
	public void scheduleJobTest() throws BatchException {
		CronJobScheduleParam params = new CronJobScheduleParam();
		Map<String,String> paramsMap = new HashMap<>();
		paramsMap.put("param_key1","param_value1");
		paramsMap.put("param_key2","param_value2");
		
		params.setCronExpression("0 15 10 * * ? 2017");
		params.setJobName("random-test-job");
		params.setParamsMap(paramsMap);
		
		jobService.scheduleJob(params);
		
		List<BatchJob> batchJobsMds =  batchJobMDSService.findByJobName("random-test-job");
		Assert.assertEquals(1,batchJobsMds.size());
		Assert.assertEquals("random-test-job",batchJobsMds.get(0).getJobName());
		Assert.assertEquals("0 15 10 * * ? 2017",batchJobsMds.get(0).getCronExpression());
	}
	
	@Test
	public void updateJobPropertyTest() {
		
	}

	
}
