package org.motechproject.alarms.scheduler;

import org.motechproject.alarms.constants.AlarmsConstants;
import org.motechproject.alarms.service.AlarmsService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AlarmsEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmsEventListener.class);

    @Autowired
    private AlarmsService alarmsService;

    @MotechListener(subjects = { AlarmsConstants.ALARM_EVENT })
    public void checkAlarm(MotechEvent event) {
        LOGGER.debug("Handling Motech event {}: {}", event.getSubject(), event.getParameters().toString());

        Long alarmId = (Long) event.getParameters().get((AlarmsConstants.ALARM_EVENT_ID));
        alarmsService.checkAlarm(alarmId);
    }
}
