package org.motechproject.pillreminder.it;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({PillReminderBundleIT.class, PillRegimenDataServiceBundleIT.class,
        PillReminderServiceBundleIT.class})
public class PillReminderIntegrationTests {
}
