package org.motechproject.messagecampaign.web.model;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO representation of all the enrollments assigned to a client. It is
 * used by the view layer to display the records.
 */
public class EnrollmentList {

    /**
     * The name of the campaign the enrollments belong to.
     */
    @JsonProperty
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private String campaignName;

    /**
     * The external ID of the client.
     */
    @JsonProperty
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private String externalId;

    /**
     * A list of enrollments.
     */
    @JsonProperty
    private List<EnrollmentDto> enrollments = new ArrayList<>();

    public EnrollmentList() {
    }

    public EnrollmentList(List<CampaignEnrollment> enrollments) {
        addEnrollments(enrollments);
    }

    public final void addEnrollments(List<CampaignEnrollment> enrollments) {
        for (CampaignEnrollment enrollment : enrollments) {
            this.enrollments.add(new EnrollmentDto(enrollment));
        }
    }

    public void setCommonCampaignName(String campaignName) {
        this.campaignName = campaignName;
        for (EnrollmentDto enrollmentDto : enrollments) {
            enrollmentDto.setCampaignName(null);
        }
    }

    public void setCommonExternalId(String externalId) {
        this.externalId = externalId;
        for (EnrollmentDto enrollmentDto : enrollments) {
            enrollmentDto.setExternalId(null);
        }
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public List<EnrollmentDto> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<EnrollmentDto> enrollments) {
        this.enrollments = enrollments;
    }
}
