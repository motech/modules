package org.motechproject.scheduletracking.it;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.motechproject.scheduletracking.osgi.ScheduleTrackingBundleIT;

@RunWith(Suite.class)
@Suite.SuiteClasses({/*ScheduleTrackingTaskBundleIT.class, */AllEnrollmentsBundleIT.class, EnrollmentsSearchBundleIT.class,
        ScheduleTrackingServiceBundleIT.class, SchedulingFloatingAlertsWithoutPreferredTimeBundleIT.class,
        SchedulingFloatingAlertsWithPreferredTimeBundleIT.class, SchedulingWithoutPreferredTimeBundleIT.class,
        SchedulingWithPreferredTimeBundleIT.class, ScheduleTrackingBundleIT.class})
public class ScheduleTrackingIntegrationTests {
}
