package org.motechproject.batch.service;

import org.motechproject.batch.exception.BatchException;
import org.motechproject.batch.model.BatchJobListDTO;
import org.motechproject.batch.model.CronJobScheduleParam;
import org.motechproject.batch.model.OneTimeJobScheduleParams;

import java.util.Map;

/**
 * Interface to schedule reschedule jobs or update job parameters
 *
 * @author Naveen
 *
 */
public interface JobService {

    /**
     * Get the list of scheduled jobs/
     *
     * @return {@link org.motechproject.batch.mds.BatchJob} which contains list of
     *          {@link org.motechproject.batch.model.BatchJobDTO}(contains fields from {@link org.motechproject.batch.mds.BatchJob})
     * @throws BatchException if unable to retrieve the job list
     */
    BatchJobListDTO getListOfJobs() throws BatchException;

    /**
     * Schedule a new cron job with given job name and cron expression.
     *
     * @param params -
     *            {@link CronJobScheduleParam} object containing jobName,
     *            paramsMap and cronExpression
     * @throws BatchException if unable to schedule the job
     */
    void scheduleJob(CronJobScheduleParam params) throws BatchException;

    /**
     * Schedule a one time job, to be run once in the future.
     *
     * @param params -
     *            {@link OneTimeJobScheduleParams} object containing
     *            jobName, paramsMap and date
     * @throws BatchException if unable to schedule the job
     */
    void scheduleOneTimeJob(OneTimeJobScheduleParams params) throws BatchException;

    /**
     * Update the job parameters of the scheduled job.
     *
     * @param jobName job name for the job for which parameters to be updated
     * @param paramsMap list of parameters to be added or changed
     * @throws BatchException if unable to update the job
     */
    void updateJobProperty(String jobName, Map<String, String> paramsMap) throws BatchException;

    /**
     * Returns the total count of batch jobs.
     * @return the number of batch jobs
     */
    long countJobs();

    /**
     * Re-schedule an existing batch job with given job name and cron expression.
     *
     * @param jobName job name for the job to be scheduled
     * @param cronExpression
     *            cron expression for the job (specified for the timely run of
     *            the job)
     */
    void rescheduleJob(String jobName, String cronExpression);

    /**
     * Unschedule an existing batch job with given job name.
     *
     * @param jobName job name for the job to be scheduled
     * @throws BatchException if unable to unschedule the job
     */
    void unscheduleJob(String jobName) throws BatchException;

}
