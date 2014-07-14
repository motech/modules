package org.motechproject.scheduletracking.it;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.motechproject.scheduletracking.osgi.ScheduleTrackingBundleIT;

@RunWith(Suite.class)
@Suite.SuiteClasses({AllEnrollmentsIT.class, EnrollmentsSearchIT.class, ScheduleTrackingServiceIT.class,
                     SchedulingFloatingAlertsWithoutPreferredTimeIT.class, SchedulingFloatingAlertsWithPreferredTimeIT.class,
                     SchedulingWithoutPreferredTimeIT.class, SchedulingWithPreferredTimeIT.class,
                     ScheduleTrackingBundleIT.class})
public class ScheduleTrackingIntegrationTests {
}
