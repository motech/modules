package org.motechproject.ihe.interop.service;

import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;

/**
 * Service for CDA Templates
 */
public interface IHETemplateService {

    /**
     * Sends given CDA template to HL7 recipient URL.
     *
     * @param post method with URL address where template will be sent
     * @param template XML template with filled fields
     */
    void sendTemplateToRecipientUrl(String template, PostMethod post) throws IOException;
}
