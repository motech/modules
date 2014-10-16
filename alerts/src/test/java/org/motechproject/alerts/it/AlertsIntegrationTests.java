package org.motechproject.alerts.it;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AlertFilterBundleIT.class, AlertsBundleIT.class, AlertServiceBundleIT.class,
        AlertsDataServiceBundleIT.class
})
public class AlertsIntegrationTests {
}
