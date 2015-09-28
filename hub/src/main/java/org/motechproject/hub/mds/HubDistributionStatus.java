package org.motechproject.hub.mds;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

/**
 * This entity stores the status of content distribution from hub to the
 * subscribers.
 */
@Entity
public class HubDistributionStatus implements java.io.Serializable {

    private static final long serialVersionUID = -6224225769462872750L;

    @Field(required = true)
    private Integer distributionStatusId;

    @Field(required = true)
    private String distributionStatusCode;

    /**
     * Gets the id of this distribution status.
     *
     * @return the id of this distribution status
     */
    public Integer getDistributionStatusId() {
        return distributionStatusId;
    }

    /**
     * Sets the id for this distribution status.
     *
     * @param distributionStatusId the id to be set
     */
    public void setDistributionStatusId(Integer distributionStatusId) {
        this.distributionStatusId = distributionStatusId;
    }

    /**
     * Gets the code of this distribution status.
     *
     * @return the code of this distribution status
     */
    public String getDistributionStatusCode() {
        return distributionStatusCode;
    }

    /**
     * Sets the code for this distribution status.
     *
     * @param distributionStatusCode the distribution status code to be set
     */
    public void setDistributionStatusCode(String distributionStatusCode) {
        this.distributionStatusCode = distributionStatusCode;
    }

    /**
     * Creates a new instance of <code>HubDistributionStatus</code>, with
     * all fields set to null.
     */
    public HubDistributionStatus() {
    }

    /**
     * Creates a new instance of <code>HubDistributionStatus</code>, with
     * all fields set to the values specified in the parameters.
     *
     * @param distributionStatusId the id of this distribution status
     * @param distributionStatusCode the code of this distribution status
     */
    public HubDistributionStatus(Integer distributionStatusId,
            String distributionStatusCode) {
        this.distributionStatusId = distributionStatusId;
        this.distributionStatusCode = distributionStatusCode;
    }

}
