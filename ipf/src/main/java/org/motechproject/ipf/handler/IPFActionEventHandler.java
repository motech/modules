package org.motechproject.ipf.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.ipf.domain.IPFTemplate;
import org.motechproject.ipf.event.EventSubjects;
import org.motechproject.ipf.service.IPFTemplateService;
import org.motechproject.ipf.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class IPFActionEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(IPFActionEventHandler.class);

    @Autowired
    private IPFTemplateService ipfTemplateService;

    @MotechListener(subjects =  {EventSubjects.ALL_TEMPLATE_ACTIONS})
    public void handleIpfTaskAction(MotechEvent event) {
        LOGGER.info("Event handled {}", event.getSubject());
        Map<String, Object> eventData = event.getParameters();

        String templateName = (String) eventData.get(Constants.TEMPLATE_NAME_PARAM);
        IPFTemplate ipfTemplate = ipfTemplateService.getTemplateByName(templateName);
        if (ipfTemplate == null) {
            LOGGER.error("Cannot find {} template", templateName);
        }
    }
}
