package org.motechproject.sms.service;

/**
 * Service that allows sending SMS messages.
 */
public interface SmsService {

    /**
     * This method allows sending outgoing sms messages through HTTP. The configuration specified in the {@link OutgoingSms}
     * object will be used for dealing with the provider.
     * @param message the representation of the sms to send
     */
    void send(final OutgoingSms message);
}
