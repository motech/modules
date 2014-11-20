package org.motechproject.messagecampaign.service;

import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollmentStatus;
import org.motechproject.messagecampaign.search.Criterion;
import org.motechproject.messagecampaign.dao.CampaignEnrollmentDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class EnrollmentService {

    private static final Logger LOG = LoggerFactory.getLogger(EnrollmentService.class);

    @Autowired
    private CampaignEnrollmentDataService campaignEnrollmentDataService;

    public void register(CampaignEnrollment enrollment) {
        CampaignEnrollment existingEnrollment = campaignEnrollmentDataService.findByExternalIdAndCampaignName(enrollment.getExternalId(), enrollment.getCampaignName());
        if (existingEnrollment == null) {
            campaignEnrollmentDataService.create(enrollment);
        } else if (existingEnrollment.getStatus() == CampaignEnrollmentStatus.INACTIVE) {
            existingEnrollment.copyFrom(enrollment);
            campaignEnrollmentDataService.update(existingEnrollment);
        } else if (Objects.equals(existingEnrollment.getReferenceDate(), enrollment.getReferenceDate()) &&
                Objects.equals(existingEnrollment.getDeliverTime(), enrollment.getDeliverTime())) {
            LOG.warn("Attempted to register duplicate enrollment with ExternalID {} and Campaign name {}, with the" +
                    "same reference date and deliver time: {}/{}", new Object[]{ enrollment.getExternalId(),
                enrollment.getCampaignName(), enrollment.getReferenceDate(), enrollment.getDeliverTime() });
        } else {
            throw new IllegalArgumentException(String.format("Enrollment with ExternalID %s for Campaign %s, already exists, with different " +
                    "reference date and delivery time: %s/%s", enrollment.getExternalId(), enrollment.getCampaignName(),
                    existingEnrollment.getReferenceDate(), existingEnrollment.getDeliverTime()));
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

    public void delete(CampaignEnrollment enrollment) {
        campaignEnrollmentDataService.delete(enrollment);
    }


    public List<CampaignEnrollment> search(CampaignEnrollmentsQuery query) {
        List<CampaignEnrollment> enrollments = new ArrayList<>();
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
