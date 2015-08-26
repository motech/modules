package org.motechproject.sms.it;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Specify which integration test classes to run
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({SmsAuditServiceBundleIT.class, IncomingControllerBundleIT.class, SendControllerBundleIT.class,
        StatusControllerBundleIT.class})
public class SmsIntegrationTests {
    public static final int BUNDLE_MS_WAIT_TIME = 3000;
}
