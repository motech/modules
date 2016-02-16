package org.motechproject.ipf.osgi;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        InitializerBundleIT.class, IPFTaskServiceBundleIT.class
})
public class IpfIntegrationTests {
}
