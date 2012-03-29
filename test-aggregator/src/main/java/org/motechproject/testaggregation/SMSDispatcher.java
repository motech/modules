package org.motechproject.testaggregation;

import org.motechproject.aggregator.aggregation.AggregateMotechEvent;
import org.motechproject.model.MotechEvent;
import org.motechproject.server.event.annotations.MotechListener;
import org.motechproject.sms.api.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SMSDispatcher {
    private final SmsService smsService;

    @Autowired
    public SMSDispatcher(SmsService smsService) {
        this.smsService = smsService;
    }

    @MotechListener(subjects = AggregateMotechEvent.SUBJECT)
    public void dispatch(MotechEvent aggregatedEvent) {
        System.out.println("Came here ...");
        List<MotechEvent> events = (List<MotechEvent>) aggregatedEvent.getParameters().get(AggregateMotechEvent.EVENTS_KEY);
        System.out.println("Found events: " + events);
    }
}
