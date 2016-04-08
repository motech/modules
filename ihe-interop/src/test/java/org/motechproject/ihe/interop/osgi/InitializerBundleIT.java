package org.motechproject.ihe.interop.osgi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.ihe.interop.service.IHEInitializer;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;

import static junit.framework.Assert.assertNotNull;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class InitializerBundleIT extends BasePaxIT {

    @Inject
    private IHEInitializer iheInitializer;

    @Test
    public void testIfJavaServiceIsPresent() {
        assertNotNull(iheInitializer);
    }
}
