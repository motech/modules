package org.motechproject.csd.scheduler;

import org.motechproject.csd.CSDEventKeys;
import org.motechproject.csd.service.CSDService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CSDEventListener {

    private CSDService csdService;

    @Autowired
    public CSDEventListener(CSDService csdService) {
        this.csdService = csdService;
    }

    @MotechListener(subjects = { CSDEventKeys.CONSUME_XML_EVENT })
    public void consumeXml(MotechEvent event) {
        csdService.fetchAndUpdate();
    }
}
