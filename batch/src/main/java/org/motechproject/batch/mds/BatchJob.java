package org.motechproject.batch.mds;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

import javax.jdo.annotations.Unique;

/**
 * Class containing all the fields related to a batch job. This is a domain object
 * persisted using MDS.
 *
 * @author naveen
 *
 */
@Entity
public class BatchJob extends MdsEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The name of the job.
     */
    @Field(required = true)
    @Unique
    private String jobName;

    /**
     * The status ID for the batch job.
     */
    @Field(required = true)
    private Integer batchJobStatusId;

    /**
     * The cron expression behind this batch job.
     */
    @Field
    private String cronExpression;

    /**
     * The content for this job, serialized to bytes.
     */
    @Field
    private Byte[] jobContent;

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
     * @return the status ID for the batch job
     */
    public Integer getBatchJobStatusId() {
        return batchJobStatusId;
    }

    /**
     * @param batchJobStatusId the status ID for the batch job
     */
    public void setBatchJobStatusId(Integer batchJobStatusId) {
        this.batchJobStatusId = batchJobStatusId;
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
     * Returns the content for the job. This will return a copy of the array from this entity.
     * @return the content for this job, serialized to bytes
     */
    public Byte[] getJobContent() {
        if (jobContent != null) {
            return jobContent.clone();
        }
        return null;
    }

    /**
     * Sets the content for the job. This will copy the provided content array.
     * @param jobContent the content for this job, serialized to bytes
     */
    public void setJobContent(Byte[] jobContent) {
        if (jobContent != null) {
            this.jobContent = jobContent.clone();
        }
    }

    /**
     * Constructs an instance without setting any fields.
     */
    public BatchJob() {
    }

    /**
     * Constructs a job using the given id and job name.
     * @param batchJobStatusId the status ID for the batch job
     * @param jobName the name of the job
     */
    public BatchJob(Integer batchJobStatusId, String jobName) {
        this.batchJobStatusId = batchJobStatusId;
        this.jobName = jobName;
    }

    /**
     * Constructs a job using the given id, job name and cron expression.
     * @param batchJobStatusId the status ID for the batch job
     * @param jobName the name of the job
     * @param cronExpression the cron expression behind this batch job
     */
    public BatchJob(Integer batchJobStatusId, String jobName,
            String cronExpression) {
        this.batchJobStatusId = batchJobStatusId;
        this.jobName = jobName;
        this.cronExpression = cronExpression;
    }

}
