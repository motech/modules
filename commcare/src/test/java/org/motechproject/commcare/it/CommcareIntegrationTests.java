package org.motechproject.commcare.it;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({CommcareTasksIntegrationBundleIT.class, CommcareBundleIT.class, CommcareApplicationDataServiceBundleIT.class})
public class CommcareIntegrationTests {
}
