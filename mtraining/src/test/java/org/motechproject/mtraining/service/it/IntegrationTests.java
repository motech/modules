package org.motechproject.mtraining.service.it;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Parent IT class to run all the individual service ITs
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({MTrainingServiceIT.class, BookmarkServiceIT.class, ActivityServiceIT.class})
public class IntegrationTests {
}
