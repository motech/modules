package org.motechproject.ipf.handler.helper;

import groovy.lang.Writable;
import groovy.text.Template;
import groovy.text.XmlTemplateEngine;
import org.apache.commons.lang.ArrayUtils;
import org.motechproject.ipf.domain.IPFRecipient;
import org.motechproject.ipf.domain.IPFTemplate;
import org.motechproject.ipf.exception.RecipientNotFoundException;
import org.motechproject.ipf.exception.TemplateNotFoundException;
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
 * Helper used in order to make transactions work in handler methods.
 */
@Component
public class IPFActionHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(IPFActionHelper.class);
    private static final String TEMPLATE_DATA_FIELD_NAME = "templateData";

    @Autowired
    private IPFTemplateDataService ipfTemplateDataService;

    @Autowired
    private IPFRecipientsService ipfRecipientsService;

    /**
     * Sends data to the HL7 recipient. Template data, recipient name and template name are in the parameters map.
     *
     * @param parameters the parameters map
     * @throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException when cannot generate data
     * @throws TemplateNotFoundException when a template does not exist in database
     * @throws RecipientNotFoundException when a recipient does not exist
     * from xml template or when template is incorrect
     */
    @Transactional
    public void handleAction(Map<String, Object> parameters) throws IOException, ClassNotFoundException,
            ParserConfigurationException, SAXException {

        String templateName = (String) parameters.get(Constants.TEMPLATE_NAME_PARAM);
        String recipientName = (String) parameters.get(Constants.RECIPIENT_NAME_PARAM);

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
        Writable writable = xmlTemplate.make(parameters);
        LOGGER.info("Template with name {}:\n{}", ipfTemplate.getTemplateName(), writable.toString());
    }
}
