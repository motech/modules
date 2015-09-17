package org.motechproject.messagecampaign.service;

import org.joda.time.LocalDate;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollmentStatus;

/**
 * This is the record which will be returned when message campaign service is queried for enrollments.
 * It holds the details of an enrollment.
 */
public class CampaignEnrollmentRecord {
    private String externalId;
    private String campaignName;
    private LocalDate referenceDate;
    private CampaignEnrollmentStatus status;

    /**
     * This is the constructor which is used to create an CampaignEnrollmentRecord
     *
     * @param externalId external ID of the created campaign enrollment
     * @param campaignName campaign name of the created campaign enrollment
     * @param referenceDate reference date of the created campaign enrollment
     * @param status status of the campaign enrollment
     */
    public CampaignEnrollmentRecord(String externalId, String campaignName, LocalDate referenceDate, CampaignEnrollmentStatus status) {
        this.externalId = externalId;
        this.campaignName = campaignName;
        this.referenceDate = referenceDate;
        this.status = status;
    }

    /**
     * This returns the External ID of a CampaignEnrollmentRecord
     *
     * @return external ID of the enrollment
     */
    public String getExternalId() {
        return externalId;
    }

    /**
     * This returns the Campaign Name of a CampaignEnrollmentRecord
     *
     * @return name of the campaign the enrollment is assigned to
     */
    public String getCampaignName() {
        return campaignName;
    }

    /**
     * This returns the Start Date of a CampaignEnrollmentRecord
     *
     * @return the reference date of this enrollment
     */
    public LocalDate getReferenceDate() {
        return referenceDate;
    }

    /**
     * This returns the Status of a CampaignEnrollmentRecord
     *
     * @return the status of this enrollment
     */
    public CampaignEnrollmentStatus getStatus() {
        return status;
    }
}
