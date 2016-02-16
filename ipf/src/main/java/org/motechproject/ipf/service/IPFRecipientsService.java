package org.motechproject.ipf.service;

import org.motechproject.ipf.domain.IPFRecipient;

import java.util.Collection;

/**
 * Recipients service supports retrieving HL7 recipients data.
 *
 * @see org.motechproject.ipf.domain.IPFRecipient
 */
public interface IPFRecipientsService {

    /**
     * Returns all HL7 recipients.
     *
     * @return collection of HL7 recipients
     */
    Collection<IPFRecipient> getAllRecipients();

    /**
     * Returns the HL7 recipient with the given name.
     *
     * @param name the name of the recipient
     * @return the HL7 recipient
     */
    IPFRecipient getRecipientbyName(String name);
}
