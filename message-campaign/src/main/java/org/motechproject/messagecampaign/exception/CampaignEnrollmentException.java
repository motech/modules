package org.motechproject.messagecampaign.exception;

/**
 * Signals than an exception occurred during campaign enrollment process
 */
public class CampaignEnrollmentException extends RuntimeException {
    public CampaignEnrollmentException(String message) {
        super(message);
    }
}
