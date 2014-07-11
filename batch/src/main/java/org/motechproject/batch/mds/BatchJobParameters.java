package org.motechproject.batch.mds;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

/**
 * Class containing parameters as fields to run the job
 */
@Entity
public class BatchJobParameters implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Field(required = true)
    private Integer batchJobId;

    @Field(required = true)
    private String parameterName;

    @Field
    private String parameterValue;

    public BatchJobParameters() {
    }

    public BatchJobParameters(Integer batchJobId, String parameterName) {
        this.batchJobId = batchJobId;
        this.parameterName = parameterName;
    }

    public BatchJobParameters(Integer batchJobId, String parameterName,
            String parameterValue) {
        this.batchJobId = batchJobId;
        this.parameterName = parameterName;
        this.parameterValue = parameterValue;

    }

    public Integer getBatchJobId() {
        return batchJobId;
    }

    public void setBatchJobId(Integer batchJobId) {
        this.batchJobId = batchJobId;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

}
