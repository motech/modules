package org.motechproject.batch.web;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.StopWatch;
import org.motechproject.batch.exception.ApplicationErrors;
import org.motechproject.batch.exception.BatchError;
import org.motechproject.batch.exception.BatchException;
import org.motechproject.batch.exception.RestException;
import org.motechproject.batch.model.BatchJobListDTO;
import org.motechproject.batch.model.BatchJobUpdateParams;
import org.motechproject.batch.model.CronJobScheduleParam;
import org.motechproject.batch.model.JobExecutionHistoryListDTO;
import org.motechproject.batch.model.OneTimeJobScheduleParams;
import org.motechproject.batch.service.JobService;
import org.motechproject.batch.service.JobTriggerService;
import org.motechproject.batch.util.BatchConstants;
import org.motechproject.batch.validation.BatchValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Controller class to perform all the batch job operations
 *
 * @author Naveen
 *
 */
@Controller
@RequestMapping("/")
public class BatchController {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(BatchController.class);

    @Autowired
    private JobService jobService;

    @Autowired
    private JobTriggerService jobTriggerService;

    @Autowired
    private BatchValidator batchValidator;

    /**
     * To get list of all the scheduled jobs
     *
     * @return List of BatchJob.
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/jobs", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public BatchJobListDTO getJobList() {
        LOGGER.info("Request to get list of batch jobs started");
        StopWatch sw = new StopWatch();
        sw.start();
        BatchJobListDTO batchJobList;
        try {
            batchJobList = jobService.getListOfJobs();
            return batchJobList;
        } catch (BatchException e) {
            LOGGER.error("Error occurred while processing request to get list of jobs");
            throw new RestException(e, e.getMessage());
        } finally {
            LOGGER.info(
                    "Request to get list of batch jobs ended. Time taken (ms) = {}",
                    sw.getTime());
            sw.stop();
        }

    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/jobHistory", method = RequestMethod.GET, params = { BatchConstants.JOB_NAME_REQUEST_PARAM })
    @ResponseBody
    public JobExecutionHistoryListDTO getjobHistoryList(
            @RequestParam(value = BatchConstants.JOB_NAME_REQUEST_PARAM) String jobName) {
        LOGGER.info("Request to get execution history of job {} started",
                jobName);
        StopWatch sw = new StopWatch();
        sw.start();
        JobExecutionHistoryListDTO jobExecutionHistoryList;
        try {
            List<String> errors = batchValidator.validateUpdateInputs(jobName);
            if (!errors.isEmpty()) {
                throw new BatchException(ApplicationErrors.BAD_REQUEST,
                        errors.toString());
            }
            jobExecutionHistoryList = jobTriggerService
                    .getJobExecutionHistory(jobName);
            return jobExecutionHistoryList;
        } catch (BatchException e) {
            LOGGER.error(
                    "Error occurred while processing request to get execution history for job {}",
                    jobName);
            throw new RestException(e, e.getMessage());
        } finally {
            LOGGER.info(
                    "Request to get execution history for job {} ended. Time taken (ms) = {}",
                    jobName, sw.getTime());
            sw.stop();
        }

    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/trigger", method = RequestMethod.POST, params = { BatchConstants.JOB_NAME_REQUEST_PARAM })
    @ResponseBody
    public long triggerJob(
            @RequestParam(BatchConstants.JOB_NAME_REQUEST_PARAM) String jobName) {

        LOGGER.info("Request to trigger the job {} started", jobName);
        StopWatch sw = new StopWatch();
        sw.start();
        try {
            List<String> errors = batchValidator.validateUpdateInputs(jobName);
            if (!errors.isEmpty()) {
                throw new BatchException(ApplicationErrors.BAD_REQUEST,
                        errors.toString());
            }
            return jobTriggerService.triggerJob(jobName);
        } catch (BatchException e) {
            LOGGER.error(
                    "Error occurred while processing request to trigger job {}",
                    jobName);
            throw new RestException(e, e.getMessage());
        } finally {
            LOGGER.info(
                    "Request to trigger the job {} ended. Time taken (ms) = {} ",
                    jobName, sw.getTime());
            sw.stop();
        }
    }

    /**
     * Schedule a cron job given job name, cron expression and parameters for
     * the job
     *
     * @param params
     *            - <code>CronJobScheduleParam</code> object containing jobName,
     *            paramsMap and cronExpression
     * @throws BatchException
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/schedulecronjob", method = RequestMethod.POST)
    @ResponseBody
    public void scheduleCronJob(@RequestBody CronJobScheduleParam params) {

        LOGGER.info("Request to schedule a cron job started");
        StopWatch sw = new StopWatch();
        sw.start();
        try {
            List<String> errors = batchValidator.validateShedulerInputs(
                    params.getJobName(), params.getCronExpression());

            if (!errors.isEmpty()) {
                throw new BatchException(ApplicationErrors.BAD_REQUEST,
                        errors.toString());
            }
            jobService.scheduleJob(params);
        } catch (BatchException e) {
            LOGGER.error(
                    "Error occured while processing request to schedule a cron job for job {} with cron expression {}",
                    params.getJobName(), params.getCronExpression());
            throw new RestException(e, e.getMessage());
        } finally {
            LOGGER.info(
                    "Request to schedule a cron job for job {} ended. Time taken (ms) = {}",
                    params.getJobName(), sw.getTime());
            sw.stop();
        }

    }

    /**
     * Reschedule a cron job given job name, cron expression
     *
     * @param jobName
     *            jobName for the job to be rescheduled
     * @param cronExpression
     *            cron expression for the job
     *
     * @throws BatchException
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/reschedulecronjob", method = RequestMethod.POST, params = { BatchConstants.JOB_NAME_REQUEST_PARAM })
    @ResponseBody
    public void rescheduleJob(
            @RequestParam(value = BatchConstants.JOB_NAME_REQUEST_PARAM) String jobName,
            @RequestParam("cronExpression") String cronExpression) {

        LOGGER.info(
                "Request to reschedule a cron job for job {} with cron expression {} started",
                jobName, cronExpression);
        StopWatch sw = new StopWatch();
        sw.start();
        try {
            List<String> errors = batchValidator.validateShedulerInputs(
                    jobName, cronExpression);

            if (!errors.isEmpty()) {
                throw new BatchException(ApplicationErrors.BAD_REQUEST,
                        errors.toString());
            }
            jobService.rescheduleJob(jobName, cronExpression);
        } catch (BatchException e) {
            LOGGER.error(
                    "Error occured while processing request to schedule a cron job for job {} with cron expression {}",
                    jobName, cronExpression);
            throw new RestException(e, e.getMessage());
        } finally {
            LOGGER.info(
                    "Request to reschedule a cron job for job {} ended. Time taken (ms) = {}",
                    jobName, sw.getTime());
            sw.stop();
        }

    }

    /**
     * Unschedule a cron job given job name
     *
     * @param jobName
     *            jobName for the job to be rescheduled
     *
     * @throws BatchException
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/unschedulecronjob", method = RequestMethod.POST, params = { BatchConstants.JOB_NAME_REQUEST_PARAM })
    @ResponseBody
    public void unscheduleJob(
            @RequestParam(value = BatchConstants.JOB_NAME_REQUEST_PARAM) String jobName) {

        LOGGER.info("Request to unschedule a cron job for job {} started",
                jobName);
        StopWatch sw = new StopWatch();
        sw.start();
        try {
            List<String> errors = batchValidator.validateUpdateInputs(jobName);

            if (!errors.isEmpty()) {
                throw new BatchException(ApplicationErrors.BAD_REQUEST,
                        errors.toString());
            }
            jobService.unscheduleJob(jobName);
        } catch (BatchException e) {
            LOGGER.error(
                    "Error occured while processing request to unschedule the job {}",
                    jobName);
            throw new RestException(e, e.getMessage());
        } finally {
            LOGGER.info(
                    "Request to reschedule a cron job for job {} ended. Time taken (ms) = {}",
                    jobName, sw.getTime());
            sw.stop();
        }

    }

    /**
     * schedules a job to be run at one particular time in future
     *
     * @param params
     *            <code>OneTimeJobScheduleParams</code> object containing
     *            jobName, paramsMap and date
     *
     * @throws BatchException
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/scheduleonetimejob", method = RequestMethod.POST)
    @ResponseBody
    public void scheduleOneTimeJob(@RequestBody OneTimeJobScheduleParams params) {
        LOGGER.info(
                "Request to schedule one time job for job {} with date {} started",
                params.getJobName(), params.getDate());
        StopWatch sw = new StopWatch();
        sw.start();

        try {
            List<String> errors = batchValidator.validateOneTimeInputs(
                    params.getJobName(), params.getDate());

            if (!errors.isEmpty()) {
                throw new BatchException(ApplicationErrors.BAD_REQUEST,
                        errors.toString());
            }
            jobService.scheduleOneTimeJob(params);
        } catch (BatchException e) {
            LOGGER.error(
                    "Error occured while processing request to schedule one time job for job {} with date {}",
                    params.getJobName(), params.getDate());
            throw new RestException(e, e.getMessage());
        } finally {
            LOGGER.info(
                    "Request to schedule one time job for job {} ended. Time taken (ms) = {}",
                    params.getJobName(), sw.getTime());
            sw.stop();
        }

    }

    /**
     * Update the parameter list for the job
     *
     * @param jobName  the name of the job for which parameters needs to be updated
     * @param paramsMap   the <code>map</code> of parameters to be added or modified
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/updatejobproperty", method = RequestMethod.POST)
    @ResponseBody
    public void updateJobProperty(@RequestBody BatchJobUpdateParams params) {
        LOGGER.info("Request to update job properties for job {} started",
                params.getJobName());
        StopWatch sw = new StopWatch();
        sw.start();
        try {
            List<String> errors = batchValidator.validateUpdateInputs(params
                    .getJobName());

            if (!errors.isEmpty()) {
                throw new BatchException(ApplicationErrors.BAD_REQUEST,
                        errors.toString());
            }
            jobService.updateJobProperty(params.getJobName(),
                    params.getParamsMap());
        } catch (BatchException e) {
            LOGGER.error(
                    "Error occured while processing request to update job properties for job {}",
                    params.getJobName());
            throw new RestException(e, e.getMessage());
        } finally {
            LOGGER.info(
                    "Request to update job properties for job {} ended. Time taken (ms) = {}",
                    params.getJobName(), sw.getTime());
            sw.stop();
        }

    }

    /**
     * It is custom exception to be thrown in case of validation failures or
     * other internal failures
     *
     * @param ex
     * @param response
     */
    @ExceptionHandler(value = { RestException.class })
    @ResponseBody
    public BatchError restExceptionHandler(RestException ex,
            HttpServletResponse response) {
        BatchError error = new BatchError();

        try {
            response.setStatus(ex.getHttpStatus().value());
            error.setErrorCode(String.valueOf(ex.getBatchException()
                    .getErrorCode()));
            error.setErrorMessage(ex.getBatchException().getErrorMessage());
            error.setApplication("batch");

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return error;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/restart", method = RequestMethod.POST, params = { BatchConstants.JOB_NAME_REQUEST_PARAM })
    @ResponseBody
    public long restartExecution(
            @RequestParam(value = BatchConstants.JOB_NAME_REQUEST_PARAM) String jobName,
            @RequestParam("executionId") Integer executionId) {

        LOGGER.info("Request to restart the execution {} for job {} started",
                executionId, jobName);
        StopWatch sw = new StopWatch();
        sw.start();
        try {

            return jobTriggerService.restart(jobName, executionId);
        } catch (BatchException e) {
            LOGGER.error(e.getMessage());
            throw new RestException(e, e.getMessage());
        } finally {
            LOGGER.info("Request to restart the execution {} for job {} ended . Time taken (ms) = {}",
                    executionId, jobName, sw.getTime());
            sw.stop();
        }
    }

    /**
     * Ping method to test if application is up
     */
    @RequestMapping("/ping")
    @ResponseBody
    public String ping() {
        return jobService.sayHello();

    }
}
