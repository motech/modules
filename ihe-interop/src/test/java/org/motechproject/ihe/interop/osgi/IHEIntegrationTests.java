package org.motechproject.ihe.interop.osgi;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        InitializerBundleIT.class, IHETaskServiceBundleIT.class
})
public class IHEIntegrationTests {
}
