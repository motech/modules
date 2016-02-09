package org.motechproject.ipf.service;

import org.motechproject.ipf.domain.IPFRecipient;

import java.util.Collection;

public interface IPFRecipientsService {

    Collection<IPFRecipient> getAllRecipients();

    IPFRecipient getRecipientbyName(String name);
}
