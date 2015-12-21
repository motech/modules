package org.motechproject.messagecampaign.exception;

/**
 * Thrown when the validation of the campaign message fails.
 */
public class CampaignMessageValidationException extends CampaignValidationException {

    private static final long serialVersionUID = 244387554165254275L;

    public CampaignMessageValidationException(String message) {
        super(message);
    }

    public CampaignMessageValidationException(String message, Exception ex) {
        super(message, ex);
    }
}
