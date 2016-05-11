package org.motechproject.ihe.interop.handler.helper;

import groovy.lang.Writable;
import groovy.text.Template;
import groovy.text.XmlTemplateEngine;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.ArrayUtils;
import org.motechproject.ihe.interop.domain.CdaTemplate;
import org.motechproject.ihe.interop.domain.HL7Recipient;
import org.motechproject.ihe.interop.exception.RecipientNotFoundException;
import org.motechproject.ihe.interop.exception.TemplateNotFoundException;
import org.motechproject.ihe.interop.service.HL7RecipientsService;
import org.motechproject.ihe.interop.service.IHETemplateDataService;
import org.motechproject.ihe.interop.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.ConnectException;
import java.util.Map;

/**
 * Helper used in order to make transactions work in handler methods.
 */
@Component
public class IHEActionHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(IHEActionHelper.class);
    private static final String TEMPLATE_DATA_FIELD_NAME = "templateData";

    @Autowired
    private IHETemplateDataService iheTemplateDataService;

    @Autowired
    private HL7RecipientsService hl7RecipientsService;

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

        CdaTemplate cdaTemplate = iheTemplateDataService.findByName(templateName);
        if (cdaTemplate == null) {
            LOGGER.error("Cannot find {} template", templateName);
            throw new TemplateNotFoundException(templateName);
        }

        HL7Recipient hl7Recipient = hl7RecipientsService.getRecipientbyName(recipientName);
        if (hl7Recipient == null) {
            LOGGER.error("Cannot find {} recipient", recipientName);
            throw new RecipientNotFoundException(templateName);
        }

        Byte[] templateData = (Byte[]) iheTemplateDataService.getDetachedField(cdaTemplate, TEMPLATE_DATA_FIELD_NAME);
        XmlTemplateEngine xmlTemplateEngine = new XmlTemplateEngine();
        Template xmlTemplate = xmlTemplateEngine.createTemplate(new String(ArrayUtils.toPrimitive(templateData)));
        Writable writable = xmlTemplate.make(parameters);
        LOGGER.info("Template with name {}:\n{}", cdaTemplate.getTemplateName(), writable.toString());
        sendTemplate(hl7Recipient.getRecipientUrl(), writable.toString());
    }

    private void sendTemplate(String url, String template) throws IOException{
        PostMethod post = new PostMethod(url);
        post.setRequestEntity(new StringRequestEntity(template, "application/xml", "utf-8"));
        HttpClient client = new HttpClient();
        LOGGER.info("Sending template to URL: {}", url);
        try {
            int responseCode = client.executeMethod(post);
            LOGGER.info("Response code: {}", responseCode);
            LOGGER.info("Response body: {}", post.getResponseBodyAsString());
        } catch (ConnectException e) {
            LOGGER.error("Cannot connect with URL: ", url);
        }
    }
}
