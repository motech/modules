package org.motechproject.ivr.event;

import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.ivr.service.OutboundCallService;
import org.motechproject.scheduler.contract.RunOnceSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static org.motechproject.ivr.event.EventParams.CONFIG;
import static org.motechproject.ivr.event.EventParams.CALL_DATE;
import static org.motechproject.ivr.event.EventParams.PARAMETERS;

/**
 * Listens to the ivr_initiate_call MotechEvent and calls initiateCall
 */
@Service
public class MotechEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MotechEventHandler.class);

    private OutboundCallService outboundCallService;
    private MotechSchedulerService schedulerService;

    @MotechListener(subjects = { EventSubjects.INITIATE_CALL })
    public void handleExternal(MotechEvent event) {
        LOGGER.info("Handling Motech event {}: {}", event.getSubject(), event.getParameters().toString());

        String config = event.getParameters().get(CONFIG).toString();
        Object date = event.getParameters().get(CALL_DATE);

        if (date == null) {
            //todo: fix "uses unchecked or unsafe operations" warning below
            Map<String, String> params = (Map<String, String>) event.getParameters().get(PARAMETERS);
            outboundCallService.initiateCall(config, params);
        } else {

            LOGGER.info("Rescheduling call for config {} to {}", config, date);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put(CONFIG, config);
            parameters.put(PARAMETERS, event.getParameters().get(PARAMETERS));

            MotechEvent runOnceJobEvent = new MotechEvent(event.getSubject(), parameters);
            schedulerService.scheduleRunOnceJob(new RunOnceSchedulableJob(runOnceJobEvent, ((DateTime) date).toDate()));
        }
    }

    @Autowired
    public MotechEventHandler(OutboundCallService outboundCallService, MotechSchedulerService schedulerService) {
        this.outboundCallService = outboundCallService;
        this.schedulerService = schedulerService;
    }
}
