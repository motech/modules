package org.motechproject.messagecampaign.search;

import org.motechproject.messagecampaign.dao.CampaignEnrollmentDataService;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link Criterion}, that fetches and filters records based on the
 * client external ID.
 */
public class ExternalIdCriterion implements Criterion {

    private String externalId;

    public ExternalIdCriterion(String externalId) {
        this.externalId = externalId;
    }

    @Override
    public List<CampaignEnrollment> fetch(CampaignEnrollmentDataService campaignEnrollmentDataService) {
        return campaignEnrollmentDataService.findByExternalId(externalId);
    }

    @Override
    public List<CampaignEnrollment> filter(List<CampaignEnrollment> campaignEnrollments) {
        List<CampaignEnrollment> filteredEnrollment = new ArrayList<>();
        for (CampaignEnrollment enrollment : campaignEnrollments) {
            if (externalId.equals(enrollment.getExternalId())) {
                filteredEnrollment.add(enrollment);
            }
        }
        return filteredEnrollment;
    }
}
