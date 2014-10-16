package org.motechproject.messagecampaign.it;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.motechproject.messagecampaign.it.dao.CampaignEnrollmentDataServiceBundleIT;
import org.motechproject.messagecampaign.it.dao.CampaignRecordServiceBundleIT;
import org.motechproject.messagecampaign.it.osgi.MessageCampaignBundleIT;
import org.motechproject.messagecampaign.it.scheduler.DayOfWeekCampaignSchedulingBundleIT;
import org.motechproject.messagecampaign.it.scheduler.RepeatCampaignSchedulingBundleIT;

@RunWith(Suite.class)
@Suite.SuiteClasses({MessageCampaignBundleIT.class, CampaignEnrollmentDataServiceBundleIT.class,
        CampaignRecordServiceBundleIT.class, MessageCampaignServiceBundleIT.class,
        DayOfWeekCampaignSchedulingBundleIT.class, RepeatCampaignSchedulingBundleIT.class})
public class MessageCampaignIntegrationTests {
}
