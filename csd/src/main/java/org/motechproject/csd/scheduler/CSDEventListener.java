package org.motechproject.csd.scheduler;

import org.motechproject.csd.CSDEventKeys;
import org.motechproject.csd.client.CSDHttpClient;
import org.motechproject.csd.service.CSDService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CSDEventListener {

    private CSDHttpClient csdHttpClient;

    private CSDService csdService;

    @Autowired
    public CSDEventListener(CSDHttpClient csdHttpClient, CSDService csdService) {
        this.csdHttpClient = csdHttpClient;
        this.csdService = csdService;
    }

    @MotechListener(subjects = { CSDEventKeys.CONSUME_XML_EVENT })
    public void consumeXml(MotechEvent event) {

        String xmlUrl = (String) event.getParameters().get(CSDEventKeys.XML_URL);

        if (xmlUrl == null) {
            throw new IllegalArgumentException("Xml url cannot be null");
        }

        String xml = csdHttpClient.getXml(xmlUrl);

        if (xml == null) {
            throw new IllegalArgumentException("Cannot load xml");
        }

        csdService.saveFromXml(xml);
    }
}
