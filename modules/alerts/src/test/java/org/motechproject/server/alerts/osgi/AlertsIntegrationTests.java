package org.motechproject.server.alerts.osgi;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AlertFilterBundleIT.class, AlertsBundleIT.class, AlertServiceBundleIT.class,
        AlertsDataServiceIT.class
})
public class AlertsIntegrationTests {
}
