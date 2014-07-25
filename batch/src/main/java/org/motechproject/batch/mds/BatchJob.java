package org.motechproject.batch.mds;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

/**
 * Class containing all the fields related to job
 *
 * @author naveen
 *
 */
@Entity
public class BatchJob implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Field(required = true)
    private String jobName;

    @Field(required = true)
    private Integer batchJobStatusId;

    @Field
    private String cronExpression;

    @Field
    private byte[] jobContent;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Integer getBatchJobStatusId() {
        return batchJobStatusId;
    }

    public void setBatchJobStatusId(Integer batchJobStatusId) {
        this.batchJobStatusId = batchJobStatusId;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public byte[] getJobContent() {
        return jobContent.clone();
    }

    public void setJobContent(byte[] jobContent) {
        if (jobContent != null) {
            this.jobContent = jobContent.clone();
        }
    }

    public BatchJob() {
    }

    public BatchJob(Integer batchJobStatusId, String jobName) {
        this.batchJobStatusId = batchJobStatusId;
        this.jobName = jobName;
    }

    public BatchJob(Integer batchJobStatusId, String jobName,
            String cronExpression) {
        this.batchJobStatusId = batchJobStatusId;
        this.jobName = jobName;
        this.cronExpression = cronExpression;
    }

}
