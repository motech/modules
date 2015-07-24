package org.motechproject.batch.service.impl;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.motechproject.batch.exception.ApplicationErrors;
import org.motechproject.batch.exception.BatchException;
import org.motechproject.batch.mds.BatchJob;
import org.motechproject.batch.mds.BatchJobParameters;
import org.motechproject.batch.mds.service.BatchJobMDSService;
import org.motechproject.batch.mds.service.BatchJobParameterMDSService;
import org.motechproject.batch.model.JobExecutionHistoryListDTO;
import org.motechproject.batch.service.JobTriggerService;
import org.motechproject.batch.util.BatchConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.batch.operations.JobOperator;
import javax.batch.operations.JobSecurityException;
import javax.batch.operations.JobStartException;
import javax.batch.operations.NoSuchJobException;
import javax.batch.operations.NoSuchJobExecutionException;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.JobInstance;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Class to perform the trigger operation for all types of jobs
 *
 * @author Naveen
 *
 */
@Service(value = "jobTriggerService")
public class JobTriggerServiceImpl implements JobTriggerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobTriggerServiceImpl.class);

    private JobOperator jsrJobOperator;

    private BatchJobParameterMDSService jobParameterRepo;

    private BatchJobMDSService jobRepo;

    @Autowired
    public JobTriggerServiceImpl(BatchJobMDSService jobRepo,
            BatchJobParameterMDSService jobParameterRepo,
            JobOperator jsrJobOperator) {
        this.jobRepo = jobRepo;
        this.jobParameterRepo = jobParameterRepo;
        this.jsrJobOperator = jsrJobOperator;

    }

    @Override
    public long triggerJob(String jobName) throws BatchException {
        LOGGER.info("Starting execution of JOB {}", jobName);

        Properties jobParameters = getJobParameters(jobName);

        storeJobContent(jobName);
        try {
            return jsrJobOperator.start(jobName, jobParameters);
        } catch (JobStartException | JobSecurityException e) {
            throw new BatchException(ApplicationErrors.JOB_TRIGGER_FAILED, e,
                    e.getMessage());
        }
    }

    private void storeJobContent(String jobName) throws BatchException {
        BatchJob batchJob = jobRepo.findByJobName(jobName);
        if (batchJob == null) {
            throw new BatchException(ApplicationErrors.JOB_NOT_FOUND);
        }

        String batchConfigResourcePath = BatchConstants.BATCH_XML_CONFIG_PATH
                + jobName + BatchConstants.XML_EXTENSION;
        try (InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(batchConfigResourcePath)) {
            if (is != null) {
                batchJob.setJobContent(ArrayUtils.toObject(IOUtils.toByteArray(is)));
                jobRepo.update(batchJob);
            } else {
                throw new BatchException(
                        ApplicationErrors.FILE_READING_WRTING_FAILED);
            }
        } catch (IOException e) {
            throw new BatchException(
                    ApplicationErrors.FILE_READING_WRTING_FAILED, e,
                    e.getMessage());
        }
    }

    @Override
    public JobExecutionHistoryListDTO getJobExecutionHistory(String jobName)
            throws BatchException {
        List<JobExecution> jobExecutions = new ArrayList<>();
        BatchJob batchJob = jobRepo.findByJobName(jobName);
        if (batchJob == null) {
            throw new BatchException(ApplicationErrors.JOB_NOT_FOUND);
        }

        int count;

        try {
            count = jsrJobOperator.getJobInstanceCount(jobName);
        } catch (NoSuchJobException nsje) {
            throw new BatchException(ApplicationErrors.JOB_NOT_FOUND, nsje);
        }
        List<JobInstance> jobInstances = jsrJobOperator.getJobInstances(
                jobName, 0, count);

        for (JobInstance jobInstance : jobInstances) {
            jobExecutions.addAll(jsrJobOperator.getJobExecutions(jobInstance));
        }

        JobExecutionHistoryListDTO historyListDTO = new JobExecutionHistoryListDTO();
        historyListDTO.setJobExecutionHistoryList(jobExecutions);

        return historyListDTO;

    }

    @Override
    public long restart(String jobName, Integer executionId)
            throws BatchException {
        Properties restartParameters = getJobParameters(jobName);
        try {
            return jsrJobOperator.restart(executionId, restartParameters);
        } catch (NoSuchJobExecutionException e) {
            throw new BatchException(ApplicationErrors.BAD_REQUEST, e,
                    e.getMessage());
        }

    }

    private Properties getJobParameters(String jobName) throws BatchException {
        BatchJob batchJob = jobRepo.findByJobName(jobName);
        if (batchJob == null) {
            throw new BatchException(ApplicationErrors.JOB_NOT_FOUND);
        }

        long batchJobId = batchJob.getId();

        List<BatchJobParameters> parametersList = jobParameterRepo
                .findByJobId((int) batchJobId);

        Properties jobParameters = new Properties();

        for (BatchJobParameters batchJobParameter : parametersList) {
            jobParameters.put(batchJobParameter.getParameterName(),
                    batchJobParameter.getParameterValue());
        }
        return jobParameters;
    }
}
