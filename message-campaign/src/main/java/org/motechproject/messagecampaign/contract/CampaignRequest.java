package org.motechproject.messagecampaign.contract;

import org.joda.time.LocalDate;
import org.motechproject.commons.date.model.Time;

/**
 * This is the document to enroll an entity into a campaign
 */
public class CampaignRequest {
    private String externalId;
    private String campaignName;
    private Time startTime;
    private LocalDate referenceDate;

    public CampaignRequest() {
    }

    /**
     * Creates a request for enrollment into a campaign. Holds all fields required for the enrollment.
     *
     * @param externalId a client defined id to identify the enrollment
     * @param campaignName the campaign into which the entity should be enrolled
     * @param referenceDate the date the campaign has started for this enrollment. It can be a past date resulting in a delayed enrollment.
     * @param startTime time of the day at which the alert must be raised. This overrides the campaign's deliverTime.
     */
    public CampaignRequest(String externalId, String campaignName, LocalDate referenceDate, Time startTime) {
        this.externalId = externalId;
        this.campaignName = campaignName;
        this.referenceDate = referenceDate;
        this.startTime = startTime;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String campaignName() {
        return this.campaignName;
    }

    public String externalId() {
        return this.externalId;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time deliverTime() {
        return this.startTime;
    }

    public LocalDate referenceDate() {
        return referenceDate;
    }

    public void setReferenceDate(LocalDate referenceDate) {
        this.referenceDate = referenceDate;
    }

    @Override
    public String toString() {
        return "CampaignRequest{" +
                "externalId='" + externalId + '\'' +
                ", campaignName='" + campaignName + '\'' +
                ", referenceDate=" + referenceDate +
                '}';
    }
}
