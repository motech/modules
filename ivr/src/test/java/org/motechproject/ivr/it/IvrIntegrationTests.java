package org.motechproject.ivr.it;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Specify which integration test classes to run
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({StatusControllerBundleIT.class, TemplateControllerBundleIT.class, ConfigServiceBundleIT.class,
        OutboundCallServiceBundleIT.class})
public class IvrIntegrationTests {
}
