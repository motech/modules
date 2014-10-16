package org.motechproject.batch.it;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.motechproject.batch.mds.service.it.BatchJobMDSServiceBundleIT;
import org.motechproject.batch.mds.service.it.BatchJobParameterMDSServiceBundleIT;
import org.motechproject.batch.service.impl.it.JobServiceBundleIT;

@RunWith(Suite.class)
@Suite.SuiteClasses({BatchJobMDSServiceBundleIT.class, BatchJobParameterMDSServiceBundleIT.class,
        JobServiceBundleIT.class})
public class BatchIntegrationTests {
}
