package org.motechproject.ivr.it;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Specify which integration test classes to run
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({StatusControllerIT.class, ConfigServiceIT.class, OutboundCallServiceIT.class})
public class IntegrationTests {
}
