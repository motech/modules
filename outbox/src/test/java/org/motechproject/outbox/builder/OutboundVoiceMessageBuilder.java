package org.motechproject.outbox.builder;

import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.outbox.domain.OutboundVoiceMessage;
import org.motechproject.outbox.domain.OutboundVoiceMessageStatus;
import org.motechproject.outbox.domain.VoiceMessageType;

import java.util.Date;

public class OutboundVoiceMessageBuilder {

    OutboundVoiceMessage msg = new OutboundVoiceMessage();

    public OutboundVoiceMessageBuilder withDefaults() {
        VoiceMessageType mt = new VoiceMessageType();
        mt.setTemplateName("http://motech.2paths.com");

        msg.setExternalId("external id");
        msg.setStatus(OutboundVoiceMessageStatus.PENDING);
        msg.setCreationTime(DateUtil.now().toDate());
        msg.setExpirationDate(new Date());
        msg.setVoiceMessageType(mt);

        return this;
    }

    public OutboundVoiceMessageBuilder withCreationTime(Date creationTime) {
        msg.setCreationTime(creationTime);
        return this;
    }

    public OutboundVoiceMessageBuilder withExternalId(String externalId) {
        msg.setExternalId(externalId);
        return this;
    }

    public OutboundVoiceMessage build() {
        return msg;
    }

    public OutboundVoiceMessageBuilder withSequenceNumber(long sequenceNumber) {
        msg.setSequenceNumber(sequenceNumber);
        return this;
    }
}
