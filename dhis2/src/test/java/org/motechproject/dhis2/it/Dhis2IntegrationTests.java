package org.motechproject.dhis2.it;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({TasksBundleIT.class, EventHandlerBundleIT.class, Dhis2SyncServiceIT.class})
public class Dhis2IntegrationTests {
}
