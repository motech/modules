package org.motechproject.batch.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.motechproject.batch.exception.BatchException;
import org.motechproject.batch.mds.BatchJob;
import org.motechproject.batch.mds.BatchJobParameters;
import org.motechproject.batch.mds.service.BatchJobMDSService;
import org.motechproject.batch.mds.service.BatchJobParameterMDSService;
import org.motechproject.batch.model.BatchJobDTO;
import org.motechproject.batch.model.BatchJobListDTO;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.batch.core.jsr.launch.JsrJobOperator;

import javax.batch.runtime.BatchRuntime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(BatchRuntime.class)
public class JobTriggerServiceImplTest {

    BatchJobParameterMDSService jobParameterRepo = mock(BatchJobParameterMDSService.class);
    BatchJobMDSService jobRepo = mock(BatchJobMDSService.class);
    JsrJobOperator jobOperator = mock(JsrJobOperator.class);

    @InjectMocks
    JobTriggerServiceImpl jobTriggerServiceImpl = new JobTriggerServiceImpl(
            jobRepo, jobParameterRepo, jobOperator);

    List<BatchJob> listBatchJobDTO;
    BatchJobDTO batchJobDTO;
    BatchJob batchJob;
    BatchJobParameters batchJobParameters = new BatchJobParameters();
    List<BatchJobParameters> parameters = new ArrayList<>();
    BatchJobListDTO batchJobListDTO;
    String cronExpression;
    String jobName = "Test Case";
    long id = 4L;

    @Before
    public void setUp() throws BatchException {

        cronExpression = "0 15 10 * * ? 2014";
        batchJob = new BatchJob();
        batchJob.setCronExpression(cronExpression);
        batchJob.setJobName("testJob");
        batchJob.setId(id);

        batchJobParameters.setBatchJobId(4);
        batchJobParameters.setParameterName("Test Case");
        batchJobParameters.setParameterValue("hcds");
        parameters.add(batchJobParameters);

        batchJobDTO = new BatchJobDTO();
        batchJobListDTO = new BatchJobListDTO();
        listBatchJobDTO = new ArrayList<>();
        listBatchJobDTO.add(batchJob);

    }

    @Test
    public void triggerJob_success() throws BatchException {
        jobName = "logAnalysis";
        PowerMockito.mockStatic(BatchRuntime.class);
        PowerMockito.when(BatchRuntime.getJobOperator())
                .thenReturn(jobOperator);

        when(jobRepo.findByJobName(jobName)).thenReturn(batchJob);
        when(jobParameterRepo.findByJobId((Integer) anyObject()))
                .thenReturn(parameters);
        jobTriggerServiceImpl.triggerJob(jobName);
        verify(jobOperator, times(1)).start((String) any(), (Properties) any());

    }

    /**
     * Invalid scenario
     *
     * @throws BatchException
     */
    @Test
    public void getJObExecutionHistory_catch_batch_exception()
            throws BatchException {
        try {
            jobTriggerServiceImpl.getJobExecutionHistory(jobName);
        } catch (BatchException e) {
            assertEquals("Job not found", e.getErrorMessage());
            assertEquals(1002, e.getErrorCode());
        }
    }
}
