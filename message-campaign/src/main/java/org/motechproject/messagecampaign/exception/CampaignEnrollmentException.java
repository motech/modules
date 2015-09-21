package org.motechproject.messagecampaign.exception;

/**
 * Signals than an exception occurred during campaign enrollment process
 */
public class CampaignEnrollmentException extends RuntimeException {

    private static final long serialVersionUID = -6298354309678521941L;

    public CampaignEnrollmentException(String message) {
        super(message);
    }
}
