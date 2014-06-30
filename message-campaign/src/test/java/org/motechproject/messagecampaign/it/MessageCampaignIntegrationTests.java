package org.motechproject.messagecampaign.it;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.motechproject.messagecampaign.dao.CampaignEnrollmentDataServiceIT;
import org.motechproject.messagecampaign.ft.MessageCampaignServiceFT;
import org.motechproject.messagecampaign.it.scheduler.DayOfWeekCampaignSchedulingIT;
import org.motechproject.messagecampaign.it.scheduler.RepeatCampaignSchedulingIT;
import org.motechproject.messagecampaign.osgi.MessageCampaignBundleIT;
import org.motechproject.messagecampaign.dao.CampaignRecordServiceIT;

@RunWith(Suite.class)
@Suite.SuiteClasses({MessageCampaignBundleIT.class, CampaignEnrollmentDataServiceIT.class, CampaignRecordServiceIT.class,
        MessageCampaignServiceIT.class, MessageCampaignServiceFT.class, DayOfWeekCampaignSchedulingIT.class, RepeatCampaignSchedulingIT.class})
public class MessageCampaignIntegrationTests {
}
