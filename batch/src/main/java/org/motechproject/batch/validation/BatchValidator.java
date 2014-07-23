package org.motechproject.batch.validation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.batch.util.BatchConstants;
import org.quartz.CronExpression;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

/**
 * Validates the input parameters of batch service API calls for HTTP requests
 *
 * @author naveen
 */
@Service
public class BatchValidator {

    public List<String> validateShedulerInputs(String jobName,
            String cronExpression) {
        List<String> errors = new ArrayList<String>();
        checkJobName(jobName, errors);

        checkCronExpression(cronExpression, errors);
        return errors;
    }

    public List<String> validateOneTimeInputs(String jobName, String date) {

        List<String> errors = new ArrayList<String>();
        checkJobName(jobName, errors);
        checkDate(date, errors);
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

    public List<String> validateUpdateInputs(String jobName) {

        List<String> errors = new ArrayList<String>();
        checkJobName(jobName, errors);
        return errors;
    }

    public List<String> validateUploadInputs(String jobName, String contentType) {

        List<String> errors = new ArrayList<String>();
        checkJobName(jobName, errors);
        checkContentType(contentType, errors);
        return errors;
    }

    private void checkContentType(String contentType, List<String> errors) {
        if (!MediaType.TEXT_XML.equals(MediaType.valueOf(contentType))) {
            errors.add("You must upload xml file for the job");
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
