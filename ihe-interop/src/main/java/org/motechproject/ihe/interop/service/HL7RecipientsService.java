package org.motechproject.ihe.interop.service;

import org.motechproject.ihe.interop.domain.HL7Recipient;

import java.util.Collection;

/**
 * Recipients service supports retrieving HL7 recipients data.
 *
 * @see HL7Recipient
 */
public interface HL7RecipientsService {

    /**
     * Returns all HL7 recipients.
     *
     * @return collection of HL7 recipients
     */
    Collection<HL7Recipient> getAllRecipients();

    /**
     * Returns the HL7 recipient with the given name.
     *
     * @param name the name of the recipient
     * @return the HL7 recipient
     */
    HL7Recipient getRecipientbyName(String name);
}
