package org.motechproject.outbox.context;

import org.motechproject.outbox.service.VoiceOutboxService;
import org.springframework.beans.factory.annotation.Autowired;


public final class OutboxContext {

    @Autowired
    private VoiceOutboxService voiceOutboxService;

    public VoiceOutboxService getVoiceOutboxService() {
        return voiceOutboxService;
    }

    public static OutboxContext getInstance() {
        return instance;
    }

    private static OutboxContext instance = new OutboxContext();

    private OutboxContext() {

    }
}
