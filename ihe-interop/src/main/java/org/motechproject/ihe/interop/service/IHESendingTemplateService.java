package org.motechproject.ihe.interop.service;

/**
 * Sending template service post xml data to HL7 recipient URL.
 */
public interface IHESendingTemplateService {

    /**
     * Send given CDA template to HL7 recipient URL.
     *
     * @param url URL address where template will be send
     * @param template XML template with filled fields
     */
    void sendTemplateToRecipientUrl(String url, String template);
}
