package org.motechproject.ihe.interop.service;

/**
 * Service for CDA Templates
 */
public interface IHETemplateService {

    /**
     * Sends given CDA template to HL7 recipient URL.
     *
     * @param url URL address where template will be sent
     * @param template XML template with filled fields
     */
    void sendTemplateToRecipientUrl(String url, String template);
}
