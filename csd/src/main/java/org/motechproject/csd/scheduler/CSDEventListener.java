package org.motechproject.csd.scheduler;

import org.motechproject.csd.constants.CSDEventKeys;
import org.motechproject.csd.service.CSDService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CSDEventListener {

    private CSDService csdService;

    @Autowired
    public CSDEventListener(CSDService csdService) {
        this.csdService = csdService;
    }

    @MotechListener(subjects = { CSDEventKeys.CONSUME_XML_EVENT_WILDCARD })
    public void consumeXml(MotechEvent event) {
        Map<String, Object> params = event.getParameters();
        csdService.fetchAndUpdate(params.get(CSDEventKeys.XML_URL).toString());
    }
}
