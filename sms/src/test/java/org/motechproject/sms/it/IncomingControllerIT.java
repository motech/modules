package org.motechproject.sms.it;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.sms.service.SmsService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;

import static junit.framework.Assert.assertNotNull;

/**
 * Verify IncomingController present & functional.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class IncomingControllerIT extends BasePaxIT{

    @Inject
    private SmsService smsService;

    @Before
    public void waitForBeans() {
        // To prevent the annoying "BeanFactory not initialized or already closed" errors
        try { Thread.sleep(2000); } catch (InterruptedException e) {  }
    }

    @Test
    public void verifyServiceFunctional() {
        getLogger().info("verifyServiceFunctional");

        assertNotNull(smsService);
    }
}
