package org.motechproject.messagecampaign.domain.campaign;

/**
 * The status of the enrollment into a campaign.
 */
public enum CampaignEnrollmentStatus {
    /**
     * The message campaign is currently active.
     */
    ACTIVE,

    /**
     * The message campaign has not yet started.
     */
    INACTIVE,

    /**
     * The message campaign has already ended.
     */
    COMPLETED
}
