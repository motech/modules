package org.motechproject.outbox.osgi.it;

import org.apache.http.impl.client.BasicResponseHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.outbox.api.domain.OutboundVoiceMessage;
import org.motechproject.outbox.api.service.VoiceOutboxService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.TestContext;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

import javax.inject.Inject;
import java.io.IOException;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class VoiceOutboxServiceBundleIT extends BasePaxIT {

    @Inject
    private VoiceOutboxService voiceOutboxService;

    @Test
    public void testVoiceOutboxService() {
        final String externalId = "VoiceOutboxServiceBundleIT-" + UUID.randomUUID();
        String messageId = null;
        try {
            OutboundVoiceMessage outboundVoiceMessage = new OutboundVoiceMessage();
            outboundVoiceMessage.setExternalId(externalId);
            voiceOutboxService.addMessage(outboundVoiceMessage);
            messageId = outboundVoiceMessage.getId();
            OutboundVoiceMessage message = voiceOutboxService.getMessageById(messageId);
            assertNotNull(message);
            assertEquals(externalId, message.getExternalId());
        } finally {
            voiceOutboxService.removeMessage(messageId);
        }
    }

    @Test
    public void testController() throws IOException, InterruptedException {
        final String response = getHttpClient().get(String.format("http://localhost:%d/outbox/vxml/outboxMessage?pId=123",
                TestContext.getJettyPort()), new BasicResponseHandler());
        assertTrue(response.contains("There are no pending messages in your outbox"));
    }
}
