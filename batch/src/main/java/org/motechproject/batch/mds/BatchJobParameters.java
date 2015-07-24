package org.motechproject.batch.mds;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

/**
 * Class containing parameters as fields to run the job.
 */
@Entity
public class BatchJobParameters implements java.io.Serializable {

    private static final long serialVersionUID = -5755680223644726209L;

    /**
     * The id of the parent batch job for this parameter.
     */
    @Field(required = true)
    private Integer batchJobId;

    /**
     * The name of the parameter.
     */
    @Field(required = true)
    private String parameterName;

    /**
     * The value of the parameter.
     */
    @Field
    private String parameterValue;

    /**
     * Constructs an instance without setting any fields.
     */
    public BatchJobParameters() {
    }

    /**
     * Constructs a parameter without a value.
     * @param batchJobId the id of the batch job
     * @param parameterName the name of this parameter
     */
    public BatchJobParameters(Integer batchJobId, String parameterName) {
        this.batchJobId = batchJobId;
        this.parameterName = parameterName;
    }

    /**
     * @param batchJobId the id of the batch job
     * @param parameterName the name of this parameter
     * @param parameterValue the value of the parameter
     */
    public BatchJobParameters(Integer batchJobId, String parameterName,
            String parameterValue) {
        this.batchJobId = batchJobId;
        this.parameterName = parameterName;
        this.parameterValue = parameterValue;

    }

    /**
     * @return the id of the batch job for this parameter
     */
    public Integer getBatchJobId() {
        return batchJobId;
    }

    /**
     * @param batchJobId the id of the batch job fpr this parameter
     */
    public void setBatchJobId(Integer batchJobId) {
        this.batchJobId = batchJobId;
    }

    /**
     * @return the name of this parameter
     */
    public String getParameterName() {
        return parameterName;
    }

    /**
     * @param parameterName the name of this parameter
     */
    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    /**
     * @return the value of this parameter
     */
    public String getParameterValue() {
        return parameterValue;
    }

    /**
     * @param parameterValue the value of this parameter
     */
    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }
}
