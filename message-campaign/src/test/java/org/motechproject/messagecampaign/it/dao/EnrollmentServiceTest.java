package org.motechproject.messagecampaign.it.dao;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.commons.date.model.Time;
import org.motechproject.messagecampaign.dao.CampaignEnrollmentDataService;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollmentStatus;
import org.motechproject.messagecampaign.service.EnrollmentService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnrollmentServiceTest {

    private static final String EXTERNAL_ID = "2135";
    private static final String CAMPAIGN_NAME = "PREGNANCY PROGRAM";

    @InjectMocks
    private EnrollmentService enrollmentService = new EnrollmentService();

    @Mock
    private CampaignEnrollmentDataService dataService;

    @Test
    public void shouldRegisterNewEnrollment() {
        CampaignEnrollment enrollment = new CampaignEnrollment(EXTERNAL_ID, CAMPAIGN_NAME);
        enrollment.setDeliverTime(new Time(19, 10));
        enrollment.setReferenceDate(new LocalDate());

        enrollmentService.register(enrollment);

        verify(dataService).create(enrollment);
    }

    @Test
    public void shouldDoNoOpWhenRegisteringTheExactSameEnrollment() {
        final Time time = new Time(19, 10);
        final LocalDate referenceDate = new LocalDate();

        CampaignEnrollment enrollment = new CampaignEnrollment(EXTERNAL_ID, CAMPAIGN_NAME);
        enrollment.setDeliverTime(time);
        enrollment.setReferenceDate(referenceDate);

        CampaignEnrollment existing = new CampaignEnrollment(EXTERNAL_ID, CAMPAIGN_NAME);
        existing.setDeliverTime(time);
        existing.setReferenceDate(referenceDate);

        when(dataService.findByExternalIdAndCampaignName(EXTERNAL_ID, CAMPAIGN_NAME)).thenReturn(existing);

        enrollmentService.register(enrollment);

        verify(dataService, never()).create(any(CampaignEnrollment.class));
        verify(dataService, never()).update(any(CampaignEnrollment.class));
        verify(dataService, never()).delete(any(CampaignEnrollment.class));
    }

    @Test
    public void shouldUpdateInactiveEnrollments() {
        final LocalDate referenceDate = new LocalDate();

        CampaignEnrollment enrollment = new CampaignEnrollment(EXTERNAL_ID, CAMPAIGN_NAME);
        enrollment.setDeliverTime(new Time(10, 20));
        enrollment.setReferenceDate(referenceDate);

        CampaignEnrollment existing = new CampaignEnrollment(EXTERNAL_ID, CAMPAIGN_NAME);
        existing.setDeliverTime(new Time(6, 4));
        existing.setReferenceDate(referenceDate.plusDays(3));
        existing.setStatus(CampaignEnrollmentStatus.INACTIVE);

        when(dataService.findByExternalIdAndCampaignName(EXTERNAL_ID, CAMPAIGN_NAME)).thenReturn(existing);

        enrollmentService.register(enrollment);

        verify(dataService, never()).create(any(CampaignEnrollment.class));

        ArgumentCaptor<CampaignEnrollment> captor = ArgumentCaptor.forClass(CampaignEnrollment.class);
        verify(dataService).update(captor.capture());

        assertEquals(CampaignEnrollmentStatus.ACTIVE, captor.getValue().getStatus());
        assertEquals(referenceDate, captor.getValue().getReferenceDate());
        assertEquals(new Time(10, 20), captor.getValue().getDeliverTime());
        assertEquals(EXTERNAL_ID, captor.getValue().getExternalId());
        assertEquals(CAMPAIGN_NAME, captor.getValue().getCampaignName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForExistingEnrollmentWithDifferentTime() {
        final LocalDate referenceDate = new LocalDate();

        CampaignEnrollment enrollment = new CampaignEnrollment(EXTERNAL_ID, CAMPAIGN_NAME);
        enrollment.setReferenceDate(referenceDate);
        enrollment.setDeliverTime(new Time(10, 10));

        CampaignEnrollment existing = new CampaignEnrollment(EXTERNAL_ID, CAMPAIGN_NAME);
        existing.setReferenceDate(referenceDate);
        existing.setDeliverTime(new Time(10, 11));

        when(dataService.findByExternalIdAndCampaignName(EXTERNAL_ID, CAMPAIGN_NAME)).thenReturn(existing);

        enrollmentService.register(enrollment);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForExistingEnrollmentWithDifferentDate() {
        final Time time = new Time(12, 17);
        final LocalDate referenceDate = new LocalDate();

        CampaignEnrollment enrollment = new CampaignEnrollment(EXTERNAL_ID, CAMPAIGN_NAME);
        enrollment.setReferenceDate(referenceDate);
        enrollment.setDeliverTime(time);

        CampaignEnrollment existing = new CampaignEnrollment(EXTERNAL_ID, CAMPAIGN_NAME);
        existing.setReferenceDate(referenceDate.plusDays(1));
        existing.setDeliverTime(time);

        when(dataService.findByExternalIdAndCampaignName(EXTERNAL_ID, CAMPAIGN_NAME)).thenReturn(existing);

        enrollmentService.register(enrollment);
    }
}
