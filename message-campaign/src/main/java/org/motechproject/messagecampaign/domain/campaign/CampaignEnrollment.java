package org.motechproject.messagecampaign.domain.campaign;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.LocalDate;
import org.motechproject.commons.date.model.Time;
import org.motechproject.mds.annotations.CrudEvents;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.event.CrudEventType;

import javax.jdo.annotations.Unique;

/**
 * Represents an enrollment for a campaign
 */

@Entity
@CrudEvents(CrudEventType.NONE)
@Unique(name = "externalIdAndCampaignName", members = {"externalId", "campaignName" })
public class CampaignEnrollment {

    private Long id;

    @Field(required = true)
    private String externalId;

    @Field(required = true)
    private String campaignName;

    private CampaignEnrollmentStatus status;
    private LocalDate referenceDate;
    private Time deliverTime;

    private CampaignEnrollment() {
    }

    public CampaignEnrollment(String externalId, String campaignName) {
        this.externalId = externalId;
        this.campaignName = campaignName;
        this.status = CampaignEnrollmentStatus.ACTIVE;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public LocalDate getReferenceDate() {
        return referenceDate;
    }

    public CampaignEnrollment copyFrom(CampaignEnrollment enrollment) {
        this.referenceDate = enrollment.getReferenceDate();
        this.status = enrollment.getStatus();
        this.deliverTime = enrollment.getDeliverTime();
        this.externalId = enrollment.getExternalId();
        return this;
    }

    public CampaignEnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(CampaignEnrollmentStatus status) {
        this.status = status;
    }

    public Time getDeliverTime() {
        return deliverTime;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public void setReferenceDate(LocalDate referenceDate) {
        this.referenceDate = referenceDate;
    }

    public void setDeliverTime(Time deliverTime) {
        this.deliverTime = deliverTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public boolean isActive() {
        return status.equals(CampaignEnrollmentStatus.ACTIVE);
    }
}
