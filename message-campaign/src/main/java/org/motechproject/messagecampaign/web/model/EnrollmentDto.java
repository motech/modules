package org.motechproject.messagecampaign.web.model;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.joda.time.LocalDate;
import org.motechproject.commons.date.model.Time;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.web.util.LocalDateSerializer;
import org.motechproject.messagecampaign.web.util.TimeSerializer;

import java.util.Objects;

/**
 * DTO representation of an enrollment. It is used to pass the representation between
 * view and controller layers.
 */
public class EnrollmentDto {

    /**
     * ID of the enrollment.
     */
    @JsonProperty
    private Long enrollmentId;

    /**
     * External ID of the client.
     */
    @JsonProperty
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private String externalId;

    /**
     * The name of the campaign this enrollment is assigned to.
     */
    @JsonProperty
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private String campaignName;

    /**
     * The preferred time to send the messages.
     */
    @JsonProperty
    @JsonSerialize(using = TimeSerializer.class)
    private Time startTime;

    /**
     * The reference date, used as a start point to send messages.
     */
    @JsonProperty
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate referenceDate;

    public EnrollmentDto() {
    }

    public EnrollmentDto(CampaignEnrollment enrollment) {
        enrollmentId = enrollment.getId();
        externalId = enrollment.getExternalId();
        campaignName = enrollment.getCampaignName();
        referenceDate = enrollment.getReferenceDate();
        startTime = enrollment.getDeliverTime();
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public LocalDate getReferenceDate() {
        return referenceDate;
    }

    public void setReferenceDate(LocalDate referenceDate) {
        this.referenceDate = referenceDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EnrollmentDto)) {
            return false;
        }

        EnrollmentDto that = (EnrollmentDto) o;

        return Objects.equals(externalId, that.externalId) && Objects.equals(campaignName, that.campaignName)
                && Objects.equals(referenceDate, that.referenceDate) && Objects.equals(startTime, that.startTime)
                && Objects.equals(enrollmentId, that.enrollmentId);
    }

    @Override
    public int hashCode() {
        int result = externalId != null ? externalId.hashCode() : 0;
        result = 31 * result + (campaignName != null ? campaignName.hashCode() : 0);
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (referenceDate != null ? referenceDate.hashCode() : 0);
        result = 31 * result + (enrollmentId != null ? enrollmentId.hashCode() : 0);
        return result;
    }
}
