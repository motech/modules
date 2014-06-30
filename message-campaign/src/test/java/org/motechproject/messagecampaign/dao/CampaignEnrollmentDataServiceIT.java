package org.motechproject.messagecampaign.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollmentStatus;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

import javax.inject.Inject;
import java.util.List;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class CampaignEnrollmentDataServiceIT extends BasePaxIT {

    @Inject
    private CampaignEnrollmentDataService campaignEnrollmentDataService;

    private String externalId = "9500012332";
    private String campaignName = "REPEATING_CHILDCARE";

    @Before
    public void setUp() {
        campaignEnrollmentDataService.deleteAll();
    }

    @Test
    public void shouldFindByExternalIdAndCampaignName() {
        CampaignEnrollment enrollment = new CampaignEnrollment(externalId, campaignName);
        enrollment.setReferenceDate(DateUtil.newDate(2011, 11, 1));
        campaignEnrollmentDataService.create(enrollment);

        CampaignEnrollment actualEnrollment = campaignEnrollmentDataService.findByExternalIdAndCampaignName(externalId, campaignName);
        assertEquals(enrollment.getExternalId(), actualEnrollment.getExternalId());
        assertEquals(enrollment.getReferenceDate(), actualEnrollment.getReferenceDate());
    }

    @Test
    public void shouldCreateNewEnrollmentIfSavedForFirstTime() {
        CampaignEnrollment enrollment = new CampaignEnrollment(externalId, campaignName);
        enrollment.setReferenceDate(DateUtil.newDate(2011, 11, 1));
        campaignEnrollmentDataService.create(enrollment);

        assertEquals(campaignEnrollmentDataService.retrieveAll().size(), 1);

        campaignEnrollmentDataService.update(enrollment);

        assertEquals(asList(campaignName), extract(campaignEnrollmentDataService.retrieveAll(), on(CampaignEnrollment.class).getCampaignName()));
        assertEquals(asList(externalId), extract(campaignEnrollmentDataService.retrieveAll(), on(CampaignEnrollment.class).getExternalId()));
        assertEquals(asList(DateUtil.newDate(2011, 11, 1)), extract(campaignEnrollmentDataService.retrieveAll(), on(CampaignEnrollment.class).getReferenceDate()));
    }

    @Test
    public void shouldReturnEnrollmentsThatMatchGivenStatus() {
        CampaignEnrollment activeEnrollment1 = new CampaignEnrollment("active_external_id_1", campaignName);
        campaignEnrollmentDataService.create(activeEnrollment1);

        CampaignEnrollment activeEnrollment2 = new CampaignEnrollment("active_external_id_2", campaignName);
        campaignEnrollmentDataService.create(activeEnrollment2);

        CampaignEnrollment inActiveEnrollment = new CampaignEnrollment("some_external_id", campaignName);
        inActiveEnrollment.setStatus(CampaignEnrollmentStatus.INACTIVE);
        campaignEnrollmentDataService.create(inActiveEnrollment);

        CampaignEnrollment completedEnrollment = new CampaignEnrollment("some_other_external_id", campaignName);
        completedEnrollment.setStatus(CampaignEnrollmentStatus.COMPLETED);
        campaignEnrollmentDataService.create(completedEnrollment);

        List<CampaignEnrollment> filteredEnrollments = campaignEnrollmentDataService.findByStatus(CampaignEnrollmentStatus.ACTIVE);
        assertEquals(asList(new String[]{"active_external_id_1", "active_external_id_2"}), extract(filteredEnrollments, on(CampaignEnrollment.class).getExternalId()));

        filteredEnrollments = campaignEnrollmentDataService.findByStatus(CampaignEnrollmentStatus.INACTIVE);
        assertEquals(asList(new String[]{"some_external_id"}), extract(filteredEnrollments, on(CampaignEnrollment.class).getExternalId()));
    }

    @Test
    public void shouldFindByExternalId() {
        campaignEnrollmentDataService.create(new CampaignEnrollment(externalId, campaignName));
        campaignEnrollmentDataService.create(new CampaignEnrollment(externalId, "different_campaign"));
        campaignEnrollmentDataService.create(new CampaignEnrollment("some_other_external_id", campaignName));

        List<CampaignEnrollment> filteredEnrollments = campaignEnrollmentDataService.findByExternalId(externalId);
        assertEquals(asList(new String[]{externalId, externalId}), extract(filteredEnrollments, on(CampaignEnrollment.class).getExternalId()));
    }

    @Test
    public void shouldFindByCampaignName() {
        campaignEnrollmentDataService.create(new CampaignEnrollment(externalId, campaignName));
        campaignEnrollmentDataService.create(new CampaignEnrollment(externalId, "different_campaign"));
        campaignEnrollmentDataService.create(new CampaignEnrollment("some_other_external_id", campaignName));

        List<CampaignEnrollment> filteredEnrollments = campaignEnrollmentDataService.findByCampaignName(campaignName);
        assertEquals(asList(new String[]{campaignName, campaignName}), extract(filteredEnrollments, on(CampaignEnrollment.class).getCampaignName()));
    }

    @Test
    public void shouldUpdateEnrollmentsWhenGivenId() {
        CampaignEnrollment enrollment = new CampaignEnrollment(externalId, campaignName);

        enrollment = campaignEnrollmentDataService.create(enrollment);

        enrollment.setExternalId("otherId");
        campaignEnrollmentDataService.update(enrollment);

        CampaignEnrollment found = campaignEnrollmentDataService.findByExternalId("otherId").get(0);

        assertEquals("otherId", found.getExternalId());
        assertTrue(campaignEnrollmentDataService.findByExternalId(externalId).isEmpty());
    }

    @After
    public void tearDown() {
        campaignEnrollmentDataService.deleteAll();
    }
}
