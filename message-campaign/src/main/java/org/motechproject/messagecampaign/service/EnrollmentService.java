package org.motechproject.messagecampaign.service;

import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollmentStatus;
import org.motechproject.messagecampaign.search.Criterion;
import org.motechproject.messagecampaign.dao.CampaignEnrollmentDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EnrollmentService {

    @Autowired
    private CampaignEnrollmentDataService campaignEnrollmentDataService;

    public void register(CampaignEnrollment enrollment) {
        CampaignEnrollment campaignEnrollment = campaignEnrollmentDataService.findByExternalIdAndCampaignName(enrollment.getExternalId(), enrollment.getCampaignName());
        if (campaignEnrollment == null) {
            campaignEnrollmentDataService.create(enrollment);
        }
    }

    public void unregister(String externalId, String campaignName) {
        CampaignEnrollment enrollment = campaignEnrollmentDataService.findByExternalIdAndCampaignName(externalId, campaignName);
        if (enrollment != null) {
            enrollment.setStatus(CampaignEnrollmentStatus.INACTIVE);
            campaignEnrollmentDataService.update(enrollment);
        }
    }

    public void unregister(CampaignEnrollment enrollment) {
        enrollment.setStatus(CampaignEnrollmentStatus.INACTIVE);
        campaignEnrollmentDataService.update(enrollment);
    }

    public List<CampaignEnrollment> search(CampaignEnrollmentsQuery query) {
        List<CampaignEnrollment> enrollments = new ArrayList<CampaignEnrollment>();
        Criterion primaryCriterion = query.getPrimaryCriterion();
        if (primaryCriterion != null) {
            enrollments = primaryCriterion.fetch(campaignEnrollmentDataService);
        }
        for (Criterion criterion : query.getSecondaryCriteria()) {
            enrollments = criterion.filter(enrollments);
        }
        return enrollments;
    }
}
