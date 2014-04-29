package org.motechproject.mobileforms.api.osgi.it;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.tasks.domain.Channel;
import org.motechproject.tasks.domain.TriggerEvent;
import org.motechproject.tasks.osgi.test.AbstractTaskBundleIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.motechproject.mobileforms.api.callbacks.FormGroupPublisher.FORM_BEAN_GROUP;
import static org.motechproject.mobileforms.api.callbacks.FormGroupPublisher.FORM_ERROR;
import static org.motechproject.mobileforms.api.callbacks.FormGroupPublisher.FORM_VALID_FROMS;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class MobileFormsTaskBundleIT extends AbstractTaskBundleIT {

    private static final String CHANNEL_NAME = "org.motechproject.motech-mobileforms-api";

    @Inject
    private BundleContext bundleContext;

    @Test
    public void testValidFormTriggerExists() throws IOException, InterruptedException {
        assertTrigger(FORM_VALID_FROMS);
    }

    @Test
    public void testFormErrorTriggerExists() throws IOException, InterruptedException {
        assertTrigger(FORM_ERROR);
    }

    private void assertTrigger(String subject) throws IOException, InterruptedException {
        waitForChannel(CHANNEL_NAME);

        Channel channel = findChannel(CHANNEL_NAME);
        assertNotNull(channel);
        List<TriggerEvent> triggerTaskEvents = channel.getTriggerTaskEvents();
        TriggerEvent trigger = findTriggerEventBySubject(triggerTaskEvents, subject);
        assertNotNull(trigger);
        assertTrue(hasEventParameterKey(FORM_BEAN_GROUP, trigger.getEventParameters()));
    }
}
