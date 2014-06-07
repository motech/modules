package org.motechproject.outbox.repository;

import org.motechproject.outbox.domain.OutboundVoiceMessage;

import java.util.Comparator;

class SequenceBasedOutboundVoiceMessageComparator implements Comparator<OutboundVoiceMessage> {
    @Override
    public int compare(OutboundVoiceMessage message1, OutboundVoiceMessage message2) {
        return Long.valueOf(message1.getSequenceNumber()).compareTo(message2.getSequenceNumber());
    }
}
