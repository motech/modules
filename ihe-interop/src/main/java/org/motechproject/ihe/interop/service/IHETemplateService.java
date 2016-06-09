package org.motechproject.ihe.interop.service;

import org.motechproject.ihe.interop.domain.HL7Recipient;

import java.io.IOException;

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
    void sendTemplateToRecipientUrl(String url, String template) throws IOException;

    /**
     * Sends given CDA template to HL7 recipient with Basic Authentication.
     *
     * @param recipient Recipient which will receive template
     * @param template XML template with filled fields
     */
    void sendTemplateToRecipientUrlWithBA(HL7Recipient recipient, String template) throws IOException;
}
