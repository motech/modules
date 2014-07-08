package org.motechproject.hub.mds;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

/**
 * This entity stores the status of content distribution from hub to the
 * subsribers
 */
@Entity
public class HubDistributionStatus implements java.io.Serializable {

    private static final long serialVersionUID = -6224225769462872750L;

    @Field(required = true)
    private Integer distributionStatusId;

    @Field(required = true)
    private String distributionStatusCode;

    public Integer getDistributionStatusId() {
        return distributionStatusId;
    }

    public void setDistributionStatusId(Integer distributionStatusId) {
        this.distributionStatusId = distributionStatusId;
    }

    public String getDistributionStatusCode() {
        return distributionStatusCode;
    }

    public void setDistributionStatusCode(String distributionStatusCode) {
        this.distributionStatusCode = distributionStatusCode;
    }

    public HubDistributionStatus() {
    }

    public HubDistributionStatus(Integer distributionStatusId,
            String distributionStatusCode) {
        this.distributionStatusId = distributionStatusId;
        this.distributionStatusCode = distributionStatusCode;
    }

}
