package org.motechproject.hub.it;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.motechproject.hub.mds.service.it.HubDistributionContentMDSServiceBundleIT;
import org.motechproject.hub.mds.service.it.HubPublisherTransactionMDSServiceBundleIT;
import org.motechproject.hub.mds.service.it.HubSubscriberTransactionMDSServiceBundleIT;
import org.motechproject.hub.mds.service.it.HubSubscriptionMDSServiceBundleIT;
import org.motechproject.hub.mds.service.it.HubTopicMDSServiceBundleIT;
import org.motechproject.hub.service.impl.it.SubscriptionServiceBundleIT;

@RunWith(Suite.class)
@Suite.SuiteClasses({HubDistributionContentMDSServiceBundleIT.class, HubPublisherTransactionMDSServiceBundleIT.class,
        HubSubscriberTransactionMDSServiceBundleIT.class, HubSubscriptionMDSServiceBundleIT.class,
        HubTopicMDSServiceBundleIT.class, SubscriptionServiceBundleIT.class})
public class HubIntegrationTests {
}
