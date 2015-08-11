package org.motechproject.messagecampaign.domain.campaign;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.LocalDate;
import org.motechproject.commons.date.model.Time;
import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.CrudEvents;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.event.CrudEventType;
import org.motechproject.mds.util.SecurityMode;

import javax.jdo.annotations.Unique;

/**
 * Domain representation of an enrollment for the message campaign.
 */
@Entity
@CrudEvents(CrudEventType.NONE)
@Unique(name = "externalIdAndCampaignName", members = {"externalId", "campaignName" })
@Access(value = SecurityMode.PERMISSIONS, members = {"manageEnrollments"})
public class CampaignEnrollment {

    /**
     * The database ID of this record.
     */
    @Field
    private Long id;

    /**
     * The external ID used to identify the enrollment.
     */
    @Field(required = true, tooltip = "A client defined id to identify the enrollment", placeholder = "1234")
    private String externalId;

    /**
     * The name of the campaign.
     */
    @Field(required = true, placeholder = "Add the name of the campaign")
    private String campaignName;

    /**
     * The current status of this campaign enrollment.
     */
    @Field
    private CampaignEnrollmentStatus status;

    /**
     * A reference date that will be used to define delivery dates for campaigns.
     */
    @Field(placeholder = "yyyy-mm-dd")
    private LocalDate referenceDate;

    /**
     * An optional field, for specifying the delivery time of the messages. If not provided, the value from the
     * {@link CampaignMessageRecord} of the corresponding campaign will be used.
     */
    @Field(placeholder = "hh:mm")
    private Time deliverTime;

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

    /**
     * Updates this campaign enrollment with the values taken from the other campaign enrollment.
     * All of the fields of this class will be replaced, except of the {@link #id} and {@link #campaignName}.
     *
     * @param enrollment a campaign enrollment to update the values from
     */
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

    /**
     * @return true, if the status of this enrollment is ACTIVE; false otherwise
     */
    @JsonIgnore
    public boolean isActive() {
        return status.equals(CampaignEnrollmentStatus.ACTIVE);
    }
}
