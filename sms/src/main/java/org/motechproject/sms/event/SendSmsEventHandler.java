package org.motechproject.sms.event;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.sms.SmsEventSubjects;
import org.motechproject.sms.http.SmsHttpService;
import org.motechproject.sms.service.OutgoingSms;
import org.motechproject.sms.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * When another module sends an SMS, it calls SmsService.send, which in turn sends one or more SEND_SMS events which
 * are handled here and passed straight through to to SmsHttpService.send
 */
@Service
public class SendSmsEventHandler {

    private SmsHttpService smsHttpService;
    private Logger logger = LoggerFactory.getLogger(SendSmsEventHandler.class);
    private SmsService smsService;

    @Autowired
    public SendSmsEventHandler(SmsHttpService smsHttpService, SmsService smsService) {
        this.smsHttpService = smsHttpService;
        this.smsService = smsService;
    }

    @MotechListener (subjects = { SmsEventSubjects.SEND_SMS })
    public void handleExternal(MotechEvent event) {
        logger.info("Handling external event {}: {}", event.getSubject(),
                event.getParameters().get("message").toString().replace("\n", "\\n"));
        smsService.send(new OutgoingSms(event));
    }

    @MotechListener (subjects = { SmsEventSubjects.PENDING, SmsEventSubjects.SCHEDULED, SmsEventSubjects.RETRYING })
    public void handleInternal(MotechEvent event) {
        logger.info("Handling internal event {}: {}", event.getSubject(),
                event.getParameters().get("message").toString().replace("\n", "\\n"));
        smsHttpService.send(new OutgoingSms(event));
    }
}

