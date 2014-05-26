package org.motechproject.callflow.osgi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.callflow.service.CallFlowServer;
import org.motechproject.callflow.service.FlowSessionService;
import org.motechproject.decisiontree.core.model.CallStatus;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class CallFlowBundleIT extends BasePaxIT {

    @Inject
    private CallFlowServer callFlowServer;
    @Inject
    private FlowSessionService flowSessionService;

    @Test
    public void testCallFlowServer() {
        String sessionId = "123a";
        String phoneNumber = "1234567890";
        String provider = "freeivr";

        try {
            ModelAndView mnv = callFlowServer.getResponse(sessionId, phoneNumber, provider, "sometree", CallStatus.Disconnect.toString(), "en");
            assertNotNull(mnv);
            assertNotNull(mnv.getViewName());
            assertTrue(mnv.getViewName().contains(provider));
        } finally {
            flowSessionService.removeCallSession(sessionId);
        }
    }
}
