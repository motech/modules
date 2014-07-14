package org.motechproject.batch.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.batch.operations.JobOperator;
import javax.batch.operations.JobSecurityException;
import javax.batch.operations.JobStartException;
import javax.batch.operations.NoSuchJobException;
import javax.batch.operations.NoSuchJobExecutionException;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.JobInstance;

import org.apache.log4j.Logger;
import org.motechproject.batch.exception.ApplicationErrors;
import org.motechproject.batch.exception.BatchException;
import org.motechproject.batch.mds.BatchJob;
import org.motechproject.batch.mds.BatchJobParameters;
import org.motechproject.batch.mds.service.BatchJobMDSService;
import org.motechproject.batch.mds.service.BatchJobParameterMDSService;
import org.motechproject.batch.model.JobExecutionHistoryListDTO;
import org.motechproject.batch.service.JobTriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Class to perform the trigger operation for all types of jobs
 * 
 * @author Naveen
 * 
 */
@Service(value = "jobTriggerService")
public class JobTriggerServiceImpl implements JobTriggerService {

    private static final Logger LOGGER = Logger
            .getLogger(JobTriggerServiceImpl.class);

    private JobOperator jsrJobOperator;

    @Value("${xml.path}")
    private String xmlPath;

    @Autowired
    public JobTriggerServiceImpl(BatchJobMDSService jobRepo,
            BatchJobParameterMDSService jobParameterRepo,
            JobOperator jsrJobOperator) {
        this.jobRepo = jobRepo;
        this.jobParameterRepo = jobParameterRepo;
        this.jsrJobOperator = jsrJobOperator;

    }

    private BatchJobParameterMDSService jobParameterRepo;

    private BatchJobMDSService jobRepo;

    private Properties getJobParameters(String jobName) throws BatchException {
        List<BatchJob> batchJobList = jobRepo.findByJobName(jobName);
        boolean jobExists = true;
        if (batchJobList == null || batchJobList.size() == 0) {
            jobExists = false;
        }
        if (!jobExists) {
            throw new BatchException(ApplicationErrors.JOB_NOT_FOUND);
        }
        BatchJob batchJob = batchJobList.get(0);
        long batchJobId = (long) jobRepo.getDetachedField(batchJob, "id");

        List<BatchJobParameters> parametersList = jobParameterRepo
                .findByJobId((int) batchJobId);

        Properties jobParameters = new Properties();

        for (BatchJobParameters batchJobParameter : parametersList) {
            jobParameters.put(batchJobParameter.getParameterName(),
                    batchJobParameter.getParameterValue());
        }
        return jobParameters;
    }

    @Override
    public long triggerJob(String jobName) throws BatchException {
        LOGGER.info("Starting executing JOB: " + jobName);
        ClassLoader contextClassLoader = Thread.currentThread()
                .getContextClassLoader();

        BatchJobClassLoader testLoader = new BatchJobClassLoader(
                contextClassLoader, xmlPath);

        Thread.currentThread().setContextClassLoader(testLoader);
        Properties jobParameters = getJobParameters(jobName);

        try {
            return jsrJobOperator.start(jobName, jobParameters);
        } catch (JobStartException | JobSecurityException e) {

            throw new BatchException(ApplicationErrors.JOB_TRIGGER_FAILED, e,
                    e.getMessage());
        } finally {

            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }
    }

    @Override
    public JobExecutionHistoryListDTO getJObExecutionHistory(String jobName)
            throws BatchException {
        List<JobExecution> jobExecutions = new ArrayList<JobExecution>();
        List<BatchJob> batchJobList = jobRepo.findByJobName(jobName);
        boolean jobExists = true;
        if (batchJobList == null || batchJobList.size() == 0) {
            jobExists = false;
        }
        if (!jobExists) {
            throw new BatchException(ApplicationErrors.JOB_NOT_FOUND);
        }

        int count = 0;

        try {
            count = jsrJobOperator.getJobInstanceCount(jobName);
        } catch (NoSuchJobException nsje) {
            throw new BatchException(ApplicationErrors.NO_SUCH_JOB_FOUND, nsje);
        }
        List<JobInstance> jobInstances = jsrJobOperator.getJobInstances(
                jobName, 0, count);
        for (int icount = 0; icount < jobInstances.size(); icount++) {
            jobExecutions.addAll(jsrJobOperator.getJobExecutions(jobInstances
                    .get(icount)));
        }
        JobExecutionHistoryListDTO historyListDTO = new JobExecutionHistoryListDTO();
        historyListDTO.setJobExecutionHistoryList(jobExecutions);
        return historyListDTO;

    }

    @Override
    public long restart(String jobName, Integer executionId)
            throws BatchException {
        ClassLoader contextClassLoader = null;
        Properties restartParameters = getJobParameters(jobName);
        try {
            contextClassLoader = Thread.currentThread().getContextClassLoader();
            BatchJobClassLoader testLoader = new BatchJobClassLoader(
                    contextClassLoader, xmlPath);

            Thread.currentThread().setContextClassLoader(testLoader);
            return jsrJobOperator.restart(executionId, restartParameters);
        } catch (NoSuchJobExecutionException e) {

            throw new BatchException(ApplicationErrors.BAD_REQUEST, e,
                    e.getMessage());
        } finally {

            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }

    }

}
