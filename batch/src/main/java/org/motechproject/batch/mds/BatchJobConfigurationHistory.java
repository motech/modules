package org.motechproject.batch.mds;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

/**
 * Class containing batch job configuration
 */
@Entity
public class BatchJobConfigurationHistory implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Field(required = true)
    private Integer batchJobId;

    @Field(required = true)
    private String jobConfiguration;

    public Integer getBatchJobId() {
        return batchJobId;
    }

    public void setBatchJobId(Integer batchJobId) {
        this.batchJobId = batchJobId;
    }

    public String getJobConfiguration() {
        return jobConfiguration;
    }

    public void setJobConfiguration(String jobConfiguration) {
        this.jobConfiguration = jobConfiguration;
    }

    public BatchJobConfigurationHistory(Integer batchJobId,
            String jobConfiguration) {
        this.batchJobId = batchJobId;
        this.jobConfiguration = jobConfiguration;
    }

}
