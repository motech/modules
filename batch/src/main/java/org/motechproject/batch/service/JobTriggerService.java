package org.motechproject.batch.service;

import org.motechproject.batch.exception.BatchException;
import org.motechproject.batch.model.JobExecutionHistoryListDTO;
import org.springframework.stereotype.Service;

/**
 * Interface to perform the trigger operation for all types of jobs.
 *
 * @author Naveen
 *
 */
@Service
public interface JobTriggerService {

    /**
     * Trigger a job instantly.
     *
     * @param jobName name of the job to be triggered
     * @return the id of the execution
     * @throws BatchException if triggering the job failed
     */
    long triggerJob(String jobName) throws BatchException;

    /**
     * Returns the execution history for a job.
     *
     * @param jobName name of the job to query for history
     * @return the history of executions for this job
     * @throws BatchException if history retrieval failed
     */
    JobExecutionHistoryListDTO getJobExecutionHistory(String jobName) throws BatchException;

    /**
     * Restarts the execution of a batch job.
     *
     * @param jobName the name of batch job
     * @param executionId the id of the execution to restart
     * @return the id of the execution
     * @throws BatchException if restarting the job failed
     */
    long restart(String jobName, Integer executionId) throws BatchException;

}
