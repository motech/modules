package org.motechproject.sms.service;

/**
 * Send an SMS
 */
public interface SmsService {
    void send(final OutgoingSms message);
}
