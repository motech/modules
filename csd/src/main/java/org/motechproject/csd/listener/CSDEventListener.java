package org.motechproject.csd.listener;

import org.joda.time.DateTime;
import org.motechproject.csd.constants.CSDEventKeys;
import org.motechproject.csd.scheduler.CSDScheduler;
import org.motechproject.csd.service.CSDService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CSDEventListener {

    private CSDService csdService;

    private CSDScheduler csdScheduler;

    @Autowired
    public CSDEventListener(CSDService csdService, CSDScheduler csdScheduler) {
        this.csdService = csdService;
        this.csdScheduler = csdScheduler;
    }

    @MotechListener(subjects = { CSDEventKeys.CONSUME_XML_EVENT_WILDCARD })
    public void consumeXml(MotechEvent event) {
        Map<String, Object> params = event.getParameters();
        csdService.fetchAndUpdate(params.get(CSDEventKeys.XML_URL).toString());
        csdScheduler.sendScheduledUpdateEventMessage(params.get(CSDEventKeys.XML_URL).toString());
    }

    @MotechListener(subjects = { CSDEventKeys.CSD_TASK_REST_UPDATE })
    public void taskUpdateUsingREST(MotechEvent event) {
        Map<String, Object> params = event.getParameters();
        csdService.fetchAndUpdateUsingREST(params.get(CSDEventKeys.XML_URL).toString());
        csdScheduler.sendTaskUpdateEventMessage(params.get(CSDEventKeys.XML_URL).toString());
    }

    @MotechListener(subjects = { CSDEventKeys.CSD_TASK_SOAP_UPDATE })
    public void taskUpdateUsingSOAP(MotechEvent event) {
        Map<String, Object> params = event.getParameters();
        csdService.fetchAndUpdateUsingSOAP(params.get(CSDEventKeys.XML_URL).toString(),
                DateTime.parse(params.get(CSDEventKeys.LAST_MODIFIED).toString()));
        csdScheduler.sendTaskUpdateEventMessage(params.get(CSDEventKeys.XML_URL).toString());
    }
}
