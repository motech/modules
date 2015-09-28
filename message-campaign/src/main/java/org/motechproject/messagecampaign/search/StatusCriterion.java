package org.motechproject.messagecampaign.search;

import org.motechproject.messagecampaign.dao.CampaignEnrollmentDataService;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollmentStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link Criterion}, that fetches and filters records based on the
 * enrollment status.
 */
public class StatusCriterion implements Criterion {

    private CampaignEnrollmentStatus campaignEnrollmentStatus;

    public StatusCriterion(CampaignEnrollmentStatus campaignEnrollmentStatus) {
        this.campaignEnrollmentStatus = campaignEnrollmentStatus;
    }

    @Override
    public List<CampaignEnrollment> fetch(CampaignEnrollmentDataService campaignEnrollmentDataService) {
        return campaignEnrollmentDataService.findByStatus(campaignEnrollmentStatus);
    }

    @Override
    public List<CampaignEnrollment> filter(List<CampaignEnrollment> campaignEnrollments) {
        List<CampaignEnrollment> filteredEnrollments = new ArrayList<>();
        for (CampaignEnrollment enrollment : campaignEnrollments) {
            if (campaignEnrollmentStatus.equals(enrollment.getStatus())) {
                filteredEnrollments.add(enrollment);
            }
        }
        return filteredEnrollments;
    }
}
