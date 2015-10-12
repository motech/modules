package org.motechproject.messagecampaign.domain;

import junit.framework.Assert;
import org.junit.Test;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollmentStatus;

public class CampaignEnrollmentTest {

    @Test
    public void shouldSetStatusAsActiveOnCreatingEnrollment() {
        CampaignEnrollment enrollment = new CampaignEnrollment("123", "Campaign name");
        Assert.assertEquals(CampaignEnrollmentStatus.ACTIVE, enrollment.getStatus());
    }
}
