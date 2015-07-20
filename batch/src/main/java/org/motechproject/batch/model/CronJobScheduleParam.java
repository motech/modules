package org.motechproject.batch.model;

import java.util.Map;

/**
 * A representation of a cron job to be scheduled.
 */
public class CronJobScheduleParam {

    /**
     * The name of the job.
     */
    private String jobName;

    /**
     * The cron expression for this job.
     */
    private String cronExpression;

    /**
     * A map containing parameters for this job.
     */
    private Map<String, String> paramsMap;

    /**
     * @return the name of the job
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * @param jobName the name of the job
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    /**
     * @return the cron expression for this job
     */
    public String getCronExpression() {
        return cronExpression;
    }

    /**
     * @param cronExpression the cron expression for this job
     */
    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    /**
     * @return a map containing parameters for this job
     */
    public Map<String, String> getParamsMap() {
        return paramsMap;
    }

    /**
     * @param paramsMap a map containing parameters for this job
     */
    public void setParamsMap(Map<String, String> paramsMap) {
        this.paramsMap = paramsMap;
    }

}
