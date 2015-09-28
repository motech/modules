package org.motechproject.messagecampaign.search;

import org.motechproject.messagecampaign.dao.CampaignEnrollmentDataService;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link Criterion}, that fetches and filters records based on the
 * campaign name.
 */
public class CampaignNameCriterion implements Criterion {

    private String campaignName;

    public CampaignNameCriterion(String campaignName) {
        this.campaignName = campaignName;
    }

    @Override
    public List<CampaignEnrollment> fetch(CampaignEnrollmentDataService campaignEnrollmentDataService) {
        return campaignEnrollmentDataService.findByCampaignName(campaignName);
    }

    @Override
    public List<CampaignEnrollment> filter(List<CampaignEnrollment> campaignEnrollments) {
        List<CampaignEnrollment> filteredEnrollments = new ArrayList<>();
        for (CampaignEnrollment enrollment : campaignEnrollments) {
            if (campaignName.equals(enrollment.getCampaignName())) {
                filteredEnrollments.add(enrollment);
            }
        }
        return filteredEnrollments;
    }
}

