package org.motechproject.messagecampaign.search;

import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.dao.CampaignEnrollmentDataService;

import java.util.List;

public interface Criterion {
    List<CampaignEnrollment> fetch(CampaignEnrollmentDataService campaignEnrollmentDataService);

    List<CampaignEnrollment> filter(List<CampaignEnrollment> campaignEnrollments);
}


