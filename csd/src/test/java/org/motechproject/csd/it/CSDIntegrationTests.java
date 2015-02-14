package org.motechproject.csd.it;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Specify which integration test classes to run
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({FacilityServiceBundleIT.class})
public class CSDIntegrationTests {
}
