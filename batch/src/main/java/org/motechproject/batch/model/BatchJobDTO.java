package org.motechproject.batch.model;

import java.util.Map;

import org.joda.time.DateTime;

/**
 * Class containing fields of Batch Job which is to be sent as response
 *
 * @author Naveen
 */
public class BatchJobDTO {

    private long jobId;
    private String jobName;
    private String cronExpression;
    private String status;
    private Map<String, String> parametersList;
    private DateTime createTime;
    private DateTime lastUpdated;
    private String createdBy;
    private String lastUpdatedBy;

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, String> getParametersList() {
        return parametersList;
    }

    public void setParametersList(
            Map<String, String> parametersList) {
        this.parametersList = parametersList;
    }

    public DateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(DateTime createTime) {
        this.createTime = createTime;
    }

    public DateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(DateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

}
