package org.motechproject.batch.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.batch.exception.ApplicationErrors;
import org.motechproject.batch.exception.BatchException;
import org.motechproject.batch.mds.BatchJob;
import org.motechproject.batch.mds.BatchJobParameters;
import org.motechproject.batch.mds.service.BatchJobMDSService;
import org.motechproject.batch.mds.service.BatchJobParameterMDSService;
import org.motechproject.batch.model.BatchJobDTO;
import org.motechproject.batch.model.BatchJobListDTO;
import org.motechproject.batch.model.CronJobScheduleParam;
import org.motechproject.batch.model.OneTimeJobScheduleParams;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.springframework.http.HttpStatus;

@RunWith(MockitoJUnitRunner.class)
public class JobServiceImplTest {

    @Mock
    BatchJobMDSService jobRepo;
    @Mock
    BatchJobParameterMDSService jobParameterRepo;
    @Mock
    MotechSchedulerService motechSchedulerService;

    @InjectMocks
    JobServiceImpl serviceImpl = new JobServiceImpl(jobRepo, jobParameterRepo,
            motechSchedulerService);

    List<BatchJob> listBatchJobDTO;
    BatchJobDTO batchJobDTO;
    BatchJob batchJob;
    List<BatchJobDTO> jobDtoList;
    BatchJobListDTO batchJobListDTO;
    String cronExpression;
    String date;
    long id = 4L;
    BatchJobListDTO listDto = new BatchJobListDTO();
    String jobName = "Test Case";

    @Before
    public void setUp() throws BatchException {
        date = "10/09/2014 10:20:16";
        cronExpression = "0 15 10 * * ? 2014";
        batchJob = new BatchJob();
        batchJob.setCronExpression(cronExpression);
        batchJob.setJobName("testJob");
        List<BatchJob> batchJobs = new ArrayList<BatchJob>();
        batchJobs.add(batchJob);

        batchJobDTO = new BatchJobDTO();
        batchJobListDTO = new BatchJobListDTO();
        listBatchJobDTO = new ArrayList<>();
        jobDtoList = new ArrayList<BatchJobDTO>();

        listBatchJobDTO.add(batchJob);
        batchJobListDTO.setBatchJobDtoList(jobDtoList);

        when(jobRepo.findByJobName(jobName)).thenReturn(batchJobs);
        when(jobRepo.retrieveAll()).thenReturn(batchJobs);
        when(
                jobRepo.getDetachedField((BatchJob) anyObject(),
                        (String) anyObject())).thenReturn(id);

    }

    /**
     * valid scenario
     *
     * @throws BatchException
     */
    @Test
    public void getListOfJobs_success() throws BatchException {
        when(
                jobRepo.getDetachedField((BatchJob) anyObject(),
                        (String) anyObject())).thenReturn(id);
        BatchJobListDTO batchJobListDTO = serviceImpl.getListOfJobs();
        assertNotNull(batchJobListDTO);
        assertEquals(1, batchJobListDTO.getBatchJobDtoList().size());
        assertEquals("testJob", batchJobListDTO.getBatchJobDtoList().get(0)
                .getJobName());
        assertEquals("0 15 10 * * ? 2014", batchJobListDTO.getBatchJobDtoList()
                .get(0).getCronExpression());
    }

    @Test
    public void scheduleJob_success() throws BatchException {
        when(jobRepo.findByJobName(jobName)).thenReturn(
                new ArrayList<BatchJob>());
        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("key_job", "value_job");
        CronJobScheduleParam params = new CronJobScheduleParam();
        params.setCronExpression(cronExpression);
        params.setJobName(jobName);
        params.setParamsMap(hm);

        serviceImpl.scheduleJob(params);

        verify(jobRepo).create((BatchJob) any());
        verify(jobParameterRepo).create((BatchJobParameters) any());
        verify(jobRepo).findByJobName(jobName);
    }

