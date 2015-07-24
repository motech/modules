package org.motechproject.batch.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.batch.exception.BatchErrors;
import org.motechproject.batch.exception.BatchException;
import org.motechproject.batch.exception.RestException;
import org.motechproject.batch.model.BatchJobDTO;
import org.motechproject.batch.model.BatchJobListDTO;
import org.motechproject.batch.model.JobExecutionHistoryListDTO;
import org.motechproject.batch.service.JobService;
import org.motechproject.batch.service.JobTriggerService;
import org.motechproject.batch.validation.BatchValidator;
import org.springframework.http.HttpStatus;

import javax.batch.runtime.JobExecution;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BatchControllerTest {

    @Mock
    JobService jobService;
    @Mock
    JobTriggerService jobTriggerService;
    @Mock
    BatchValidator batchValidator;
    @Mock
    JobExecution jobExecution;

    @InjectMocks
    BatchController controller = new BatchController();

    private String jobName;
    private List<String> errors;

    @Before
    public void setUp() throws BatchException {

        jobName = "testJob";
        errors = new ArrayList<>();

        BatchJobDTO batchJobDTO = new BatchJobDTO();
        BatchJobListDTO batchJobListDTO = new BatchJobListDTO();
        List<BatchJobDTO> listBatchJobDTO = new ArrayList<>();
        batchJobDTO.setCronExpression("0 15 10 * * ? 2014");
        batchJobDTO.setJobId(2);
        batchJobDTO.setJobName("testJob");
        listBatchJobDTO.add(batchJobDTO);
        batchJobListDTO.setBatchJobDtoList(listBatchJobDTO);

        JobExecutionHistoryListDTO jobExecutionHistoryList = new JobExecutionHistoryListDTO();
        List<JobExecution> paramsList = new ArrayList<>();
        paramsList.add(jobExecution);
        jobExecutionHistoryList.setJobExecutionHistoryList(paramsList);

        when(jobService.getListOfJobs()).thenReturn(batchJobListDTO);
        when(jobTriggerService.getJobExecutionHistory(jobName)).thenReturn(
                jobExecutionHistoryList);
        when(batchValidator.validateUpdateInputs(jobName)).thenReturn(errors);
    }

    /**
     * valid input scenario
     */
    @Test
    public void getJobListReturnsValidResponse() {
        BatchJobListDTO batchJobListDTO = controller.getJobList();
        assertNotNull(batchJobListDTO);
        assertEquals(1, batchJobListDTO.getBatchJobDtoList().size());
        assertEquals("testJob", batchJobListDTO.getBatchJobDtoList().get(0)
                .getJobName());
    }

    @Test
    public void getJobHistoryListReturnsValidResponse() throws Exception {

        JobExecutionHistoryListDTO jobExecutionHistoryList = controller.getjobHistoryList(jobName);
        assertNotNull(jobExecutionHistoryList);
        assertEquals(1, jobExecutionHistoryList.getJobExecutionHistoryList().size());

    }

    /**
     * Invalid scenario: mandatory field <code>jobName</code> empty
     */
    @Test
    public void getJobHistoryListEmptyJobName() {
        jobName = "";
        errors.add("Job name must be provided");
        when(batchValidator.validateUpdateInputs(jobName)).thenReturn(errors);
        try {
            controller.getjobHistoryList(jobName);
        } catch (RestException e) {
            BatchErrors be = e.getBatchException().getError();
            assertEquals(1001, be.getCode());
            assertEquals(HttpStatus.BAD_REQUEST, be.getHttpStatus());
            assertEquals("One or more input parameter(s) may be wrong", be.getMessage());
        }
    }

    /**
     * Invalid scenario: mandatory field <code>jobName</code> null
     */
    @Test
    public void getJobHistoryListNullJobName() {
        when(batchValidator.validateUpdateInputs(null)).thenReturn(errors);
        errors.add("Job name must be provided");

        try {
            controller.getjobHistoryList(jobName);
        } catch (RestException e) {
            BatchErrors be = e.getBatchException().getError();
            assertEquals(1001, be.getCode());
            assertEquals(HttpStatus.BAD_REQUEST, be.getHttpStatus());
            assertEquals("One or more input parameter(s) may be wrong", be.getMessage());
        }
    }

    /**
     * valid input scenario
     */
    @Test
    public void triggerJobReturnsValidResponse() {
        errors.clear();
        when(batchValidator.validateUpdateInputs(jobName)).thenReturn(errors);
        controller.triggerJob(jobName);
    }

    /**
     * Invalid scenario: mandatory field <code>jobName</code> empty
     */
    @Test
    public void triggerJobWithEmptyJobName() {
        jobName = "";
        errors.clear();
        errors.add("Job name must be provided");
        when(batchValidator.validateUpdateInputs(jobName)).thenReturn(errors);
        try {
            controller.triggerJob(jobName);
        } catch (RestException e) {
            BatchErrors be = e.getBatchException().getError();
            assertEquals(1001, be.getCode());
            assertEquals(HttpStatus.BAD_REQUEST, be.getHttpStatus());
            assertEquals("One or more input parameter(s) may be wrong",
                    be.getMessage());
        }
    }

    /**
     * Invalid scenario: mandatory field <code>jobName</code> null
     */
    @Test
    public void triggerJobWithNullJobName() {
        jobName = null;
        errors.clear();
        errors.add("Job name must be provided");
        when(batchValidator.validateUpdateInputs(jobName)).thenReturn(errors);
        try {
            controller.triggerJob(jobName);
        } catch (RestException e) {
            BatchErrors be = e.getBatchException().getError();
            assertEquals(1001, be.getCode());
            assertEquals(HttpStatus.BAD_REQUEST, be.getHttpStatus());
            assertEquals("One or more input parameter(s) may be wrong",
                    be.getMessage());
        }
    }
}
