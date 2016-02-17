package org.motechproject.ipf.handler;

import groovy.lang.Writable;
import groovy.text.Template;
import groovy.text.XmlTemplateEngine;
import org.apache.commons.lang.ArrayUtils;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.ipf.domain.IPFRecipient;
import org.motechproject.ipf.exception.RecipientNotFoundException;
import org.motechproject.ipf.exception.TemplateNotFoundException;
import org.motechproject.ipf.domain.IPFTemplate;
import org.motechproject.ipf.event.EventSubjects;
import org.motechproject.ipf.service.IPFRecipientsService;
import org.motechproject.ipf.service.IPFTemplateDataService;
import org.motechproject.ipf.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Map;

/**
 * Component which listens for Motech events and sends data to the HL7 recipients.
 *
 * @see org.motechproject.ipf.domain.IPFTemplate
 * @see org.motechproject.ipf.domain.IPFRecipient
 * @see org.motechproject.ipf.event.EventSubjects
 */
@Component
public class IPFActionEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(IPFActionEventHandler.class);
    private static final String TEMPLATE_DATA_FIELD_NAME = "templateData";

    @Autowired
    private IPFTemplateDataService ipfTemplateDataService;

    @Autowired
    private IPFRecipientsService ipfRecipientsService;

    /**
     * Handles an event and sends data to the HL7 recipient. Template data, recipient name and template name are in the
     * event parameters.
     *
     * @param event the event which contains data for template
     * @throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException when cannot generate data
     * @throws TemplateNotFoundException when a template does not exist in database
     * @throws RecipientNotFoundException when a recipient does not exist
     * from xml template or when template is incorrect
     */
    @MotechListener(subjects =  {EventSubjects.ALL_TEMPLATE_ACTIONS})
    @Transactional
    public void handleIpfTaskAction(MotechEvent event) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException {
        LOGGER.info("Event handled {}", event.getSubject());
        Map<String, Object> eventData = event.getParameters();

        String templateName = (String) eventData.get(Constants.TEMPLATE_NAME_PARAM);
        String recipientName = (String) eventData.get(Constants.RECIPIENT_NAME_PARAM);

        IPFTemplate ipfTemplate = ipfTemplateDataService.findByName(templateName);
        if (ipfTemplate == null) {
            LOGGER.error("Cannot find {} template", templateName);
            throw new TemplateNotFoundException(templateName);
        }

        IPFRecipient ipfRecipient = ipfRecipientsService.getRecipientbyName(recipientName);
        if (ipfRecipient == null) {
            LOGGER.error("Cannot find {} recipient", recipientName);
            throw new RecipientNotFoundException(templateName);
        }

        Byte[] templateData = (Byte[]) ipfTemplateDataService.getDetachedField(ipfTemplate, TEMPLATE_DATA_FIELD_NAME);
        XmlTemplateEngine xmlTemplateEngine = new XmlTemplateEngine();
        Template xmlTemplate = xmlTemplateEngine.createTemplate(new String(ArrayUtils.toPrimitive(templateData)));
        Writable writable = xmlTemplate.make(event.getParameters());
        LOGGER.info("Template with name {}:\n{}", ipfTemplate.getTemplateName(), writable.toString());
    }
}
