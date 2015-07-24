package org.motechproject.batch.validation;

import org.apache.commons.lang.StringUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.batch.util.BatchConstants;
import org.quartz.CronExpression;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Validates the input parameters of batch service API calls for HTTP requests
 *
 * @author naveen
 */
@Service
public class BatchValidator {

    /**
     * Validates inputs for a cron job - the job name and cron expression.
     * @param jobName the job name to validate
     * @param cronExpression the cron expression to validate
     * @return list of errors from validation, empty list if there were no errors
     */
    public List<String> validateSchedulerInputs(String jobName, String cronExpression) {
        List<String> errors = new ArrayList<>();
        checkJobName(jobName, errors);

        checkCronExpression(cronExpression, errors);
        return errors;
    }

    /**
     * Validates inputs for one time job - the job name and the date.
     * @param jobName the job name
     * @param date the date on which this job should run
     * @return list of errors from validation, empty list if there were no errors
     */
    public List<String> validateOneTimeInputs(String jobName, String date) {

        List<String> errors = new ArrayList<>();
        checkJobName(jobName, errors);
        checkDate(date, errors);
        return errors;
    }

    /**
     * Validates inputs for an update of job parameters - the job name.
     * @param jobName the job name
     * @return list of errors from validation, empty list if there were no errors
     */
    public List<String> validateUpdateInputs(String jobName) {

        List<String> errors = new ArrayList<>();
        checkJobName(jobName, errors);
        return errors;
    }

    private void checkDate(String date, List<String> errors) {
        if (StringUtils.isBlank(date)) {
            errors.add("Date must be provided.");
        } else {
            DateTimeFormatter formatter = DateTimeFormat
                    .forPattern(BatchConstants.DATE_FORMAT);
            try {
                formatter.parseDateTime(date);
            } catch (IllegalArgumentException e) {
                errors.add("Date passed is invalid. Passed value: [" + date
                        + "]");
            }
        }
    }

    private void checkCronExpression(String cronExpression, List<String> errors) {
        if (cronExpression == null
                || !CronExpression.isValidExpression(cronExpression)) {
            errors.add("Job cron expression supplied is not valid");
        }
    }

    private void checkJobName(String jobName, List<String> errors) {
        if (StringUtils.isBlank(jobName)) {
            errors.add("Job name must be provided");
        }
    }
}