    @Test
    public void scheduleJob_duplicate_job() throws BatchException {
        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("key_job", "value_job");
        CronJobScheduleParam params = new CronJobScheduleParam();
        params.setCronExpression(cronExpression);
        params.setJobName(jobName);
        params.setParamsMap(hm);
        try {
            serviceImpl.scheduleJob(params);
        } catch (BatchException be) {
            ApplicationErrors error = (ApplicationErrors) be.getError();
            assertEquals("Duplicate Job", error.getMessage());
            assertEquals(1003, error.getCode());
            assertEquals(HttpStatus.BAD_REQUEST, error.getHttpStatus());
        }
        verify(jobRepo, times(0)).create((BatchJob) any());
        verify(jobParameterRepo, times(0)).create((BatchJobParameters) any());
        verify(jobRepo).findByJobName(jobName);
    }

    @Test
    public void scheduleOneTimeJob_success() throws BatchException {
        when(jobRepo.findByJobName(jobName)).thenReturn(
                new ArrayList<BatchJob>());
        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("job_key", "job_value");
        OneTimeJobScheduleParams params = new OneTimeJobScheduleParams();
        params.setJobName(jobName);
        params.setParamsMap(hm);
        params.setDate(date);

        serviceImpl.scheduleOneTimeJob(params);
        verify(jobRepo).findByJobName(jobName);
        verify(jobRepo).create((BatchJob) any());
        verify(jobParameterRepo).create((BatchJobParameters) any());
    }

    @Test
    public void scheduleOneTimeJob_duplicate_job() {
        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("job_key", "job_value");
        OneTimeJobScheduleParams params = new OneTimeJobScheduleParams();
        params.setJobName(jobName);
        params.setParamsMap(hm);
        params.setDate(date);

        try {
            serviceImpl.scheduleOneTimeJob(params);
        } catch (BatchException be) {
            ApplicationErrors error = (ApplicationErrors) be.getError();
            assertEquals("Duplicate Job", error.getMessage());
            assertEquals(1003, error.getCode());
            assertEquals(HttpStatus.BAD_REQUEST, error.getHttpStatus());
        }
        verify(jobRepo, times(0)).create((BatchJob) any());
        verify(jobParameterRepo, times(0)).create((BatchJobParameters) any());
        verify(jobRepo).findByJobName(jobName);
    }

    /**
     * valid scenario(when parameterList from db matches the list sent for
     * update)
     *
     * @throws BatchException
     */
    @Test
    public void updateJobProperty_success_params_match() throws BatchException {
        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("batch_key", "batch_value");
        List<BatchJobParameters> batchJobParametersList = new ArrayList<BatchJobParameters>();
        BatchJobParameters batchJobParam = new BatchJobParameters();
        batchJobParam.setBatchJobId(2);
        batchJobParam.setParameterName("batch_key");
        batchJobParam.setParameterValue("batch_value");
        batchJobParametersList.add(batchJobParam);
        when(jobParameterRepo.findByJobId(2))
                .thenReturn(batchJobParametersList);

        serviceImpl.updateJobProperty(jobName, hm);

        verify(jobParameterRepo).findByJobId((Integer) any());
        verify(jobParameterRepo, times(1)).findByJobId((Integer) any());
        verify(jobParameterRepo).create((BatchJobParameters) any());

    }

    /**
     * valid scenario(when parameterList from db does not match the list sent
     * for update)
     *
     * @throws BatchException
     */
    @Test
    public void updateJobProperty_success_params_mismatch()
            throws BatchException {
        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("batch_key", "batch_value");
        List<BatchJobParameters> batchJobParametersList = new ArrayList<BatchJobParameters>();
        BatchJobParameters batchJobParam = new BatchJobParameters();
        batchJobParam.setBatchJobId(2);
        batchJobParam.setParameterName("batch_key_db");
        batchJobParam.setParameterValue("batch_value_db");
        batchJobParametersList.add(batchJobParam);
        when(jobParameterRepo.findByJobId(4))
                .thenReturn(batchJobParametersList);

        serviceImpl.updateJobProperty(jobName, hm);

        verify(jobParameterRepo).findByJobId((Integer) any());
        verify(jobParameterRepo, times(1)).findByJobId((Integer) any());
        verify(jobParameterRepo).create((BatchJobParameters) any());
    }
}
