package org.motechproject.messagecampaign.service;

import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.springframework.stereotype.Component;

/**
 * Maps domain representation of a campaign enrollment into dto representation,
 * that is used by the {@link MessageCampaignService}.
 */
@Component
public class CampaignEnrollmentRecordMapper {

    /**
     * Maps provided domain representation to {@link CampaignEnrollmentRecord}.
     *
     * @param enrollment domain representation of campaign enrollment
     * @return dto representation of campaign enrollment
     */
    public CampaignEnrollmentRecord map(CampaignEnrollment enrollment) {
        if (enrollment == null) {
            return null;
        }
        return new CampaignEnrollmentRecord(enrollment.getExternalId(), enrollment.getCampaignName(), enrollment.getReferenceDate(), enrollment.getStatus());
    }
}
