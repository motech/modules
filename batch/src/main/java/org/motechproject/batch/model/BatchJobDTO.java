package org.motechproject.batch.model;

import org.joda.time.DateTime;

import java.util.Map;

/**
 * Class containing fields of Batch Job which is to be sent as response through REST.
 *
 * @author Naveen
 */
public class BatchJobDTO {

    /**
     * The id of the batch job.
     */
    private long jobId;

    /**
     * The name of the batch job.
     */
    private String jobName;

    /**
     * The cron expression behind this batch job.
     */
    private String cronExpression;

    /**
     * The status of the batch job.
     */
    private String status;

    /**
     * A map of parameters for this batch job.
     */
    private Map<String, String> parameters;

    /**
     * The date time when this job was created.
     */
    private DateTime createTime;

    /**
     * The last modification date for this job.
     */
    private DateTime lastUpdated;

    /**
     * The username of the user who created this job.
     */
    private String createdBy;

    /**
     * The username of the user who last updated this job.
     */
    private String lastUpdatedBy;



    /**
     * @return the id of the batch job
     */
    public long getJobId() {
        return jobId;
    }

    /**
     * @param jobId the id of the batch job
     */
    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    /**
     * @return the name of the batch job
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * @param jobName the name of the batch job
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    /**
     * @return the cron expression behind this batch job
     */
    public String getCronExpression() {
        return cronExpression;
    }

    /**
     * @param cronExpression the cron expression behind this batch job
     */
    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    /**
     * @return the status of the batch job
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status of the batch job
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return a map of parameters for this batch job
     */
    public Map<String, String> getParameters() {
        return parameters;
    }

    /**
     * @param parameters a map of parameters for this batch job
     */
    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    /**
     * @return the date time when this job was created
     */
    public DateTime getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime the date time when this job was created
     */
    public void setCreateTime(DateTime createTime) {
        this.createTime = createTime;
    }

    /**
     * @return the last modification date for this job
     */
    public DateTime getLastUpdated() {
        return lastUpdated;
    }

    /**
     * @param lastUpdated the last modification date for this job
     */
    public void setLastUpdated(DateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * @return the username of the user who created this job
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the username of the user who created this job
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return the username of the user who last updated this job
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * @param lastUpdatedBy the username of the user who last updated this job
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

}
